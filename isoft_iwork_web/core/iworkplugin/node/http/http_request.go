package http

import (
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/param"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/httputil"
	"net/http"
	"strings"
)

type HttpRequestParserNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *HttpRequestParserNode) Execute(trackingId string) {
	request := this.Dispatcher.TmpDataMap[iworkconst.HTTP_REQUEST_OBJECT].(*http.Request)
	headers := param.GetStaticParamValueWithStep(this.WorkCache.Work.AppId, iworkconst.STRING_PREFIX+"headers?", this.WorkStep).(string)
	headerSlice := strings.Split(headers, ",")
	paramMap := make(map[string]interface{}, 0)
	for _, header := range headerSlice {
		headerVal := request.Header.Get(header)
		paramMap["header_"+header] = headerVal
	}
	cookies := param.GetStaticParamValueWithStep(this.WorkCache.Work.AppId, iworkconst.STRING_PREFIX+"cookies?", this.WorkStep).(string)
	cookieSlice := strings.Split(cookies, ",")
	for _, cookie := range cookieSlice {
		if cookieVal, err := request.Cookie(cookie); err == nil {
			paramMap["cookie_"+cookie] = cookieVal.Value
		}
	}
	paramMap["ip"] = httputil.GetClientIP(request)
	// 将数据数据存储到数据中心
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, paramMap)
}

func (this *HttpRequestParserNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "headers?", "解析的请求头参数,多个参数使用逗号分隔"},
		2: {iworkconst.STRING_PREFIX + "cookies?", "解析的 cookies 参数,多个参数使用逗号分隔"},
	}
	return this.BPIS1(paramMap)
}

func (this *HttpRequestParserNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{"ip"})
}

func (this *HttpRequestParserNode) GetRuntimeParamOutputSchema() *iworkmodels.ParamOutputSchema {
	var parseToItems = func(paramStr, paramPrefix string) []iworkmodels.ParamOutputSchemaItem {
		items := make([]iworkmodels.ParamOutputSchemaItem, 0)
		if paramStr != "" {
			paramSlice := strings.Split(paramStr, ",")
			for _, param := range paramSlice {
				items = append(items, iworkmodels.ParamOutputSchemaItem{
					ParamName: paramPrefix + param,
				})
			}
		}
		return items
	}

	pos := &iworkmodels.ParamOutputSchema{}
	headers := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"headers?", this.WorkStep).(string)
	cookies := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"cookies?", this.WorkStep).(string)
	pos.ParamOutputSchemaItems = append(pos.ParamOutputSchemaItems, parseToItems(headers, "header_")...)
	pos.ParamOutputSchemaItems = append(pos.ParamOutputSchemaItems, parseToItems(cookies, "cookie_")...)
	return pos
}

type HttpWirterNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *HttpWirterNode) Execute(trackingId string) {
	request := this.Dispatcher.TmpDataMap[iworkconst.HTTP_REQUEST_OBJECT].(*http.Request)
	response := this.Dispatcher.TmpDataMap[iworkconst.HTTP_REQUEST_OBJECT].(*http.Response)
	request_header := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"request_header?", this.WorkStep).(string)
	response_header := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"response_header?", this.WorkStep).(string)
	if request_header != "" {
		paramSlice := strings.Split(request_header, ",")
		for _, param := range paramSlice {
			request.Header.Add(strings.TrimPrefix(param, "request_"), this.Dispatcher.TmpDataMap[param].(string))
		}
	}
	if response_header != "" {
		paramSlice := strings.Split(response_header, ",")
		for _, param := range paramSlice {
			response.Header.Add(strings.TrimPrefix(param, "request_"), this.Dispatcher.TmpDataMap[param].(string))
		}
	}
}

func (this *HttpWirterNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int]*node.ParamInputSchemaItemDefinition{
		1: {
			ParamName: iworkconst.STRING_PREFIX + "request_header?",
			ParamDesc: "设置 http 请求头,多个参数使用逗号分割",
		},
		2: {
			ParamName: iworkconst.STRING_PREFIX + "response_header?",
			ParamDesc: "设置 http 响应头,多个参数使用逗号分割",
		},
	}
	return this.BPIS2(paramMap)
}

func (this *HttpWirterNode) GetRuntimeParamInputSchema() *iworkmodels.ParamInputSchema {
	var parseToItems = func(paramStr, paramPrefix string) []*iworkmodels.ParamInputSchemaItem {
		items := make([]*iworkmodels.ParamInputSchemaItem, 0)
		if paramStr != "" {
			paramSlice := strings.Split(paramStr, ",")
			for _, param := range paramSlice {
				items = append(items, &iworkmodels.ParamInputSchemaItem{
					ParamName: paramPrefix + param,
				})
			}
		}
		return items
	}

	pis := &iworkmodels.ParamInputSchema{}
	request_header := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"request_header?", this.WorkStep).(string)
	response_header := param.GetStaticParamValueWithStep(this.AppId, iworkconst.STRING_PREFIX+"response_header?", this.WorkStep).(string)
	if request_header != "" {
		pis.ParamInputSchemaItems = append(pis.ParamInputSchemaItems, parseToItems(request_header, "request_")...)
	}
	if response_header != "" {
		pis.ParamInputSchemaItems = append(pis.ParamInputSchemaItems, parseToItems(response_header, "response_")...)
	}
	return pis
}
