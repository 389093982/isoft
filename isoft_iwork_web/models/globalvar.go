package models

import (
	"fmt"
	"github.com/astaxie/beego/orm"
	"isoft/isoft_utils/common/stringutil"
	"strings"
	"time"
)

type GlobalVar struct {
	Id              int64     `json:"id"`
	AppId           int64     `json:"app_id"`
	EnvName         string    `json:"env_name"`
	Name            string    `json:"name"`
	Value           string    `json:"value" orm:"type(text)"`
	Type            int       `json:"type"` // 类型：0 表示不可删除
	Desc            string    `json:"desc"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

// 多字段唯一键
func (u *GlobalVar) TableUnique() [][]string {
	return [][]string{
		{"AppId", "Name", "EnvName"},
	}
}

func QueryGlobalVarByName(app_id int64, name string) (gv GlobalVar, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("global_var")
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	err = qs.Filter("name", name).One(&gv)
	return
}

func GetAllGlobalVars() (gvs []string) {
	var list orm.ParamsList
	o := orm.NewOrm()
	_, err := o.QueryTable("global_var").ValuesFlat(&list, "name")
	if err == nil {
		for _, lst := range list {
			gvs = append(gvs, fmt.Sprintf("$Global.%s;", lst.(string)))
		}
	}
	gvs = stringutil.RemoveRepeatForSlice(gvs)
	return
}

func DeleteGlobalVarById(id int64) error {
	o := orm.NewOrm()
	_, err := o.QueryTable("global_var").Filter("id", id).Delete()
	return err
}

func InsertOrUpdateGlobalVar(globalVar *GlobalVar, o orm.Ormer) (id int64, err error) {
	if globalVar.Id > 0 {
		id, err = o.Update(globalVar)
	} else {
		id, err = o.Insert(globalVar)
	}
	return
}

func QueryAllGlobalVar(app_id int64) (globalVars []GlobalVar) {
	o := orm.NewOrm()
	qs := o.QueryTable("global_var")
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	qs.All(&globalVars)
	return
}

func QueryGlobalVar(app_id int64, condArr map[string]string, page int, offset int, o orm.Ormer) (globalVars []GlobalVar, counts int64, err error) {
	qs := o.QueryTable("global_var")
	if search, ok := condArr["search"]; ok && strings.TrimSpace(search) != "" {
		qs = qs.Filter("name__contains", search)
	}
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	counts, _ = qs.Count()
	qs = qs.OrderBy("-last_updated_time").Limit(offset, (page-1)*offset)
	qs.All(&globalVars)
	return
}
