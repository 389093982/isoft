package controllers

import (
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/pageutil"
	"time"
)

func (this *WorkController) ModuleList() {
	app_id, _ := this.GetInt64("app_id", -1)
	condArr := make(map[string]string)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	if search := this.GetString("search"); search != "" {
		condArr["search"] = search
	}
	modules, count, err := models.QueryPageModuleList(app_id, condArr, current_page, offset, orm.NewOrm())
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "modules": modules,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums())}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) EditModule() {
	var module models.Module
	module_id, err := this.GetInt64("module_id", -1)
	if err == nil && module_id > 0 {
		module.Id = module_id
	}
	module.AppId, _ = this.GetInt64("app_id", -1)
	module.ModuleName = this.GetString("module_name")
	module.ModuleDesc = this.GetString("module_desc")
	module.CreatedBy = "SYSTEM"
	module.CreatedTime = time.Now()
	module.LastUpdatedBy = "SYSTEM"
	module.LastUpdatedTime = time.Now()
	_, err = models.InsertOrUpdateModule(&module, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) DeleteModuleById() {
	id, _ := this.GetInt64("id")
	_, err := models.DeleteModuleById(id, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) GetAllModules() {
	app_id, _ := this.GetInt64("app_id", -1)
	moudles, err := models.QueryAllModules(app_id)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "moudles": moudles}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}
