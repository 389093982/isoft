package controllers

import "github.com/astaxie/beego"

// 该控制器处理页面错误请求
type ErrorController struct {
	beego.Controller
}

func (this *ErrorController) Error404() {
	this.TplName = "index.html"
}
