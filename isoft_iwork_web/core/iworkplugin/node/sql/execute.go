package sql

import (
	"errors"
	"fmt"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkutil/datatypeutil"
	"isoft/isoft_iwork_web/core/iworkutil/sqlutil"
	"isoft/isoft_iwork_web/models"
	"strings"
)

type SQLExecuteNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *SQLExecuteNode) Execute(trackingId string) {
	sql, namings := parseNamingSql(this.TmpDataMap[iworkconst.STRING_PREFIX+"sql"].(string))
	dataSourceName := this.TmpDataMap[iworkconst.STRING_PREFIX+"db_conn"].(string)

	// insert 语句且有批量操作时整改 sql 语句
	sql = this.modifySqlInsertWithBatch(this.TmpDataMap, sql)
	// sql_binding 参数获取
	sql_binding := getSqlBinding(this.TmpDataMap, namings)

	txm := this.DataStore.TxManger.(*node.TxManager)
	txm.FirstBegin(dataSourceName)

	lastInsertId, affected := sqlutil.ExecuteWithTx(sql, sql_binding, txm.Tx)
	// 将数据数据存储到数据中心
	// 存储 affected
	paramMap := map[string]interface{}{iworkconst.NUMBER_PREFIX + "affected": affected, iworkconst.NUMBER_PREFIX + "lastInsertId": lastInsertId}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, paramMap)
	this.checkPanicNoAffectedMsg(affected)
}

// 当影响条数为 0 时,是否报出异常信息
func (this *SQLExecuteNode) checkPanicNoAffectedMsg(affected int64) {
	if panicNoAffected := this.TmpDataMap[iworkconst.STRING_PREFIX+"panic_no_affected?"]; panicNoAffected != nil {
		panicNoAffectedMsg := panicNoAffected.(string)
		if affected == 0 && panicNoAffectedMsg != "" {
			panic(&interfaces.InsensitiveError{Error: errors.New(panicNoAffectedMsg)})
		}
	}
}

func (this *SQLExecuteNode) modifySqlInsertWithBatch(tmpDataMap map[string]interface{}, sql string) string {
	if batch_data := tmpDataMap[iworkconst.FOREACH_PREFIX+"batch_data?"]; batch_data != nil {
		values := sql[strings.Index(sql, "BATCH[")+len("BATCH[") : strings.Index(sql, "]")]
		replaceValues := strings.Repeat(values+",", datatypeutil.InterfaceConvertToInt(batch_data))
		sql = strings.ReplaceAll(sql, fmt.Sprintf("BATCH[%s]", values), replaceValues[:len(replaceValues)-1])
	}
	return sql
}

func (this *SQLExecuteNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.FOREACH_PREFIX + "batch_data?", "仅供批量插入数据时使用"},
		2: {iworkconst.STRING_PREFIX + "sql", "执行sql语句"},
		3: {iworkconst.MULTI_PREFIX + "sql_binding?", "sql绑定数据,个数必须和当前执行sql语句中的占位符参数个数相同", "repeatable__" + iworkconst.FOREACH_PREFIX + "batch_data?"},
		4: {iworkconst.STRING_PREFIX + "panic_no_affected?", "执行 sql 影响条数为 0 时,抛出的异常信息,为空时不抛出异常!"},
		5: {iworkconst.STRING_PREFIX + "db_conn", "数据库连接信息,需要使用 $RESOURCE 全局参数"},
	}
	return this.BPIS1(paramMap)
}

func (this *SQLExecuteNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{iworkconst.NUMBER_PREFIX + "affected", iworkconst.NUMBER_PREFIX + "lastInsertId"})
}

func (this *SQLExecuteNode) ValidateCustom(app_id int64) (checkResult []string) {
	validateAndGetDataStoreName(app_id, this.WorkStep)
	return []string{}
}

func (this *SQLExecuteNode) BuildParamNamingRelation(items []*iworkmodels.ParamInputSchemaItem) {
	var namingStr string
	for index, item := range items {
		if item.ParamName == iworkconst.STRING_PREFIX+"sql" {
			_, namings := parseNamingSql(item.ParamValue)
			namingStr = strings.Join(namings, ",")
		}
		if item.ParamName == iworkconst.MULTI_PREFIX+"sql_binding?" {
			items[index].ParamNamings = namingStr
		}
	}
}
