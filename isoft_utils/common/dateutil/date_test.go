package dateutil

import (
	"fmt"
	"strconv"
	"testing"
	"time"
)

func Test_ParseInLocation(t *testing.T) {
	fmt.Println(time.Now().Unix())
	ts, err := strconv.ParseInt("1582819200000", 10, 64)
	if err == nil {
		//时间戳 to 时间
		tm := time.Unix(ts/1e3, 0)
		fmt.Println(tm.Local())
	} else {
		panic(err)
	}
}
