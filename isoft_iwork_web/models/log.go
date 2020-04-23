package models

import (
	"github.com/astaxie/beego/orm"
	"time"
)

type RunlogRecord struct {
	Id              int64     `json:"id"`
	AppId           int64     `json:"app_id"`
	TrackingId      string    `json:"tracking_id"`
	WorkId          int64     `json:"work_id"`
	WorkName        string    `json:"work_name"`
	LogLevel        string    `json:"log_level"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

type RunlogDetail struct {
	Id              int64     `json:"id"`
	TrackingId      string    `json:"tracking_id" orm:"null"`
	WorkStepName    string    `json:"work_step_name" orm:"null"`
	LogLevel        string    `json:"log_level" orm:"null"` // INFO、SUCCESS、ERROR
	Detail          string    `json:"detail" orm:"type(text);null"`
	LogOrder        int64     `json:"log_order" orm:"null"`
	CreatedBy       string    `json:"created_by" orm:"null"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime);null"`
	LastUpdatedBy   string    `json:"last_updated_by" orm:"null"`
	LastUpdatedTime time.Time `json:"last_updated_time" orm:"null"`
}

// 多字段索引
func (this *RunlogDetail) TableIndex() [][]string {
	return [][]string{
		[]string{"Id", "TrackingId"},
	}
}

func InsertRunlogRecord(record *RunlogRecord) (id int64, err error) {
	o := orm.NewOrm()
	id, err = o.Insert(record)
	return
}

func InsertMultiRunlogDetail(details []*RunlogDetail) (num int64, err error) {
	o := orm.NewOrm()
	for _, detail := range details {
		if detail.LogLevel == "ERROR" {
			o.QueryTable("runlog_record").Filter("tracking_id", detail.TrackingId).Update(orm.Params{"LogLevel": "ERROR"})
			break
		}
	}
	num, err = o.InsertMulti(len(details), &details)
	return
}

func QueryRunlogRecordWithTracking(tracking_id string) (runLogRecord RunlogRecord, err error) {
	o := orm.NewOrm()
	err = o.QueryTable("runlog_record").Filter("tracking_id", tracking_id).One(&runLogRecord)
	return
}

func QueryRunlogRecordCount(workId int64, log_level string) (count int64, err error) {
	qs := orm.NewOrm().QueryTable("runlog_record").Filter("work_id", workId)
	if log_level != "" {
		qs = qs.Filter("log_level", log_level)
	}
	count, err = qs.Count()
	return
}

func QueryRunlogRecord(app_id int64, work_id int64, logLevel string, page int, offset int) (runLogRecords []RunlogRecord, counts int64, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("runlog_record")
	if work_id > 0 {
		work, _ := QueryWorkById(work_id, o)
		qs = qs.Filter("work_name", work.WorkName)
	}
	if app_id > 0 {
		qs = qs.Filter("app_id", app_id)
	}
	if logLevel != "" {
		qs = qs.Filter("log_level", logLevel)
	}
	qs = qs.OrderBy("-last_updated_time")
	counts, _ = qs.Count()
	qs = qs.Limit(offset, (page-1)*offset)
	_, err = qs.All(&runLogRecords)
	return
}

func QueryLastRunlogDetail(tracking_id string) (runLogDetails []RunlogDetail, err error) {
	o := orm.NewOrm()
	// __startswith 多级 tracking_id 也查出来
	_, err = o.QueryTable("runlog_detail").Filter("tracking_id__startswith", tracking_id).OrderBy("log_order").All(&runLogDetails)
	return
}

type ValidatelogRecord struct {
	Id              int64     `json:"id"`
	TrackingId      string    `json:"tracking_id"`
	WorkId          int64     `json:"work_id"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

type ValidatelogDetail struct {
	Id              int64     `json:"id"`
	TrackingId      string    `json:"tracking_id"`
	WorkId          int64     `json:"work_id"`
	WorkStepId      int64     `json:"work_step_id"`
	WorkName        string    `json:"work_name"`
	WorkStepName    string    `json:"work_step_name"`
	ParamName       string    `json:"param_name"`
	SuccessFlag     bool      `json:"success_flag"` // 默认就是 false
	Detail          string    `json:"detail" orm:"type(text)"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

func InsertValidatelogRecord(record *ValidatelogRecord) (id int64, err error) {
	o := orm.NewOrm()
	id, err = o.Insert(record)
	return
}

func InsertMultiValidatelogDetail(details []*ValidatelogDetail) (num int64, err error) {
	o := orm.NewOrm()
	num, err = o.InsertMulti(len(details), &details)
	return
}

func QueryLastValidatelogRecord(work_id int64) (record ValidatelogRecord, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("validatelog_record")
	if work_id > 0 {
		qs = qs.Filter("work_id", work_id)
	}
	err = qs.OrderBy("-last_updated_time").One(&record)
	return
}

func QueryLastValidatelogDetail(work_id int64) (details []ValidatelogDetail, err error) {
	if record, err := QueryLastValidatelogRecord(work_id); err == nil {
		o := orm.NewOrm()
		_, err = o.QueryTable("validatelog_detail").
			Filter("tracking_id", record.TrackingId).OrderBy("success_flag", "-work_id", "work_step_id").All(&details)
	}
	return
}
