package stringutil

import "strings"

// 将 v 转成非空字符串,转换失败时使用默认值
func GetString(v interface{}, def string, trim ...bool) string {
	if _, ok := v.(string); !ok {
		return def
	}
	s := v.(string)
	if len(trim) > 0 && trim[0] == true {
		s = strings.TrimSpace(s)
	}
	if s != "" {
		return s
	}
	return def
}

// 根据分隔符进行分割得到数组,同时保留分隔符 sep
func SplitWithSepRetain(s, sep string) []string {
	parts := strings.Split(s, sep)
	arr := make([]string, 0)
	for index, part := range parts {
		arr = append(arr, part)
		if index < len(parts)-1 {
			arr = append(arr, sep)
		}
	}
	return arr
}
