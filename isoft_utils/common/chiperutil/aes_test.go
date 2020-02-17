package chiperutil

import (
	"fmt"
	"testing"
)

// iwork 框架数据库加解密测试函数
func Test_AesEncryptToStr(t *testing.T) {
	str := "BpLnfgDsc2WD8F2qNfHK5a84jjJkwzDk"
	encryptStr := AesEncryptToStr("linker", str)
	fmt.Println(encryptStr)
	encryptStr = AesEncryptToStr("Linker2020", str)
	fmt.Println(encryptStr)
}

func Test_AesDecryptToStr(t *testing.T) {
	str := "BpLnfgDsc2WD8F2qNfHK5a84jjJkwzDk"
	decryptStr := AesDecryptToStr("PTQsnvVsmquBJWFwSO6pNg==", str)
	fmt.Println(decryptStr)
}
