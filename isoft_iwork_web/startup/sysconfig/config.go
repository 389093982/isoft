package sysconfig

import "github.com/astaxie/beego"

var SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
