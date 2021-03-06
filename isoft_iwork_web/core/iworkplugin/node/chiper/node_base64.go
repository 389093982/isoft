package chiper

import (
	"encoding/base64"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
)

type Base64EncodeNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *Base64EncodeNode) Execute(trackingId string) {
	input := this.TmpDataMap[iworkconst.STRING_PREFIX+"input"].(string)
	encodeString := base64.StdEncoding.EncodeToString([]byte(input))
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, map[string]interface{}{iworkconst.STRING_PREFIX + "encode_data": encodeString})
}

func (this *Base64EncodeNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "input", "待编码的输入字符串"},
	}
	return this.BPIS1(paramMap)
}

func (this *Base64EncodeNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{iworkconst.STRING_PREFIX + "encode_data"})
}

type Base64DecodeNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *Base64DecodeNode) Execute(trackingId string) {
	input := this.TmpDataMap[iworkconst.STRING_PREFIX+"input"].(string)
	bytes, err := base64.StdEncoding.DecodeString(input)
	if err == nil {
		this.DataStore.CacheDatas(this.WorkStep.WorkStepName, map[string]interface{}{
			iworkconst.STRING_PREFIX + "decode_data":     string(bytes),
			iworkconst.BYTE_ARRAY_PREFIX + "decode_data": bytes,
		}, iworkconst.BYTE_ARRAY_PREFIX+"decode_data")
	} else {
		panic(err)
	}
}

func (this *Base64DecodeNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "input", "待解码的输入字符串"},
	}
	return this.BPIS1(paramMap)
}

func (this *Base64DecodeNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{iworkconst.STRING_PREFIX + "decode_data", iworkconst.BYTE_ARRAY_PREFIX + "decode_data"})
}
