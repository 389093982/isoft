package sqlutil

import (
	"fmt"
	"isoft/isoft_iwork_web/core/iworkutil/stringutil"
	stringutil2 "isoft/isoft_utils/common/stringutil"
	"strings"
	"regexp"
)

func GetAllTableNames(dataSourceName string) []string {
	_, rowDatas := Query("show tables;", []interface{}{}, dataSourceName)
	tableNames := make([]string, 0)
	for _, rowData := range rowDatas {
		for _, tableName := range rowData {
			tableNames = append(tableNames, tableName.(string))
		}
	}
	return tableNames
}

func GetAllColumnNames(tableName, dataSourceName string) []string {
	return GetMetaDatas(fmt.Sprintf("select * from %s where 1=0", tableName), dataSourceName)
}

func ParseSpecialCharsetAnd(sqlStr string) string {
	sqlStr, _ = stringutil2.ReplaceAllString(sqlStr, `__AND__(\s|__AND__)+__AND__`, "__AND__") // 将多个 __AND__ 替换成一个
	parts := stringutil.SplitWithSepRetain(sqlStr, "__AND__")
	for index, part := range parts {
		if part == "__AND__" {
			if concatableN(parts[index-1], parts[index+1]) {
				parts[index] = " and "
			} else {
				parts[index] = " "
			}
		}
	}
	return strings.Join(parts, "")
}

func concatableN(part1, part2 string) bool {
	return concatable(part1) && concatable(part2)
}

func concatable(part string) bool {
	part = strings.ToLower(strings.TrimSpace(part))
	// .匹配任意一个字符 ，*匹配零个或多个 ，优先匹配更多(贪婪)
	orderMatch, _ := regexp.MatchString(`^(order)\s+.*`, part)
	whereMatch, _ := regexp.MatchString(`^(where)\s+.*`, part)
	whereMatch2, _ := regexp.MatchString(`.*\s+(where)`, part)
	if orderMatch || whereMatch || whereMatch2{
		return false
	}
	if part == ";" || part == "" {
		return false
	}
	return true
}
