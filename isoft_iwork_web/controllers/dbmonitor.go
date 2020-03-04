package controllers

import (
	"isoft/isoft_iwork_web/models"
)

func (this *WorkController) GetDBMonitorMeta() {
	monitorMeta1, monitorMeta2, err := models.QueryDBMonitorMeta()
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "monitorMeta1": monitorMeta1, "monitorMeta2": monitorMeta2}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) LoadDBMonitorData() {
	app_id, _ := this.GetInt64("_app_id", -1)
	resource_name := this.GetString("resource_name")
	table_name := this.GetString("table_name")
	monitors, err := models.QueryDBMonitorData(app_id, resource_name, table_name)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "monitors": monitors}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}
