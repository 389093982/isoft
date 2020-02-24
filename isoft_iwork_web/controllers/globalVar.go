package controllers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"strings"
	"time"
)

func (this *WorkController) GlobalVarList() {
	app_id, _ := this.GetInt64("app_id", -1)
	condArr := map[string]string{"search": this.GetString("search")}
	globalVars, err := models.QueryGlobalVar(app_id, condArr, orm.NewOrm())
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "globalVars": globalVars, "onuse": sysconfig.ENV_ONUSE}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) EditGlobalVar() {
	app_id, _ := this.GetInt64("app_id", -1)
	id, err := this.GetInt64("id", -1)
	name := this.GetString("name")
	env_name := this.GetString("env_name")
	value := strings.TrimSpace(this.GetString("value"))
	encrypt_flag, _ := this.GetBool("encrypt_flag", false)
	if encrypt_flag {
		value = iworkutil.EncodeToBase64StringSecurity(value)
	} else {
		value = iworkutil.DecodeBase64StringSecurity(value)
	}
	globalVar := &models.GlobalVar{
		AppId:           app_id,
		Name:            name,
		EnvName:         env_name,
		EncryptFalg:     encrypt_flag,
		Value:           value,
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
			return iworkutil.DecodeBase64StringSecurity(gv.Value)
		}
	}
	return iworkutil.DecodeBase64StringSecurity(value)
}
