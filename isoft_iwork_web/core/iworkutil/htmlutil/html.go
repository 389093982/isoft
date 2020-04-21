package htmlutil

import (
	"io/ioutil"
	"net/http"
	"regexp"
	"strings"
)

func getUrlBytes(url string) (bytes []byte) {
	resp, err := http.Get(url)
	if err != nil {
		return
	}
	defer resp.Body.Close()
	bytes, _ = ioutil.ReadAll(resp.Body)
	return
}

func GetAllHref(url string) (results []string) {
	// 发送 HTTP 请求获取 URL 地址 bytes
	body := getUrlBytes(url)
	href_pattern := "(href|src)=\"(.+?)\""
	href_reg := regexp.MustCompile(href_pattern)
	// 根据正则表达式获取所有的地址
	hrefs := href_reg.FindAllString(string(body), -1)
	// v 的格式可能是
	// href="/a/b/c.css" href="http://www.baidu.com/a/b/c.css"
	// src="/a/b/c.css"	 src="http://www.baidu.com/a/b/c.css"
	for _, v := range hrefs {
		str := strings.Split(v, "\"")[1] // 获取中间部分
		if len(str) < 1 {
			continue
		}
		switch str[0] {
		case 'h': // http 开始的绝对地址
			results = append(results, str)
		case '/': // 以 / 开头的相对地址
			if len(str) != 1 && str[1] == '/' { // 以双 // 开头补齐协议
				results = append(results, "http:"+str)
			}

			if len(str) != 1 && str[1] != '/' { // 以单 / 开头补齐站点
				results = append(results, url+str[1:])
			}
		default:
			results = append(results, url+str)
		}
	}
	return results
}
