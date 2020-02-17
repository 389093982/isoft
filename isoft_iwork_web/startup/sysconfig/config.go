package sysconfig

import (
	"github.com/astaxie/beego"
	"isoft/isoft_iwork_web/core/iworkutil/fileutil"
)

var (
	FileSavePath               string // 静态文件服务器地址
	SYSCONFIG_LOGWIRTER_ENABLE bool
	IWORK_PANICTRACE_SIZE      int
	ENV_ONUSE                  string
)

func init() {
	FileSavePath = beego.AppConfig.String("file.upload.server")
	fileutil.MkdirAll(FileSavePath)

	SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
	IWORK_PANICTRACE_SIZE, _ = beego.AppConfig.Int("iwork.panicTrace.size")
	ENV_ONUSE = beego.AppConfig.String("iwork.envname.onuse")
}
