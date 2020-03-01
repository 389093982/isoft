package logutil

import "github.com/astaxie/beego/logs"

func Info(f interface{}, v ...interface{}) {
	logs.Info(f, v...)
}

func Warn(f interface{}, v ...interface{}) {
	logs.Warn(f, v...)
}

func Error(f interface{}, v ...interface{}) {
	logs.Error(f, v...)
}

func Debug(f interface{}, v ...interface{}) {
	logs.Debug(f, v...)
}
