package controllers

import (
	"encoding/json"
	"encoding/xml"
	"fmt"
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/core/iworkutil/sqlutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/jsonutil"
	"isoft/isoft_utils/common/pageutil"
	"isoft/isoft_utils/common/xmlutil"
	"strings"
	"time"
)

func (this *WorkController) EditAuditTask() {
	app_id, _ := this.GetInt64("app_id", -1)
	id, _ := this.GetInt64("id", 0)
	taskName := this.GetString("task_name")
	taskDesc := this.GetString("task_desc")
	var task *models.AuditTask
	if id > 0 {
		if task0, err := models.QueryAuditTaskId(id, orm.NewOrm()); err == nil {
			task = &task0
		}
	}
	if task == nil {
		task = new(models.AuditTask)
		task.AppId = app_id
		task.CreatedBy = "SYSTEM"
		task.CreatedTime = time.Now()
	}
	task.TaskName = taskName
	task.TaskDesc = taskDesc
	task.LastUpdatedBy = "SYSTEM"
	task.LastUpdatedTime = time.Now()
	_, err := models.InsertOrUpdateAuditTask(task, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) QueryPageAuditTask() {
	app_id, _ := this.GetInt64("app_id", -1)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	tasks, count, err := models.QueryPageAuditTask(app_id, current_page, offset, orm.NewOrm())
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "tasks": tasks,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums())}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) GetAuditHandleData() {
	app_id, _ := this.GetInt64("app_id", -1)
	taskName := this.GetString("task_name")
	current_page, _ := this.GetInt64("current_page")
	offset, _ := this.GetInt64("offset")
	case_name := this.GetString("case_name")
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	resource, _ := models.QueryResourceByName(app_id, taskDetail.ResourceName)
	resource.ResourceDsn = this.GlobalVarParserWarpper(app_id, resource.ResourceDsn)

	// 智能审核系统第三步获取审核数据,根据不同场景查询不同数据,默认查询全部数据
	filterSql := taskDetail.QueryCases[0].QuerySql
	for _, queryCase := range taskDetail.QueryCases {
		if queryCase.CaseName == case_name && strings.TrimSpace(queryCase.QuerySql) != "" {
			filterSql = queryCase.QuerySql
		}
	}
	_, rowDatas0, err1 := sqlutil.QuerySql(strings.Replace(filterSql, "*", "count(*) as totalcount", -1),
		[]interface{}{}, resource.ResourceDsn)
	_, rowDatas, err2 := sqlutil.QuerySql(fmt.Sprintf(`%s limit ?,?`, filterSql), []interface{}{(current_page - 1) * offset, offset}, resource.ResourceDsn)
	if err1 == nil && err2 == nil {
		this.Data["json"] = &map[string]interface{}{
			"status":     "SUCCESS",
			"rowDatas":   rowDatas,
			"totalcount": rowDatas0[0]["totalcount"],
			"colNames":   taskDetail.ColNames,
		}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": errorutil.GetFirstError(err1, err2).Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) EditAuditTaskTarget() {
	taskName := this.GetString("task_name")
	update_cases := this.GetString("update_cases")
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	json.Unmarshal([]byte(update_cases), &taskDetail.UpdateCases)
	task.TaskDetail = xmlutil.RenderToString(taskDetail)
	// 配置入库
	_, err := models.InsertOrUpdateAuditTask(&task, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) EditAuditTaskSource() {
	app_id, _ := this.GetInt64("app_id", -1)
	taskName := this.GetString("task_name")
	resourceName := this.GetString("resource_name")
	query_cases := this.GetString("query_cases")
	var queryCases []models.QueryCase
	json.Unmarshal([]byte(query_cases), &queryCases)
	resource, _ := models.QueryResourceByName(app_id, resourceName)
	resource.ResourceDsn = this.GlobalVarParserWarpper(app_id, resource.ResourceDsn)
	colNames := sqlutil.GetMetaDatas(queryCases[0].QuerySql, resource.ResourceDsn)
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	taskDetail.QueryCases = queryCases
	taskDetail.ResourceName = resourceName
	taskDetail.ColNames = jsonutil.RenderToJson(colNames)
	task.TaskDetail = xmlutil.RenderToString(taskDetail)
	// 配置入库
	_, err := models.InsertOrUpdateAuditTask(&task, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) QueryTaskDetail() {
	taskName := this.GetString("task_name")
	task, err := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	if err == nil {
		var taskDetail models.TaskDetail
		xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "task": task, "taskDetail": taskDetail}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) ExecuteAuditTask() {
	app_id, _ := this.GetInt64("app_id", -1)
	taskName := this.GetString("task_name")
	sql_str := this.GetString("sql_str")
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	resource, _ := models.QueryResourceByName(app_id, taskDetail.ResourceName)
	resource.ResourceDsn = this.GlobalVarParserWarpper(app_id, resource.ResourceDsn)
	_, _, err := sqlutil.ExecuteSql(sql_str, nil, resource.ResourceDsn)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) DeleteAuditTask() {
	taskName := this.GetString("task_name")
	err := models.DeleteAuditTask(taskName, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}
