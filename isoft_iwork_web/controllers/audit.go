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
	id, err := this.GetInt64("id", -1)
	taskName := this.GetString("task_name")
	taskDesc := this.GetString("task_desc")
	task := &models.AuditTask{
		AppId:           app_id,
		TaskName:        taskName,
		TaskDesc:        taskDesc,
		CreatedBy:       "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedBy:   "SYSTEM",
		LastUpdatedTime: time.Now(),
	}
	if err == nil && id > 0 {
		task.Id = id
	}
	_, err = models.InsertOrUpdateAuditTask(task, orm.NewOrm())
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
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	resource, _ := models.QueryResourceByName(app_id, taskDetail.ResourceName)
	resource.ResourceDsn = this.GlobalVarParserWarpper(app_id, resource.ResourceDsn)
	_, rowDatas0, err1 := sqlutil.QuerySql(strings.Replace(taskDetail.QuerySql, "*", "count(*) as totalcount", -1),
		[]interface{}{}, resource.ResourceDsn)
	_, rowDatas, err2 := sqlutil.QuerySql(fmt.Sprintf(`%s limit ?,?`, taskDetail.QuerySql), []interface{}{(current_page - 1) * offset, offset}, resource.ResourceDsn)
	if err1 == nil && err2 == nil && len(rowDatas) > 0 {
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
	querySql := this.GetString("query_sql")
	resource, _ := models.QueryResourceByName(app_id, resourceName)
	colNames := sqlutil.GetMetaDatas(querySql, resource.ResourceDsn)
	task, _ := models.QueryAuditTaskByTaskName(taskName, orm.NewOrm())
	var taskDetail models.TaskDetail
	xml.Unmarshal([]byte(task.TaskDetail), &taskDetail)
	taskDetail.ResourceName = resourceName
	taskDetail.QuerySql = querySql
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
	_, affected, err := sqlutil.ExecuteSql(sql_str, nil, resource.ResourceDsn)
	if err == nil && affected > 0 {
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
