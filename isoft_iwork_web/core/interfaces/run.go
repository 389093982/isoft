package interfaces

import (
	"isoft/isoft_iwork_web/core/iworkcache"
)

type IworkRun interface {
	RunWork(cache *iworkcache.WorkCache) error
}
