package task

import (
	"fmt"
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/toolbox"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/core/iworkutil/sqlutil"
	"isoft/isoft_iwork_web/core/logutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"strings"
	"time"
)

func init() {
	RunDBWatcher()
}

func RunDBWatcher() {
	tk := toolbox.NewTask("RunDBWatcher", sysconfig.IWORK_DBMONITOR_CRON, func() error {
		resources := models.QueryAllResource(-1, "db")
		for _, resource := range resources {
			watcher := new(DBWatcher)
			watcher.Dsn = watcher.GlobalVarParserWarpper(resource.AppId, resource.ResourceDsn)
			watcher.resource = &resource
			watcher.Run()
		}
		return nil
	})
	toolbox.AddTask("RunDBWatcher", tk)
}

type DBWatcher struct {
	resource *models.Resource
	Dsn      string
}

func (this *DBWatcher) Run() {
	defer func() {
		if err := recover(); err != nil {
			logutil.Error("DBWatcher Run error! %v", err)
		}
	}()
	tableNames := sqlutil.GetAllTableNames(this.Dsn)
	for _, tableName := range tableNames {
		datacount := sqlutil.QuerySelectCount(fmt.Sprintf(`select count(*) as count from %s`, tableName), make([]interface{}, 0), this.Dsn)
		monitor := &models.DbMonitor{
			AppId:           this.resource.AppId,
			ResourceName:    this.resource.ResourceName,
			TableName:       tableName,
			DataCount:       datacount,
			LastUpdatedTime: time.Now(),
		}
		models.InsertDBMonitor(monitor, orm.NewOrm())
	}
}

func (this *DBWatcher) GlobalVarParserWarpper(app_id int64, value string) string {
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
