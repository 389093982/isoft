package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

type AppId struct {
	Id              int64     `json:"id"`
	AppName         string    `json:"app_name"`
	AppDesc         string    `json:"app_desc"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

func InsertOrUpdateAppId(appId *AppId) (id int64, err error) {
	o := orm.NewOrm()
	if appId.Id > 0 {
		id, err = o.Update(appId)
	} else {
		id, err = o.Insert(appId)
	}
	return
}

func QueryPageAppIdList(condArr map[string]string, page int, offset int) (appIds []AppId, counts int64, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("app_id")
	counts, _ = qs.Count()
	qs = qs.Limit(offset, (page-1)*offset)
	qs.All(&appIds)
	return
}

func DeleteAppById(id int64) error {
	o := orm.NewOrm()
	_, err := o.QueryTable("app_id").Filter("id", id).Delete()
	return err
}
