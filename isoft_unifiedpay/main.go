package main

import (
	"github.com/astaxie/beego"
	_ "isoft/isoft_unifiedpay/routers"
	_ "isoft/isoft_unifiedpay/startup/db"
	_ "isoft/isoft_unifiedpay/startup/globalSessions"
	_ "isoft/isoft_unifiedpay/startup/logger"
	_ "isoft/isoft_unifiedpay/startup/memory"
)

func main() {
	beego.Run()
}
