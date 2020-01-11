package routers

import (
	"github.com/astaxie/beego"
	"isoft/isoft_im_api/controllers"
)

func init() {
	beego.Router("/", &controllers.MainController{})

	// 配置 websocket 路由
	//http.HandleFunc("/ws", handleConnections)
	beego.Router("/ws", &controllers.WSController{})
}
