package startup

import "github.com/ivpusic/grpool"

var RunLogPool = grpool.NewPool(1000, 1000)
