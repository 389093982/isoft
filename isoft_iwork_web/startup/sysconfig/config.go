package sysconfig

import "github.com/astaxie/beego"

var SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
var IWORK_PANICTRACE_SIZE, _ = beego.AppConfig.Int("iwork.panicTrace.size")
var ENV_ONUSE = beego.AppConfig.String("iwork.envname.onuse")
