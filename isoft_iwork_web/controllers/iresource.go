package controllers

import (
	"github.com/astaxie/beego/utils/pagination"
	"isoft/isoft_iwork_web/core/iworkpool"
	"isoft/isoft_iwork_web/core/iworkutil/sftputil"
	"isoft/isoft_iwork_web/core/iworkutil/sshutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_utils/common/pageutil"
	"time"
)

func (this *WorkController) EditResource() {
	app_id, _ := this.GetInt64("app_id", -1)
	// 刷新内存
	defer memory.FlushAppId(app_id)
	var resource models.Resource
	resource.Id, _ = this.GetInt64("resource_id", -1)
	resource.AppId, _ = this.GetInt64("app_id", -1)
	resource.ResourceName = this.GetString("resource_name")
	resource.ResourceType = this.GetString("resource_type")
	resource.ResourceUrl = this.GetString("resource_url")
	resource.ResourceDsn = this.GetString("resource_dsn")
	resource.ResourceUsername = this.GetString("resource_username")
	resource.ResourcePassword = this.GetString("resource_password")
	resource.CreatedBy = "SYSTEM"
	resource.CreatedTime = time.Now()
	resource.LastUpdatedBy = "SYSTEM"
	resource.LastUpdatedTime = time.Now()
	if _, err := models.InsertOrUpdateResource(&resource); err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) GetResourceById() {
	id, _ := this.GetInt64("id", -1)
	resource, err := models.QueryResourceById(id)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "resource": resource}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) GetAllResource() {
	app_id, _ := this.GetInt64("app_id", -1)
	resource_type := this.GetString("resource_type")
	resources := models.QueryAllResource(app_id, resource_type)
	this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "resources": resources}
	this.ServeJSON()
}

func (this *WorkController) FilterPageResource() {
	condArr := make(map[string]string)
	app_id, _ := this.GetInt64("app_id", -1)
	offset, _ := this.GetInt("offset", 10)            // 每页记录数
	current_page, _ := this.GetInt("current_page", 1) // 当前页
	if search := this.GetString("search"); search != "" {
		condArr["search"] = search
	}
	resources, count, err := models.QueryResource(app_id, condArr, current_page, offset)
	paginator := pagination.SetPaginator(this.Ctx, offset, count)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "resources": resources,
			"paginator": pageutil.Paginator(paginator.Page(), paginator.PerPageNums, paginator.Nums())}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) DeleteResource() {
	app_id, _ := this.GetInt64("app_id", -1)
	// 刷新内存
	defer memory.FlushAppId(app_id)
	id, _ := this.GetInt64("id")
	err := models.DeleteResource(id)
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR"}
	}
	this.ServeJSON()
}

func (this *WorkController) ValidateResource() {
	var err error
	id, _ := this.GetInt64("id")
	app_id, _ := this.GetInt64("app_id", -1)
	resource, err := models.QueryResourceById(id)
	if err == nil {
		resource.ResourceDsn = this.GlobalVarParserWarpper(app_id, resource.ResourceDsn)
		switch resource.ResourceType {
		case "db":
			db, err1 := iworkpool.GetDBConn("mysql", resource.ResourceDsn)
			if err1 == nil {
				err = db.Ping()
			} else {
				err = err1
			}
		case "sftp":
			sshClient, sftpClient, err1 := sftputil.SFTPConnect(resource.ResourceUsername, resource.ResourcePassword, resource.ResourceDsn, 22)
			if err1 == nil {
				defer sshClient.Close()
				defer sftpClient.Close()
			}
			err = err1
		case "ssh":
			err = sshutil.SSHConnectTest(resource.ResourceUsername, resource.ResourcePassword, resource.ResourceDsn, 22)
		}
	}
	if err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}
	this.ServeJSON()
}
