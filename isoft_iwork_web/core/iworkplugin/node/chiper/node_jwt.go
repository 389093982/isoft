package chiper

import (
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/param"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/chiperutil"
	"strconv"
	"strings"
)

type CreateJWTNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *CreateJWTNode) Execute(trackingId string) {
	secretKey := this.TmpDataMap[iworkconst.STRING_PREFIX+"secretKey"].(string)
	_expireSecond := this.TmpDataMap[iworkconst.NUMBER_PREFIX+"expireSecond"].(string)
	expireSecond, _ := strconv.ParseInt(_expireSecond, 10, 64)
	_claimsMap := this.TmpDataMap[iworkconst.STRING_PREFIX+"claimsMap"].(string)
	claimsMap := make(map[string]string)
	_claimsArr := strings.Split(_claimsMap, ",")
	for _, claim := range _claimsArr {
		claimsMap[claim] = this.TmpDataMap[iworkconst.STRING_PREFIX+claim].(string)
	}
	tokenString, err := chiperutil.CreateJWT(secretKey, claimsMap, expireSecond)
	if err != nil {
		panic(err)
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, map[string]interface{}{
		iworkconst.STRING_PREFIX + "tokenString": tokenString,
	})
}

func (this *CreateJWTNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "secretKey", "密钥"},
		2: {iworkconst.STRING_PREFIX + "claimsMap", "加密参数,多个参数逗号分隔"},
		3: {iworkconst.NUMBER_PREFIX + "expireSecond", "过期秒数"},
	}
	return this.BPIS1(paramMap)
}

func (this *CreateJWTNode) GetRuntimeParamInputSchema() *iworkmodels.ParamInputSchema {
	items := make([]*iworkmodels.ParamInputSchemaItem, 0)
	claims := param.GetStaticParamValueWithStep(this.WorkCache.Work.AppId, iworkconst.STRING_PREFIX+"claimsMap", this.WorkStep).(string)
	claimArr := strings.Split(claims, ",")
	for _, claim := range claimArr {
		items = append(items, &iworkmodels.ParamInputSchemaItem{ParamName: iworkconst.STRING_PREFIX + strings.TrimSpace(claim)})
	}
	return &iworkmodels.ParamInputSchema{ParamInputSchemaItems: items}
}

func (this *CreateJWTNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{iworkconst.STRING_PREFIX + "tokenString"})
}

type ParseJWTNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *ParseJWTNode) Execute(trackingId string) {
	tokenString := this.TmpDataMap[iworkconst.STRING_PREFIX+"tokenString"].(string)
	secretKey := this.TmpDataMap[iworkconst.STRING_PREFIX+"secretKey"].(string)
	handleExpiredError := this.TmpDataMap[iworkconst.BOOL_PREFIX+"handleExpiredError?"].(string)
	claimsMap, isExpired, err := chiperutil.ParseJWT2(secretKey, tokenString)
	if handleExpiredError == "`true`" || handleExpiredError == "true" {
		claimsMap[iworkconst.BOOL_PREFIX+"Expired"] = isExpired
	} else if err != nil {
		panic(err)
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, claimsMap)
}

func (this *ParseJWTNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	//paramMap := map[int][]string{
	//	1: {iworkconst.STRING_PREFIX + "tokenString", "密文"},
	//	2: {iworkconst.STRING_PREFIX + "secretKey", "密钥"},
	//	3: {iworkconst.STRING_PREFIX + "claimsMap", "解密参数,多个参数逗号分隔"},
	//}
	//return this.BPIS1(paramMap)

	paramMap := map[int]*node.ParamInputSchemaItemDefinition{
		1: {
			ParamName: iworkconst.STRING_PREFIX + "tokenString",
			ParamDesc: "密文",
		},
		2: {
			ParamName: iworkconst.STRING_PREFIX + "secretKey",
			ParamDesc: "密钥",
		},
		3: {
			ParamName: iworkconst.STRING_PREFIX + "claimsMap",
			ParamDesc: "解密参数,多个参数逗号分隔",
		},
		4: {
			ParamName:    iworkconst.BOOL_PREFIX + "handleExpiredError?",
			ParamDesc:    "处理 jwt 数据过期错误,true 返回过期标识,false 则抛出异常,默认 false",
			ParamChoices: []string{"`true`", "`false`"},
		},
	}
	return this.BPIS2(paramMap)
}

func (this *ParseJWTNode) GetRuntimeParamOutputSchema() *iworkmodels.ParamOutputSchema {
	pos := &iworkmodels.ParamOutputSchema{}
	_claimsMap := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"claimsMap", this.WorkStep).(string)
	claimArr := strings.Split(_claimsMap, ",")
	items := make([]iworkmodels.ParamOutputSchemaItem, 0)
	for _, claim := range claimArr {
		items = append(items, iworkmodels.ParamOutputSchemaItem{
			ParamName: strings.TrimSpace(claim),
		})
	}
	handleExpiredError := param.GetStaticParamValueWithStep(this.AppId, iworkconst.BOOL_PREFIX+"handleExpiredError?", this.WorkStep).(string)
	if handleExpiredError == "`true`" || handleExpiredError == "true" {
		items = append(items, iworkmodels.ParamOutputSchemaItem{
			ParamName: iworkconst.BOOL_PREFIX + "Expired",
		})
	}
	pos.ParamOutputSchemaItems = items
	return pos
}
