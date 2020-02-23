package iworklog

import (
	"fmt"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"time"
)

const cacheLen = 5

type CacheLoggerWriter struct {
	caches   []*models.RunlogDetail
	logOrder int64
}

func (this *CacheLoggerWriter) Reset() *CacheLoggerWriter {
	this.cleanCaches()
	this.logOrder = 0
	return this
}

func (this *CacheLoggerWriter) cleanCaches() {
	this.caches = this.caches[0:0]
}

func (this *CacheLoggerWriter) Write(trackingId, workStepName, logLevel, detail string) {
	this.logOrder = this.logOrder + 1
	log := &models.RunlogDetail{
		TrackingId:      trackingId,
		WorkStepName:    workStepName,
		LogLevel:        logLevel,
		Detail:          detail,
		LogOrder:        this.logOrder,
		CreatedBy:       "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedBy:   "SYSTEM",
		LastUpdatedTime: time.Now(),
	}
	this.caches = append(this.caches, log)
	if len(this.caches) >= cacheLen {
		this.flush()
		this.cleanCaches()
	}
}

func (this *CacheLoggerWriter) flush() {
	if sysconfig.SYSCONFIG_LOGWIRTER_ENABLE {
		caches := make([]*models.RunlogDetail, len(this.caches))
		copy(caches, this.caches) // 值拷贝
		startup.RunLogPool.JobQueue <- func() {
			if _, err := models.InsertMultiRunlogDetail(caches); err != nil {
				fmt.Println("@@@@@@@@@@@@@@@@@@@@@@@" + err.Error())
			}
		}
	}
}

func (this *CacheLoggerWriter) Close() {
	if len(this.caches) > 0 {
		this.flush()
	}
}

// 统计操作所花费的时间方法
func (this *CacheLoggerWriter) RecordCostTimeLog(operateName, trackingId string, start time.Time) {
	this.Write(trackingId, "", iworkconst.LOG_LEVEL_INFO,
		fmt.Sprintf("%s total cost time :%v ms", operateName, time.Now().Sub(start).Nanoseconds()/1e6))
}
