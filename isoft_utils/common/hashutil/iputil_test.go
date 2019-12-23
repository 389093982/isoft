package hashutil

import (
	"fmt"
	"isoft/isoft_utils/common/osutil"
	"testing"
)

func Test_GetLocalIp(t *testing.T) {
	fmt.Println(osutil.GetLocalIp())
}
