package controllers

import (
	"github.com/astaxie/beego/orm"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"time"
)

func (this *WorkController) SaveFilters() {
	app_id, _ := this.GetInt64("app_id", -1)
	filter_id, _ := this.GetInt64("filter_id", -1)
	filterWork, _ := models.QueryWorkById(filter_id, orm.NewOrm())
	filters := make([]*models.Filters, 0)
	// workNames 以逗号分隔
	workNames := this.GetString("workNames")
	complex_work_name := this.GetString("complexWorkName")
	filters = append(filters, &models.Filters{
		FilterWorkId:    filterWork.Id,
		FilterWorkName:  filterWork.WorkName,
		AppId:           app_id,
		WorkName:        workNames,
		ComplexWorkName: complex_work_name,
		CreatedBy:       "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedBy:   "SYSTEM",
		LastUpdatedTime: time.Now(),
	})
	_, err := models.InsertMultiFilters(filter_id, filters)
	if err == nil {
		flushMemoryFilter(app_id)
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func flushMemoryFilter(app_id int64) {
	memory.FlushMemoryFilter(app_id)
}
