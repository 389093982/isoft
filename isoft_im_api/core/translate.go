package core

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"isoft/isoft_utils/common/httputil"
	"net/http"
)

type YDResult struct {
	TraslateType    string              `json:"type"`
	ErrorCode       int64               `json:"errorCode"`
	ElapsedTime     int64               `json:"elapsedTime"`
	TranslateResult [][]TranslateResult `json:"translateResult"`
}

type TranslateResult struct {
	Src string `json:"src"`
	Tgt string `json:"tgt"`
}

// ZH_CN2EN 中文 > 英语
// EN2ZH_CN 英语 > 中文
func YDTranslate(s string) (rs string, err error) {
	var ydResult YDResult
	url := fmt.Sprintf("http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=%s", s)
	httputil.DoHttpRequestWithParserFunc(url, "GET", nil, nil, func(resp *http.Response) {
		if responsebytes, err1 := ioutil.ReadAll(resp.Body); err1 == nil {
			if err2 := json.Unmarshal(responsebytes, &ydResult); err2 != nil {
				rs, err = "", err2
			}
		} else {
			rs, err = "", err1
		}
	})
	if len(ydResult.TranslateResult) > 0 {
		rs, err = ydResult.TranslateResult[0][0].Tgt, nil
	}
	return
}
