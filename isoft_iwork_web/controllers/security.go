package controllers

import "isoft/isoft_utils/common/chiperutil"

func (this *WorkController) Login() {
	username := this.GetString("username")
	passwd := this.GetString("passwd")
	expiredSecond := 3600 * 1 // 默认 1 h 过期时间
	tokenString, err := chiperutil.CreateJWT("iwork", map[string]string{"username": username, "passwd": passwd}, int64(expiredSecond))
	if username == "admin" && passwd == "admin123" && err == nil {

		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "tokenString": tokenString, "loginExpiredSecond": expiredSecond}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": "login error!"}
	}
	this.ServeJSON()
}
