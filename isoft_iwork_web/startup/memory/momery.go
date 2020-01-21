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
