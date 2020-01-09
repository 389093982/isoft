package sysconfig

import "github.com/astaxie/beego"

var SYSCONFIG_VARS_USAGE_LOGGABLE = beego.AppConfig.DefaultBool("sysconfig_vars_usage_loggable", false)

var SYSCONFIG_LOGWIRTER_ENABLE = beego.AppConfig.DefaultBool("iwork.logwriter.enable", false)
