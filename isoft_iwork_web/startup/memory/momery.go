package memory

import (
	"isoft/isoft_iwork_web/models"
	"sync"
)

var GlobalVarMap sync.Map
var ResourceMap sync.Map
var FilterMap sync.Map

func FlushAll() {
	FlushMemoryGlobalVar()
	FlushMemoryResource(-1)
	FlushMemoryFilter()
}

func FlushMemoryFilter() {
	filters, _ := models.QueryAllFilters()
	for index, _ := range filters {
		// 不存在则初始化并添加
		// 存在则获取后添加
		if fs, ok := FilterMap.LoadOrStore(filters[index].FilterWorkName, []*models.Filters{&filters[index]}); ok {
			FilterMap.Store(filters[index].FilterWorkName, append(fs.([]*models.Filters), &filters[index]))
		}
	}
}

func FlushMemoryGlobalVar() {
	globalVars := models.QueryAllGlobalVar()
	for index, _ := range globalVars {
		GlobalVarMap.Store(globalVars[index].Name, &globalVars[index])
	}
}

func FlushMemoryResource(app_id int64) {
	resources := models.QueryAllResource(app_id)
	for index, _ := range resources {
		ResourceMap.Store(resources[index].ResourceName, &resources[index])
	}
}
