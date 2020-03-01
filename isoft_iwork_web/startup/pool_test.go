package startup

import (
	"fmt"
	"github.com/ivpusic/grpool"
	"testing"
)

func Test_CalculateHashWithFile(t *testing.T) {
	pool := grpool.NewPool(1000, 1000)
	for {
		pool.JobQueue <- func() {
			fmt.Println("helloworld")
		}
	}
}
