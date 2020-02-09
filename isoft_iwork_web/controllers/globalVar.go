package controllers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"isoft/isoft_utils/common/pageutil"
	"strings"
	"time"
)

func (this *WorkController) GlobalVarList() {
	app_id, _ := this.GetInt64("app_id", -1)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	condArr := map[string]string{"search": this.GetString("search")}
	globalVars, count, err := models.QueryGlobalVar(app_id, condArr, current_page, offset, orm.NewOrm())
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "globalVars": globalVars,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums()), "onuse": sysconfig.ENV_ONUSE}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) EditGlobalVar() {
	app_id, _ := this.GetInt64("app_id", -1)
	id, err := this.GetInt64("id", -1)
	globalVarName := this.GetString("globalVarName")
	globalVarValue01 := this.GetString("globalVarValue01")
	globalVarValue02 := this.GetString("globalVarValue02")
	globalVar := &models.GlobalVar{
		AppId:           app_id,
		Name:            globalVarName,
		EnvName:         globalVarValue01,
		Value:           globalVarValue02,
		Type:            1,
		CreatedBy:       "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedBy:   "SYSTEM",
		LastUpdatedTime: time.Now(),
	}
	if err == nil && id > 0 {
		globalVar.Id = id
	}
	_, err = models.InsertOrUpdateGlobalVar(globalVar, orm.NewOrm())
	if err == nil {
		flushMemoryGlobalVar(app_id)
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func (this *WorkController) QueryEvnNameList() {
	envNameList := beego.AppConfig.String("iwork.envname.list")
	strArray := strings.Split(envNameList, ",")
	resultMap := make(map[string]interface{})
	if len(strArray) > 0 {
		resultMap["status"] = "SUCCESS"
	} else {
		resultMap["status"] = "ERROR"
	}
	resultMap["EnvNameList"] = strArray
	this.Data["json"] = &resultMap
	this.ServeJSON()
}

func (this *WorkController) DeleteGlobalVarById() {
	app_id, _ := this.GetInt64("app_id", -1)
	id, _ := this.GetInt64("id")
	err := models.DeleteGlobalVarById(id)
	if err == nil {
		flushMemoryGlobalVar(app_id)
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}

func flushMemoryGlobalVar(app_id int64) {
	// 刷新全局变量
	memory.FlushMemoryGlobalVar(app_id)
	// 刷新资源链接,资源链接可以使用了全局变量
	memory.FlushMemoryResource(app_id)
}

func (this *WorkController) GetAllGlobalVars() {
	gvs := models.GetAllGlobalVars()
	this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "globalVars": gvs}
	this.ServeJSON()
}

func (this *WorkController) GlobalVarParserWarpper(app_id int64, value string) string {
	value = strings.TrimSpace(value)
	if strings.HasPrefix(value, "$Global.") {
		value = strings.TrimPrefix(value, "$Global.")
		value = strings.TrimSuffix(value, ";")
		gv, err := models.QueryGlobalVarByName(app_id, value, sysconfig.ENV_ONUSE)
		if err == nil {
			return gv.Value
		}
	}
	return value
}
