package controllers

import (
	"fmt"
	"isoft/isoft_iwork_web/core/iworkutil/sqlutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/stringutil"
	"strings"
)

func (this *WorkController) LoadQuickSqlMeta() {
	resource_id, _ := this.GetInt64("resource_id")
	var err error
	// 查询所有的数据库信息
	resource, _ := models.QueryResourceById(resource_id)
	tableColumnsMap := make(map[string]interface{}, 0)
	tableSqlMap := make(map[string]interface{}, 0)
	tableNames := sqlutil.GetAllTableNames(resource.ResourceDsn)
	for _, tableName := range tableNames {
		tableColumns := sqlutil.GetAllColumnNames(tableName, resource.ResourceDsn)
		tableColumnsMap[tableName] = tableColumns
		tableSqlMap[tableName] = []string{
			tableName,
			fmt.Sprintf(`select count(*) as count from %s`, tableName),
			fmt.Sprintf(`select count(*) as count from %s where 1 = 0`, tableName),
			strings.Join(tableColumns, ","),
			fmt.Sprintf(`select %s from %s where 1 = 0`, strings.Join(tableColumns, ","), tableName),
			fmt.Sprintf(`insert into %s(%s) values(%s)`, tableName, strings.Join(tableColumns, ","),
				strings.Join(stringutil.GetRepeatSlice("?", len(tableColumns)), ",")),
			fmt.Sprintf(`insert into %s(%s) values(%s)`, tableName, strings.Join(tableColumns, ","),
				strings.Join(stringutil.GetFormatSlice(tableColumns, func(s string) string {
					return ":" + s
				}), ",")),
			fmt.Sprintf(`update %s set %s where id = ?`, tableName,
				strings.Join(stringutil.GetFormatSlice(tableColumns, func(s string) string {
					return s + "=?"
				}), ",")),
			fmt.Sprintf(`update %s set %s where id = :id`, tableName,
				strings.Join(stringutil.GetFormatSlice(tableColumns, func(s string) string {
					return s + "=:" + s
				}), ",")),
		}
	}

	if err == nil {
		this.Data["json"] = &map[string]interface{}{
			"status":          "SUCCESS",
			"tableNames":      tableNames,
			"tableColumnsMap": tableColumnsMap,
			"tableSqlMap":     tableSqlMap,
		}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}
