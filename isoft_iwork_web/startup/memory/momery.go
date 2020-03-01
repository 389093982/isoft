package memory

import (
	"fmt"
	"github.com/astaxie/beego/logs"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/task"
	"strings"
	"sync"
	"time"
)

var appIdCacheMap sync.Map
var appNameCacheMap sync.Map
var GlobalVarMap sync.Map
var ResourceMap sync.Map
var FilterMap sync.Map

// 统计刷新内存所花费的时间
func recordCostTimeLog(app_id int64, start time.Time) {
	logs.Info("flush app_id for %d memory total cost time :%v ms", app_id, time.Now().Sub(start).Nanoseconds()/1e6)
}

func FlushAll() {
	FlushAppId(-1)
}

func FlushAppId(app_id int64) {
	defer recordCostTimeLog(app_id, time.Now())
	// 刷新内存
	FlushMemoryAppId(app_id)
	FlushMemoryGlobalVar(app_id)
	FlushMemoryResource(app_id)
	FlushMemoryFilter(app_id)
	// 同时清理所有的内存 work 流程,第一次访问会再次加载
	iworkcache.DeleteAllWorkCache(app_id)
	// 刷新定时任务
	task.RefreshCronTask(app_id)
}

// 根据 app_id 刷新内存中的 filter
func FlushMemoryFilter(app_id int64) {
	filters, _ := models.QueryAllFilters(app_id)
	for index, _ := range filters {
		filterWorkName := string(filters[index].AppId) + "_" + filters[index].FilterWorkName
		// 不存在则初始化并添加
		// 存在则获取后添加
		if fs, ok := FilterMap.LoadOrStore(filterWorkName, []*models.Filters{&filters[index]}); ok {
			FilterMap.Store(filterWorkName, append(fs.([]*models.Filters), &filters[index]))
		}
	}
}

func FlushMemoryGlobalVar(app_id int64) {
	// 先清除当前 app_id 下面所有的旧数据
	GlobalVarMap.Range(func(k, v interface{}) bool {
		if strings.HasPrefix(k.(string), fmt.Sprintf("%d_", app_id)) {
			GlobalVarMap.Delete(k)
		}
		return true
	})
	globalVars := models.QueryAllGlobalVar(app_id)
	for index, _ := range globalVars {
		GlobalVarMap.Store(fmt.Sprintf("%d_%s_%s", globalVars[index].AppId, globalVars[index].Name, globalVars[index].EnvName), &globalVars[index])
	}
	iworkcache.DeleteAllWorkCache(app_id)
}

func FlushMemoryResource(app_id int64) {
	resources := models.QueryAllResource(app_id)
	for index, _ := range resources {
		ResourceMap.Store(string(resources[index].AppId)+"_"+resources[index].ResourceName, &resources[index])
	}
}

func FlushMemoryAppId(app_id int64) {
	if appId, err := GetAppIdWithCache(app_id, ""); err == nil {
		appIdCacheMap.Delete(app_id)
		appNameCacheMap.Delete(appId.AppName)
	}
	GetAppIdWithCache(app_id, "")
}

func GetAppIdWithCache(app_id int64, app_name string) (*models.AppId, error) {
	if app_id > 0 {
		if cache, ok := appIdCacheMap.Load(app_id); ok {
			return cache.(*models.AppId), nil
		}
	}
	if app_name != "" {
		if cache, ok := appNameCacheMap.Load(app_name); ok {
			return cache.(*models.AppId), nil
		}
	}
	appId, err := models.GetAppId(app_id, app_name)
	if err == nil {
		appIdCacheMap.Store(appId.Id, &appId)
		appNameCacheMap.Store(appId.AppName, &appId)
		return &appId, nil
	} else {
		return nil, err
	}
}
