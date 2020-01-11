package iworkpool

import (
	"isoft/isoft_iwork_web/core/iworklog"
	"sync"
)

var CacheLoggerWriterPool = sync.Pool{
	New: func() interface{} {
		return new(iworklog.CacheLoggerWriter)
	},
}
