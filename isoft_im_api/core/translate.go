package core

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
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

func GetTranslateType(s string) string {
	if s == "type=ZH_CN2EN" {
		return "ZH_CN2EN"
	} else if s == "type=EN2ZH_CN" {
		return "EN2ZH_CN"
	} else {
		return "AUTO"
	}
}

// ZH_CN2EN 中文 > 英语
// EN2ZH_CN 英语 > 中文
func YDTranslate(s string, translateType string) (rs string, err error) {
	var ydResult YDResult
	url := "http://fanyi.youdao.com/translate"
	paramMap := map[string]interface{}{
		"doctype": "json",
		"type":    translateType,
		"i":       s,
	}
	resp, _err := http.Post(url, "application/x-www-form-urlencoded", GetParamReader(paramMap))
	if err != nil {
		rs, err = "", _err
	}
	if responsebytes, err1 := ioutil.ReadAll(resp.Body); err1 == nil {
		if err2 := json.Unmarshal(responsebytes, &ydResult); err2 != nil {
			rs, err = "", err2
		}
	} else {
		rs, err = "", err1
	}
	if len(ydResult.TranslateResult) > 0 {
		for _, item := range ydResult.TranslateResult[0] {
			rs += item.Tgt
		}
	}
	return
}

func GetParamReader(paramMap map[string]interface{}) *strings.Reader {
	if paramMap == nil || len(paramMap) == 0 {
		return strings.NewReader("")
	}
	s := make([]string, 0)
	for k, v := range paramMap {
		s = append(s, fmt.Sprintf("%s=%v", k, v))
	}
	paramStr := strings.Join(s, "&")
	return strings.NewReader(paramStr)
}
