package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

// 审核任务表
type AuditTask struct {
	Id              int64     `json:"id"`
	AppId           int64     `json:"app_id"`
	TaskName        string    `json:"task_name"`
	TaskDesc        string    `json:"task_desc" orm:"type(text)"`
	TaskDetail      string    `json:"task_detail" orm:"type(text)"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

type TaskDetail struct {
	ResourceName string       `json:"resource_name"`
	QueryCases   []QueryCase  `json:"query_cases"`
	ColNames     string       `json:"col_names"`
	UpdateCases  []UpdateCase `json:"update_cases"`
}

type QueryCase struct {
	CaseName  string `json:"case_name"`
	QuerySql  string `json:"query_sql"`
	QueryDesc string `json:"query_desc"`
}

type UpdateCase struct {
	CaseName   string `json:"case_name"`
	UpdateSql  string `json:"update_sql"`
	UpdateDesc string `json:"update_desc"`
}

func InsertOrUpdateAuditTask(task *AuditTask, o orm.Ormer) (id int64, err error) {
	if task.Id > 0 {
		id, err = o.Update(task)
	} else {
		id, err = o.Insert(task)
	}
	return
}

func QueryAuditTaskByTaskName(task_name string, o orm.Ormer) (task AuditTask, err error) {
	err = o.QueryTable("audit_task").Filter("task_name", task_name).One(&task)
	return
}

func QueryPageAuditTask(app_id int64, page int, offset int, o orm.Ormer) (tasks []AuditTask, counts int64, err error) {
	qs := o.QueryTable("audit_task")
	counts, _ = qs.Count()
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	qs = qs.OrderBy("-last_updated_time").Limit(offset, (page-1)*offset)
	qs.All(&tasks)
	return
}

func QueryAllAuditTasks(o orm.Ormer) (tasks []AuditTask, err error) {
	_, err = o.QueryTable("audit_task").All(&tasks)
	return
}

func DeleteAuditTask(task_name string, o orm.Ormer) (err error) {
	_, err = o.QueryTable("audit_task").Filter("task_name", task_name).Delete()
	return
}
