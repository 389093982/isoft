package params

import (
	"errors"
	"fmt"
	"isoft/isoft_iwork_web/core/iworkdata/datastore"
	"isoft/isoft_iwork_web/core/iworkdata/param"
	"isoft/isoft_iwork_web/core/iworkfunc"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/core/iworkutil/datatypeutil"
	"isoft/isoft_iwork_web/core/iworkvalid"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"reflect"
	"strconv"
	"strings"
)

// iworkmodels.ParamInputSchemaItem 解析类
type PisItemDataParser struct {
	DataStore          *datastore.DataStore
	Item               *iworkmodels.ParamInputSchemaItem
	PureTextTmpDataMap map[string]string
	TmpDataMap         map[string]interface{}
}

func (this *PisItemDataParser) FillPisItemDataToTmp() {
	// 对必要的参数进行非空判断
	this.checkEmpty()
	this.FillPisItemDataToPureTmp()
	this.FillPisItemDataToNPureTmp()
}

func (this *PisItemDataParser) FillPisItemDataToPureTmp() {
	this.PureTextTmpDataMap[this.Item.ParamName] = this.Item.ParamValue
}

func (this *PisItemDataParser) checkEmpty() {
	// 对参数进行非空校验
	if ok, checkResults := iworkvalid.CheckEmptyForItem(this.Item); !ok {
		panic(strings.Join(checkResults, ";"))
	}
}

func (this *PisItemDataParser) FillPisItemDataToNPureTmp() {
	// tmpDataMap 存储解析值
	if this.Item.PureText {
		// 表示为纯文本则放入字符串,不再解析
		this.TmpDataMap[this.Item.ParamName] = this.Item.ParamValue
		return
	}
	// 判断当前参数是否是 repeat 参数
	if !this.Item.Repeatable {
		this.TmpDataMap[this.Item.ParamName] = this.ParseAndGetParamVaule(this.Item.ParamName, this.Item.ParamValue) // 输入数据存临时
	} else {
		this.ForeachFillPisItemDataToTmp()
	}
}

func (this *PisItemDataParser) ForeachFillPisItemDataToTmp() {
	repeatDatas := make([]interface{}, 0)
	foreachRefer := this.TmpDataMap[this.Item.ForeachRefer]
	if foreachRefer != nil {
		// 获取 item.ForeachRefer 对应的 repeat 切片数据,作为迭代参数,而不再从前置节点输出获取
		repeatDatas = datatypeutil.InterfaceConvertToSlice(foreachRefer)
	}
	if len(repeatDatas) > 0 {
		paramValues := make([]interface{}, 0)
		for _, repeatData := range repeatDatas {
			// 替代的节点名称、替代的对象
			replaceProviderNodeName := strings.ReplaceAll(strings.TrimSpace(this.PureTextTmpDataMap[this.Item.ForeachRefer]), ";", "")
			replaceMap := map[string]interface{}{replaceProviderNodeName: repeatData}
			paramValue := this.ParseAndGetParamVaule(this.Item.ParamName, this.Item.ParamValue, replaceMap) // 输入数据存临时
			paramValues = append(paramValues, paramValue)
		}
		this.TmpDataMap[this.Item.ParamName] = paramValues // 所得值则是个切片
	} else {
		this.TmpDataMap[this.Item.ParamName] = this.ParseAndGetParamVaule(this.Item.ParamName, this.Item.ParamValue) // 输入数据存临时
	}
}

// 解析 paramVaule 并赋值,数据来源于前置节点(从 dataStore 中获取实际值)
// 解析结果可能的情况有多种：单值 interface{}, 多值 []interface{}, 对象值 map[string]interface{}
func (this *PisItemDataParser) ParseAndGetParamVaule(paramName, paramVaule string, replaceMap ...map[string]interface{}) interface{} {
	// 将 paramValue 解析成对象值 []*OobjectAttrs
	objectAttrs := this.parseToObjectAttrs(paramVaule)
	// 存储 []*OobjectAttrs 转换后的 map[string]interface{}
	objectMap := make(map[string]interface{}, 0)
	// 存储 []*AttrObjects 转换后的 []interface{}
	parseValues := make([]interface{}, 0)
	for _, objectAttr := range objectAttrs {
		objectAttr.attrParseValue = this.parseParamVaule(paramName, objectAttr.attrPureValue, replaceMap...)
		// 此处禁止使用 datatypeutil.InterfaceConvertToSlice(), 因为 parseValues 中的元素可以是个 interface{} 也可以是个 []interface{}
		parseValues, objectMap[objectAttr.attrName] = append(parseValues, objectAttr.attrParseValue), objectAttr.attrParseValue
	}
	// 单值
	if len(parseValues) == 1 {
		return parseValues[0]
	}
	// 空值
	if len(parseValues) == 0 {
		return nil
	}
	return parseValues
}

func (this *PisItemDataParser) parseToObjectAttr(index int, paramValue string) *ObjectAttr {
	var attrName, attrPureValue string
	if strings.Contains(paramValue, "::") {
		attrName = paramValue[:strings.Index(paramValue, "::")]
		attrPureValue = paramValue[strings.Index(paramValue, "::")+2:]
	} else if strings.Contains(paramValue, "$") {
		attrName = strings.ReplaceAll(paramValue[strings.LastIndex(paramValue, ".")+1:], ";", "")
		attrPureValue = paramValue
	} else {
		attrName, attrPureValue = string(index), paramValue
	}
	return &ObjectAttr{index: index, attrName: attrName, attrPureValue: attrPureValue}
}

// 属性对象
type ObjectAttr struct {
	index          int
	attrName       string      // 对象属性名
	attrPureValue  string      // 对象属性纯文本值
	attrParseValue interface{} // 对象属性解析值
}

// 从缓存中获取 FuncCallers
func (this *PisItemDataParser) GetMultiValsWithCache(paramValue string) ([]string, error) {
	multiVals, ok1 := this.DataStore.WC.MultiVals[paramValue]
	err, ok2 := this.DataStore.WC.MultiValError[paramValue]
	if ok1 || ok2 {
		return multiVals, err
	}
	this.DataStore.WC.ParseToMultiVals(paramValue)
	return this.GetMultiValsWithCache(paramValue)
}

// 将 paramVaule 转行成 []*ObjectAttr
func (this *PisItemDataParser) parseToObjectAttrs(paramVaule string) []*ObjectAttr {
	objectAttrs := make([]*ObjectAttr, 0)
	multiVals, err := this.GetMultiValsWithCache(paramVaule)
	if err != nil {
		panic(err)
	}
	for index, value := range multiVals {
		if _value := iworkutil.TrimParamValue(value); strings.TrimSpace(_value) != "" {
			objectAttr := this.parseToObjectAttr(index, strings.TrimSpace(_value))
			objectAttrs = append(objectAttrs, objectAttr)
		}
	}
	return objectAttrs
}

// 从缓存中获取 FuncCallers
func (this *PisItemDataParser) ParseToFuncCallersWithCache(paramValue string) ([]*iworkfunc.FuncCaller, error) {
	callers, ok1 := this.DataStore.WC.FuncCallerMap[paramValue]
	err, ok2 := this.DataStore.WC.FuncCallerErrMap[paramValue]
	if ok1 || ok2 {
		return callers, err
	}
	this.DataStore.WC.ParseToFuncCallers(paramValue)
	return this.ParseToFuncCallersWithCache(paramValue)
}

func (this *PisItemDataParser) parseParamVaule(paramName, paramValue string, replaceMap ...map[string]interface{}) interface{} {
	defer func() {
		if err := recover(); err != nil {
			str := "<span style='color:red;'>execute func with expression is %s, error msg is :%s</span>"
			panic(fmt.Sprintf(str, paramValue, err.(error).Error()))
		}
	}()

	callers, err := this.ParseToFuncCallersWithCache(paramValue)
	if err != nil {
		panic(err)
	}
	if callers == nil || len(callers) == 0 {
		// 是直接参数,不需要函数进行特殊处理
		parser := &SimpleParser{
			paramName:  paramName,
			paramVaule: paramValue,
			DataStore:  this.DataStore,
		}
		return parser.parseParamValue(replaceMap...)
	} else {
		return this.parseParamVauleWithCallers(callers, paramName, replaceMap...)
	}
}

func (this *PisItemDataParser) parseParamVauleWithCallers(callers []*iworkfunc.FuncCaller, paramName string, replaceMap ...map[string]interface{}) interface{} {
	historyFuncResultMap := make(map[string]interface{}, 0)
	var lastFuncResult interface{}
	// 按照顺序依次执行函数
	for _, caller := range callers {
		args := this.getCallerArgs(caller, historyFuncResultMap, paramName, replaceMap...)
		// 执行函数并记录结果,供下一个函数执行使用
		result := iworkfunc.ExecuteFuncCaller(caller, args)
		historyFuncResultMap["$func."+caller.FuncUUID], lastFuncResult = result, result
	}
	return lastFuncResult
}

// 函数参数替换成实际意义上的值
func (this *PisItemDataParser) getCallerArgs(caller *iworkfunc.FuncCaller,
	historyFuncResultMap map[string]interface{}, paramName string, replaceMap ...map[string]interface{}) []interface{} {
	args := make([]interface{}, 0)
	for _, arg := range caller.FuncArgs {
		// 判断参数是否来源于 historyFuncResultMap
		if _arg, ok := historyFuncResultMap[arg]; ok {
			args = append(args, _arg)
		} else {
			// 是直接参数,不需要函数进行特殊处理
			parser := &SimpleParser{
				paramName:  paramName,
				paramVaule: arg,
				DataStore:  this.DataStore,
			}
			_arg := parser.parseParamValue(replaceMap...)
			args = append(args, _arg)
		}
	}
	return args
}

type SimpleParser struct {
	paramName  string
	paramVaule string
	DataStore  *datastore.DataStore
}

func (this *SimpleParser) parseParamVauleWithReplaceProviderNode(app_id int64, replaceMap ...map[string]interface{}) interface{} {
	for replaceProviderNodeName, replaceProviderData := range replaceMap[0] {
		replaceProviderNodeName = strings.ReplaceAll(replaceProviderNodeName, ";", "")
		if strings.HasPrefix(this.paramVaule, replaceProviderNodeName) {
			attr := strings.Replace(this.paramVaule, replaceProviderNodeName+".", "", 1)
			attr = strings.ReplaceAll(attr, ";", "")
			return replaceProviderData.(map[string]interface{})[attr]
		}
	}
	return nil
}

// paramValue 来源于前置节点
func (this *SimpleParser) parseParamVauleWithPrefixNode(app_id int64) interface{} {
	// 格式校验
	if !strings.HasPrefix(this.paramVaule, "$") {
		panic(errors.New(fmt.Sprintf("%s ~ %s is not start with $", this.paramName, this.paramVaule)))
	}
	resolver := param.ParamVauleParser{ParamValue: this.paramVaule}
	nodeName := resolver.GetNodeNameFromParamValue()
	this.paramName = resolver.GetParamNameFromParamValue()
	paramValue := this.DataStore.GetData(nodeName, this.paramName) // 作为直接对象, dataStore 里面可以直接获取
	if paramValue != nil {
		return paramValue
	}
	if strings.Contains(this.paramName, ".") {
		_paramName := this.paramName[:strings.LastIndex(this.paramName, ".")]
		datas := this.DataStore.GetData(nodeName, _paramName) // 作为对象属性
		if datas == nil {
			return nil // 对象直接不存在，后续不必执行
		}
		attr := this.paramName[strings.LastIndex(this.paramName, ".")+1:]
		if reflect.TypeOf(datas).Kind() == reflect.Slice {
			return reflect.ValueOf(datas).Index(0).Interface().(map[string]interface{})[attr]
		}
		return datas.(map[string]interface{})[attr]
	} else {
		return paramValue
	}
}

// 尽量从缓存中获取
func (this *SimpleParser) parseParamVauleFromResource(app_id int64) interface{} {
	resource_name := strings.TrimPrefix(this.paramVaule, "$RESOURCE.")
	if resource, ok := memory.ResourceMap.Load(string(app_id) + "_" + resource_name); ok {
		resource := resource.(*models.Resource)
		if resource.ResourceType == "db" {
			if strings.HasPrefix(resource.ResourceDsn, "$Global.") {
				// 重新去查询全局变量
				this.paramVaule = resource.ResourceDsn
				return this.parseParamVauleFromGlobalVar(app_id)
			} else {
				return resource.ResourceDsn
			}
		} else if resource.ResourceType == "sftp" || resource.ResourceType == "ssh" {
			return resource
		}
		return ""
	} else {
		panic(errors.New(fmt.Sprintf("can't find resource for %s", resource_name)))
	}
}

// 尽量从缓存中获取
func (this *SimpleParser) parseParamVauleFromGlobalVar(app_id int64) interface{} {
	gvName := strings.TrimPrefix(this.paramVaule, "$Global.")
	gvName = strings.TrimSuffix(gvName, ";")
	if gv, ok := memory.GlobalVarMap.Load(fmt.Sprintf("%d_%s_%s", app_id, gvName, sysconfig.ENV_ONUSE)); ok {
		return strings.TrimSpace(gv.(*models.GlobalVar).Value)
	} else if gv, ok := memory.GlobalVarMap.Load(fmt.Sprintf("%d_%s_%s", app_id, gvName, "dev")); ok {
		return strings.TrimSpace(gv.(*models.GlobalVar).Value)
	} else {
		panic(errors.New(fmt.Sprintf("can't find globalVar for %s", gvName)))
	}
}

// 是直接参数,不需要函数进行特殊处理
func (this *SimpleParser) parseParamValue(replaceMap ...map[string]interface{}) interface{} {
	app_id := this.DataStore.WC.Work.AppId
	this.paramVaule = iworkfunc.DncodeSpecialForParamVaule(this.paramVaule)
	// 变量
	if strings.HasPrefix(strings.ToUpper(this.paramVaule), "$GLOBAL.") {
		return this.parseParamVauleFromGlobalVar(app_id)
	} else if strings.HasPrefix(strings.ToUpper(this.paramVaule), "$RESOURCE.") {
		return this.parseParamVauleFromResource(app_id)
	} else if strings.HasPrefix(strings.ToUpper(this.paramVaule), "$WORK.") {
		return iworkutil.GetWorkSubNameFromParamValue(this.paramVaule)
	} else if strings.HasPrefix(strings.ToUpper(this.paramVaule), "$ENTITY.") {
		return iworkutil.GetParamValueForEntity(app_id, this.paramVaule)
	} else if strings.HasPrefix(strings.ToUpper(this.paramVaule), "$") {
		if len(replaceMap) > 0 {
			if paramVaule := this.parseParamVauleWithReplaceProviderNode(app_id, replaceMap...); paramVaule != nil {
				return paramVaule
			}
		}
		return this.parseParamVauleWithPrefixNode(app_id)
	} else if strings.HasPrefix(this.paramVaule, "`") && strings.HasSuffix(this.paramVaule, "`") {
		// 字符串
		return this.paramVaule[1 : len(this.paramVaule)-1]
	} else {
		// 数字, 默认转换成 int64
		if int64Val, err := strconv.ParseInt(this.paramVaule, 10, 64); err == nil {
			return int64Val
		}
		return this.paramVaule
	}
}
