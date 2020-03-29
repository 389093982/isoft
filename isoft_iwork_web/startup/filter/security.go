package filter

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/context"
	"isoft/isoft_unifiedpay/common/chiperutil"
	"strings"
)

var whiteIps string

func init() {
	whiteIps = beego.AppConfig.String("iwork.check.white.ips")
}

func SecurityFilterFunc(ctx *context.Context) {
	if isWhiteUrl(ctx.Request.URL.String()) {
		return
	}
	ipFilter(ctx)
	loginFilter(ctx)
}

func isWhiteUrl(url string) bool {
	return strings.HasPrefix(url, "/api/iwork/httpservice/") || strings.HasPrefix(url, "/api/iwork/login")
}

func checkIp(ip string) bool {
	return strings.Contains(whiteIps, ip)
}

func ipFilter(ctx *context.Context) {
	if !checkIp(ctx.Input.IP()) {
		ctx.ResponseWriter.WriteHeader(401)
	}
}

func loginFilter(ctx *context.Context) {
	tokenString := ctx.Request.FormValue("tokenString")
	if _, err := chiperutil.ParseJWT("iwork", tokenString); err != nil {
		ctx.ResponseWriter.WriteHeader(401)
	}
}
