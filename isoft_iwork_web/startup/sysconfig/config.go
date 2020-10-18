package sysconfig

import (
	"github.com/astaxie/beego"
	"isoft/isoft_iwork_web/core/iworkutil/fileutil"
	"isoft/isoft_iwork_web/core/iworkutil/stringutil"
	"isoft/isoft_iwork_web/startup/db"
	"strings"
)

var (
	FileSavePath               string // 静态文件服务器地址
	SYSCONFIG_LOGWIRTER_ENABLE bool
	IWORK_PANICTRACE_SIZE      int
	ENV_ONUSE                  string
	PERSISTENT_DIR             string // iwork 框架持久化文件所在目录
	INTERNAL_ERROR_MSG         string // iwork 框架内部出错默认提示文字
	IWORK_DBMONITOR_CRON       string // DB 监控频率 cron 表达式
	IWORK_DB_DSN               string
	IWORK_ADMIN_ACCOUNT        string
	IWORK_ADMIN_PASSWORD       string
)

func init() {
	FileSavePath = beego.AppConfig.String("file.upload.server")
	fileutil.MkdirAll(FileSavePath)

	SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
	IWORK_PANICTRACE_SIZE, _ = beego.AppConfig.Int("iwork.panicTrace.size")
	ENV_ONUSE = beego.AppConfig.String("iwork.envname.onuse")
	PERSISTENT_DIR = strings.TrimSpace(beego.AppConfig.String("iwork.persistent.dir"))
	INTERNAL_ERROR_MSG = stringutil.GetString(beego.AppConfig.String("iwork.internal.errorMsg"), "InternalError", true)
	IWORK_DBMONITOR_CRON = stringutil.GetString(beego.AppConfig.String("iwork.dbmonitor.cron"), "0 0 * * * ?", true)
	IWORK_DB_DSN = db.Dsn
	IWORK_ADMIN_ACCOUNT = beego.AppConfig.String("iwork.admin.account")
	IWORK_ADMIN_PASSWORD = beego.AppConfig.String("iwork.admin.password")
}
