package main

import (
	"github.com/astaxie/beego"
	"isoft/isoft_iwork_web/controllers"
	"isoft/isoft_iwork_web/core/iworkplugin/node/regist"
	"isoft/isoft_iwork_web/core/iworkpool"
	_ "isoft/isoft_iwork_web/routers"
	_ "isoft/isoft_iwork_web/startup/db"
	_ "isoft/isoft_iwork_web/startup/dipool"
	"isoft/isoft_iwork_web/startup/filter"
	_ "isoft/isoft_iwork_web/startup/logger"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	_ "isoft/isoft_iwork_web/startup/sysconfig"
	"isoft/isoft_iwork_web/startup/task"
	_ "net/http/pprof"
)

func main() {
	// 首页和 404 页面都跳往前端 index.html 页面
	beego.ErrorController(&controllers.ErrorController{})

	beego.InsertFilter("/api/iwork/httpservice/*", beego.BeforeExec, filter.FilterFunc)
	beego.InsertFilter("/*", beego.BeforeExec, filter.IPFilterFunc)

	memory.FlushAll()
	iworkpool.LoadAndCachePool()
	regist.RegistNodes()
	task.RegisterCronTask()
	beego.SetStaticPath("/api/iwork/files", sysconfig.FileSavePath)

	beego.Run()
}
