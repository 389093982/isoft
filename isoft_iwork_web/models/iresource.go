package models

import (
	"github.com/astaxie/beego/orm"
	"strings"
	"sync"
	"time"
)

var openConnFunc func(driverName, dataSourceName string) (err error)

// 注册打开连接函数
func RegisterOpenConnFunc(f func(driverName, dataSourceName string) (err error)) {
	var once = &sync.Once{}
	once.Do(func() {
		openConnFunc = f
	})
}

type Resource struct {
	Id               int64     `json:"id"`
	AppId            int64     `json:"app_id"`
	ResourceName     string    `json:"resource_name"`
	ResourceType     string    `json:"resource_type"`
	ResourceUrl      string    `json:"resource_url"`
	ResourceDsn      string    `json:"resource_dsn"`
	ResourceUsername string    `json:"resource_username"`
	ResourcePassword string    `json:"resource_password"`
	CreatedBy        string    `json:"created_by"`
	CreatedTime      time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy    string    `json:"last_updated_by"`
	LastUpdatedTime  time.Time `json:"last_updated_time"`
}

func InsertOrUpdateResource(resource *Resource) (id int64, err error) {
	o := orm.NewOrm()
	if resource.Id > 0 {
		id, err = o.Update(resource)
	} else {
		id, err = o.Insert(resource)
	}
	if err == nil && resource.ResourceType == "db" && openConnFunc != nil {
		openConnFunc("mysql", resource.ResourceDsn)
	}
	return
}

func QueryResource(app_id int64, condArr map[string]string, page int, offset int) (resources []Resource, counts int64, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("resource")
	var cond = orm.NewCondition()
	if search, ok := condArr["search"]; ok && strings.TrimSpace(search) != "" {
		subCond := orm.NewCondition()
		subCond = cond.And("resource_name__contains", search).Or("resource_type__contains", search).Or("resource_url__contains", search)
		cond = cond.AndCond(subCond)
	}
	cond = cond.And("app_id", app_id)
	qs = qs.SetCond(cond)
	counts, _ = qs.Count()
	qs = qs.Limit(offset, (page-1)*offset)
	qs.All(&resources)
	return
}

func QueryAllResource(app_id int64, resource_type ...string) (resources []Resource) {
	o := orm.NewOrm()
	qs := o.QueryTable("resource")
	if app_id > 0 { // app_id <= 0 查询全部
		qs = qs.Filter("app_id", app_id)
	}
	if len(resource_type) > 0 {
		qs = qs.Filter("resource_type", resource_type[0])
	}
	qs.All(&resources)
	return
}

func QueryResourceByName(app_id int64, resource_name string) (resource Resource, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("resource").Filter("resource_name", resource_name)
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	err = qs.One(&resource)
	return
}

func DeleteResource(id int64) error {
	o := orm.NewOrm()
	_, err := o.QueryTable("resource").Filter("id", id).Delete()
	return err
}

func QueryResourceById(id int64) (resource Resource, err error) {
	o := orm.NewOrm()
	err = o.QueryTable("resource").Filter("id", id).One(&resource)
	return
}
