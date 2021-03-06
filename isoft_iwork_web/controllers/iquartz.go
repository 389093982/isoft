package controllers

import (
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_utils/common/pageutil"
	"time"
)

func (this *WorkController) ToggleQuartzStatus() {
	app_id, _ := this.GetInt64("app_id", -1)
	defer memory.FlushAppId(app_id)
	task_name := this.GetString("task_name")
	operate := this.GetString("operate")
	var err error
	if operate == "delete" {
		err = models.DeleteCronMetaByTaskName(task_name, orm.NewOrm())
	} else {
		meta, _ := models.QueryCronMetaByName(task_name)
		if operate == "start" {
			meta.Enable = true
		} else if operate == "stop" {
			meta.Enable = false
		}
		_, err = models.InsertOrUpdateCronMeta(&meta, orm.NewOrm())
	}
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) EditQuartz() {
	app_id, _ := this.GetInt64("app_id", -1)
	defer memory.FlushAppId(app_id)
	var meta models.CronMeta
	meta.AppId = app_id
	meta.Id, _ = this.GetInt64("id", 0)
	meta.TaskName = this.Input().Get("task_name")
	meta.TaskType = this.Input().Get("task_type")
	meta.CronStr = this.Input().Get("cron_str")
	meta.Enable = false
	meta.CreatedBy = "SYSTEM"
	meta.CreatedTime = time.Now()
	meta.LastUpdatedBy = "SYSTEM"
	meta.LastUpdatedTime = time.Now()
	if _, err := models.InsertOrUpdateCronMeta(&meta, orm.NewOrm()); err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) FilterPageQuartz() {
	app_id, _ := this.GetInt64("app_id", -1)
	condArr := make(map[string]string)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	if search := this.GetString("search"); search != "" {
		condArr["search"] = search
	}
	quartzs, count, err := models.QueryCronMeta(app_id, condArr, current_page, offset)
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "quartzs": quartzs,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums())}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}
