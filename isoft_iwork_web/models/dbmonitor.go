package models

import (
	"github.com/astaxie/beego/orm"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"time"
)

type DbMonitor struct {
	Id              int64     `json:"id"`
	AppId           int64     `json:"app_id"`
	ResourceName    string    `json:"resource_name"`
	TableName       string    `json:"table_name"`
	DataCount       int64     `json:"data_count"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

func InsertDBMonitor(monitor *DbMonitor, o orm.Ormer) (id int64, err error) {
	id, err = o.Insert(monitor)
	return
}

func QueryDBMonitorMeta() ([]orm.ParamsList, []orm.ParamsList, error) {
	o := orm.NewOrm()
	rs1 := make([]orm.ParamsList, 0)
	rs2 := make([]orm.ParamsList, 0)
	_, err1 := o.QueryTable("db_monitor").Distinct().ValuesList(&rs1, "app_id", "resource_name")
	_, err2 := o.QueryTable("db_monitor").Distinct().ValuesList(&rs2, "app_id", "resource_name", "table_name")
	return rs1, rs2, errorutil.GetFirstError2(err1, err2)
}

func QueryDBMonitorData(app_id int64, resource_name, table_name string) (monitors []DbMonitor, err error) {
	o := orm.NewOrm()
	_, err = o.QueryTable("db_monitor").Filter("app_id", app_id).Filter("resource_name", resource_name).
		Filter("table_name", table_name).OrderBy("-last_updated_time").Limit(30).All(&monitors)
	return
}
