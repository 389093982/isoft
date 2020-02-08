package memory

import (
	"isoft/isoft_iwork_web/models"
	"sync"
)

var GlobalVarMap sync.Map
var ResourceMap sync.Map
var FilterMap sync.Map

func FlushAll() {
	FlushMemoryGlobalVar(-1)
	FlushMemoryResource(-1)
	FlushMemoryFilter(-1)
}

func FlushMemoryFilter(app_id int64) {
	filters, _ := models.QueryAllFilters(app_id)
	for index, _ := range filters {
		// 不存在则初始化并添加
		// 存在则获取后添加
		if fs, ok := FilterMap.LoadOrStore(string(filters[index].AppId)+"_"+filters[index].FilterWorkName, []*models.Filters{&filters[index]}); ok {
			FilterMap.Store(string(filters[index].AppId)+"_"+filters[index].FilterWorkName, append(fs.([]*models.Filters), &filters[index]))
		}
	}
}

func FlushMemoryGlobalVar(app_id int64) {
	globalVars := models.QueryAllGlobalVar(app_id)
	for index, _ := range globalVars {
		GlobalVarMap.Store(string(globalVars[index].AppId)+"_"+globalVars[index].Name, &globalVars[index])
	}
}

func FlushMemoryResource(app_id int64) {
	resources := models.QueryAllResource(app_id)
	for index, _ := range resources {
		ResourceMap.Store(string(resources[index].AppId)+"_"+resources[index].ResourceName, &resources[index])
	}
}

var appIdCacheMap sync.Map
var appNameCacheMap sync.Map

func ReloadAppId(app_id int64) {
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
