package iworkutil

import (
	"encoding/base64"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_utils/common/stringutil"
	"strings"
)

// 用正则表达式匹配出相对变量值
func GetRelativeValueWithReg(s string) []string {
	return stringutil.GetNoRepeatSubStringWithRegexp(s, `\$[a-zA-Z0-9_]+(\.[a-zA-Z0-9_]+)*`)
}

// 用正则表达式匹配出相对变量值,多个值只返回第一个
func GetSingleRelativeValueWithReg(s string) string {
	values := GetRelativeValueWithReg(s)
	if len(values) > 0 {
		return values[0]
	}
	return s
}

func GetWorkSubNameFromParamValue(paramValue string) string {
	value := strings.TrimSpace(paramValue)
	value = strings.Replace(value, "$WORK.", "", -1)
	value = strings.Replace(value, ";", "", -1)
	value = strings.Replace(value, "\n", "", -1)
	value = strings.TrimSpace(value)
	return value
}

func GetWorkSubNameForWorkSubNode(paramInputSchema *iworkmodels.ParamInputSchema) string {
	for _, item := range paramInputSchema.ParamInputSchemaItems {
		if item.ParamName == iworkconst.STRING_PREFIX+"work_sub" && strings.HasPrefix(strings.TrimSpace(item.ParamValue), "$WORK.") {
			// 找到 work_sub 字段值
			return GetWorkSubNameFromParamValue(strings.TrimSpace(item.ParamValue))
		}
	}
	return ""
}

func EncodeToBase64StringSecurity(str string) string {
	if strings.HasPrefix(str, "base64:") {
		return str
	}
	return "base64:" + EncodeToBase64String([]byte(str))
}

func DecodeBase64StringSecurity(str string) string {
	if !strings.HasPrefix(str, "base64:") {
		return str
	}
	str = strings.TrimPrefix(str, "base64:")
	return string(DecodeBase64String(str))
}

func EncodeToBase64String(src []byte) string {
	return base64.StdEncoding.EncodeToString(src)
}

func DecodeBase64String(encodeString string) (bytes []byte) {
	if bytes, err := base64.StdEncoding.DecodeString(encodeString); err == nil {
		return bytes
	}
	return
}

func GetParamValueForEntity(app_id int64, paramValue string) string {
	//paramValue = strings.TrimSpace(paramValue)
	//paramValue = strings.Replace(paramValue, ";", "", -1)
	//if !strings.HasPrefix(paramValue, "$Entity.") {
	//	return paramValue
	//}
	//entity_name := strings.Replace(paramValue, "$Entity.", "", -1)
	//if entity, err := models.QueryEntityByEntityName(entity_name); err == nil {
	//	return entity.EntityFieldStr
	//}
	return ""
}

// 去除不合理的字符
func TrimParamValue(paramValue string) string {
	// 先进行初次的 trim
	paramValue = strings.TrimSpace(paramValue)
	// 去除前后的 \n
	paramValue = strings.TrimPrefix(paramValue, "\n")
	paramValue = strings.TrimSuffix(paramValue, "\n")
	// 再进行二次 trim
	paramValue = strings.TrimSpace(paramValue)
	return paramValue
}
