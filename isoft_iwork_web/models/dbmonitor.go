package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

type DbMonitor struct {
	Id              int64     `json:"id"`
	AppId           int64     `json:"id"`
	ResourceName    string    `json:"dsn"`
	TableName       string    `json:"dsn"`
	DataCount       int64     `json:"app_id"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

func InsertDBMonitor(monitor *DbMonitor, o orm.Ormer) (id int64, err error) {
	id, err = o.Insert(monitor)
	return
}
