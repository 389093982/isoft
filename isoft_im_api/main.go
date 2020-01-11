package main

import (
	"github.com/astaxie/beego"
	_ "isoft/isoft_im_api/routers"
	_ "isoft/isoft_im_api/startup/db"
	_ "isoft/isoft_iwork_web/startup/logger"
)

func main() {
	beego.Run()
}
