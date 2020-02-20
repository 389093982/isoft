package sysconfig

import (
	"github.com/astaxie/beego"
	"isoft/isoft_iwork_web/core/iworkutil/fileutil"
	"isoft/isoft_iwork_web/core/iworkutil/stringutil"
	"strings"
)

var (
	FileSavePath               string // 静态文件服务器地址
	SYSCONFIG_LOGWIRTER_ENABLE bool
	IWORK_PANICTRACE_SIZE      int
	ENV_ONUSE                  string
	PERSISTENT_DIR             string // iwork 框架持久化文件所在目录
	INTERNAL_ERROR_MSG         string // iwork 框架内部出错默认提示文字
)

func init() {
	FileSavePath = beego.AppConfig.String("file.upload.server")
	fileutil.MkdirAll(FileSavePath)

	SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
	IWORK_PANICTRACE_SIZE, _ = beego.AppConfig.Int("iwork.panicTrace.size")
	ENV_ONUSE = beego.AppConfig.String("iwork.envname.onuse")
	PERSISTENT_DIR = strings.TrimSpace(beego.AppConfig.String("iwork.persistent.dir"))
	INTERNAL_ERROR_MSG = stringutil.GetString(beego.AppConfig.String("iwork.internal.errorMsg"), "InternalError", true)
}
