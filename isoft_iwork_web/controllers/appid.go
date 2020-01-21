package controllers

import (
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_utils/common/pageutil"
	"time"
)

func (this *WorkController) EditAppid() {
	var appid models.AppId
	appid.Id, _ = this.GetInt64("id", 0)
	appid.AppName = this.GetString("app_name")
	appid.AppDesc = this.GetString("app_desc")
	appid.CreatedBy = "SYSTEM"
	appid.CreatedTime = time.Now()
	appid.LastUpdatedBy = "SYSTEM"
	appid.LastUpdatedTime = time.Now()
	if _, err := models.InsertOrUpdateAppId(&appid); err == nil {
		memory.ReloadAppId(appid.Id)
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) QueryPageAppIdList() {
	condArr := make(map[string]string)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	if search := this.GetString("search"); search != "" {
		condArr["search"] = search
	}
	appIds, count, err := models.QueryPageAppIdList(condArr, current_page, offset)
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "appids": appIds,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums())}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) DeleteAppid() {
	appid, _ := this.GetInt64("id", -1)
	err := models.DeleteAppById(appid)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}
