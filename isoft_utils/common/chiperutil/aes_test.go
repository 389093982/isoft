package chiperutil

import (
	"fmt"
	"testing"
)

func Test_Aes(t *testing.T) {
	//str := GenerateRandSeq(32)
	str := "zhourui&zhoupeng12345678"
	fmt.Println(str)
	encryptStr := AesEncryptToStr("123456", str)
	fmt.Println(encryptStr)
	decryptStr := AesDecryptToStr("jKgvx136LQWYrTlQZ5RMEQ==", str)
	fmt.Println(decryptStr)
}
