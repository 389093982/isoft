package http

import (
	"fmt"
	"github.com/pkg/errors"
	"io/ioutil"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/httputil"
	"net/http"
	"strings"
)

type HttpRequestNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *HttpRequestNode) Execute(trackingId string) {
	// 参数准备
	var request_url, request_method string
	if _request_url, ok := this.TmpDataMap[iworkconst.STRING_PREFIX+"request_url"].(string); ok {
		request_url = _request_url
	}
	if _request_method, ok := this.TmpDataMap[iworkconst.STRING_PREFIX+"request_method?"].(string); ok {
		request_method = _request_method
	}
	paramMap := fillParamMapData(this.TmpDataMap, iworkconst.MULTI_PREFIX+"request_params?")
	headerMap := fillParamMapData(this.TmpDataMap, iworkconst.MULTI_PREFIX+"request_headers?")

	dataMap := make(map[string]interface{}, 0)

	err := httputil.DoHttpRequestWithParserFunc(request_url, request_method, paramMap, headerMap, func(resp *http.Response) {
		dataMap[iworkconst.NUMBER_PREFIX+"StatusCode"] = resp.StatusCode
		dataMap[iworkconst.STRING_PREFIX+"ContentType"] = resp.Header.Get("content-type")
		responsebytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			panic(err)
		} else {
			dataMap[iworkconst.STRING_PREFIX+"response_data"] = string(responsebytes)
			dataMap[iworkconst.BYTE_ARRAY_PREFIX+"response_data"] = responsebytes
			dataMap[iworkconst.BASE64STRING_PREFIX+"response_data"] = iworkutil.EncodeToBase64String(responsebytes)
		}
	})
	if err != nil {
		panic(errors.Wrapf(err, "[HttpRequestNode-%s]", request_url))
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, dataMap,
		iworkconst.STRING_PREFIX+"response_data", iworkconst.BYTE_ARRAY_PREFIX+"response_data", iworkconst.BASE64STRING_PREFIX+"response_data")
}

func (this *HttpRequestNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "request_url", "请求资源的url地址"},
		2: {iworkconst.STRING_PREFIX + "request_method?", "可选参数,请求方式,默认是GET请求,支持GET、POST"},
		3: {iworkconst.MULTI_PREFIX + "request_params?", "可选参数,请求参数,格式参考：key=value,多个参数用分隔符 & 拼接"},
		4: {iworkconst.MULTI_PREFIX + "request_headers?", "可选参数,请求头参数,格式参考：key=value,多个参数用分隔符 & 拼接"},
	}
	return this.BPIS1(paramMap)
}

func (this *HttpRequestNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{
		iworkconst.STRING_PREFIX + "response_data",
		iworkconst.BYTE_ARRAY_PREFIX + "response_data",
		iworkconst.BASE64STRING_PREFIX + "response_data",
		iworkconst.NUMBER_PREFIX + "StatusCode",
		iworkconst.STRING_PREFIX + "ContentType"})
}

func fillParamMapData(tmpDataMap map[string]interface{}, paramName string) map[string]interface{} {
	paramMap := make(map[string]interface{})
	if paramValue, ok := tmpDataMap[paramName].(string); ok {
		// 根据分隔符 & 分割得到多个参数
		for _, keyValue := range strings.Split(paramValue, "&") {
			// 再根据分隔符 = 进行分割
			if key, value := checkParameter(keyValue); strings.TrimSpace(key) != "" {
				paramMap[strings.TrimSpace(key)] = strings.TrimSpace(value)
			}
		}
	}
	return paramMap
}

func checkParameter(s string) (paramName, paramValue string) {
	s = strings.TrimSpace(s)
	if !strings.Contains(s, "=") {
		panic(errors.New(fmt.Sprint("invalid parameter for %s", s)))
	}
	index := strings.Index(s, "=")
	paramName = strings.TrimSpace(s[:index])
	paramValue = strings.TrimSpace(s[index+1:])
	return
}
