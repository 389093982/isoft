package framework

import (
	"github.com/pkg/errors"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/block"
	"isoft/isoft_iwork_web/core/iworkdata/entry"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkutil/reflectutil"
	"isoft/isoft_iwork_web/models"
	"reflect"
	"strings"
)

type ForeachNode struct {
	node.BaseNode
	WorkStep         *models.WorkStep
	BlockStep        *block.BlockStep
	BlockStepRunFunc func(args *interfaces.RunOneStepArgs) (receiver *entry.Receiver)
}

// foreach 节点执行逻辑
func (this *ForeachNode) Execute(trackingId string) {
	if this.BlockStep.HasChildren {
		// 获取需要迭代的数据
		foreach_data := this.TmpDataMap[iworkconst.FOREACH_PREFIX+"foreach_data"]
		// 是可迭代的切片
		if !reflectutil.IsSlice(foreach_data) {
			panic("foreach_data is not a array!")
		}
		slis := reflectutil.InterfaceToSlice(foreach_data)
		for index, sli := range slis {
			this.PrepareIterParam(index, sli)
			this.runForeachChildren(trackingId)
		}
	} else {
		panic(errors.New("empty foreach was found!"))
	}
}

// 准备每一次迭代的数据
func (this *ForeachNode) PrepareIterParam(index int, v reflect.Value) {
	var (
		foreachDataAttr         string
		foreachDataAttrNotEmpty bool
	)
	foreachDataAttr, foreachDataAttrNotEmpty = this.TmpDataMap[iworkconst.COMPLEX_PREFIX+"foreach_data_attr?"].(string)

	paramMap := make(map[string]interface{})
	// 迭代索引
	paramMap[iworkconst.NUMBER_PREFIX+"foreach_index"] = index
	// 是对象属性
	if reflectutil.IsMap(v) {
		keys, values := reflectutil.InterfaceToMap(v)
		for i := 0; i < len(keys); i++ {
			paramMap["item.attr_"+keys[i].String()] = values[i].Interface()
		}
	} else if foreachDataAttrNotEmpty {
		paramMap["item.attr_"+foreachDataAttr] = v.Interface()
	} else {
		paramMap["item.attr_value"] = v.Interface()
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, paramMap)
}

func (this *ForeachNode) runForeachChildren(trackingId string) {
	bsoRunner := node.BlockStepOrdersRunner{
		ParentStepId: this.WorkStep.WorkStepId,
		WorkCache:    this.WorkCache,
		TrackingId:   trackingId,
		LogWriter:    this.LogWriter,
		Store:        this.DataStore, // 获取数据中心
		Dispatcher:   nil,
		RunOneStep:   this.BlockStepRunFunc,
	}
	bsoRunner.Run()
}

func (this *ForeachNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.FOREACH_PREFIX + "foreach_data", "迭代的元素"},
		2: {iworkconst.COMPLEX_PREFIX + "foreach_data_attr?", "迭代元素属性名,多个属性名用逗号分割,单个属性名可省略"},
	}
	return this.BPIS1(paramMap)
}

func (this *ForeachNode) GetRuntimeParamOutputSchema() *iworkmodels.ParamOutputSchema {
	items := make([]iworkmodels.ParamOutputSchemaItem, 0)
	inputSchema := this.ParamSchemaCacheParser.GetCacheParamInputSchema()

	var (
		//foreach_data_item *iworkmodels.ParamInputSchemaItem
		foreach_data_attr_item *iworkmodels.ParamInputSchemaItem
		//foreach_data string
		//foreach_data_attr string
	)
	for _, item := range inputSchema.ParamInputSchemaItems {
		if item.ParamName == iworkconst.FOREACH_PREFIX+"foreach_data" {
			//foreach_data_item = item
			//foreach_data = item.ParamValue
		} else if item.ParamName == iworkconst.COMPLEX_PREFIX+"foreach_data_attr?" {
			foreach_data_attr_item = item
			//foreach_data_attr = item.ParamValue
		}
	}
	//foreach_data = strings.TrimSpace(strings.ReplaceAll(foreach_data, ";", ""))
	//foreach_data_attr = strings.TrimSpace(strings.ReplaceAll(foreach_data_attr, foreach_data+".", ""))
	//attrs := strings.Split(foreach_data_attr, ";")
	//for _, attr := range attrs {
	//	if strings.TrimSpace(attr) != "" {
	//		// 每个字段放入 items 中
	//		items = append(items, iworkmodels.ParamOutputSchemaItem{
	//			ParamName: strings.TrimSpace(attr), ParentPath: "item",
	//		})
	//	}
	//}

	attrNames := getAttrNames(foreach_data_attr_item)
	if len(attrNames) == 0 {
		attrNames = append(attrNames, "value")
	}
	for _, attr := range attrNames {
		if strings.TrimSpace(attr) != "" {
			// 每个字段放入 items 中
			items = append(items, iworkmodels.ParamOutputSchemaItem{
				ParamName: "attr_" + strings.TrimSpace(attr), ParentPath: "item",
			})
		}
	}
	return &iworkmodels.ParamOutputSchema{ParamOutputSchemaItems: items}
}

func (this *ForeachNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{iworkconst.NUMBER_PREFIX + "foreach_index"})
}

// 获取所有的属性名
func getAttrNames(foreach_data_attr_item *iworkmodels.ParamInputSchemaItem) []string {
	if foreach_data_attr_item.PureText {
		return strings.Split(foreach_data_attr_item.ParamValue, ",")
	}
	return []string{}
}
