package dipool

import (
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkrun"
	"isoft/isoft_iwork_web/startup/dipool/pool"
)

type IworkRunImpl struct {
}

func (this *IworkRunImpl) RunWork(cache *iworkcache.WorkCache) error {
	iworkrun.RunOneWork(cache.WorkId, nil)
	return nil
}

func init() {
	parser := node.ParamSchemaParser{}
	pool.Container.SetSingleton("parser", &parser)
	pool.Container.SetSingleton("iworkRunImpl", &IworkRunImpl{})
}
