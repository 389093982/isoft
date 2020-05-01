package stringutil

import (
	"fmt"
	"isoft/isoft_iwork_web/core/iworkutil/fileutil"
	"os"
	"path/filepath"
	"strings"
	"testing"
)

func Test_Rune(t *testing.T) {
	fmt.Print(strings.IndexRune("hello中国.txt", '.'))
}

func Test_GetTypeOfInterface(t *testing.T) {
	fmt.Print(GetTypeOfInterface("demo"))
}

func Test_GetNoRepeatSubStringWithRegexp(t *testing.T) {
	ss := GetNoRepeatSubStringWithRegexp("a$hello.hello", `\$[a-zA-Z0-9_]+`)
	for _, s := range ss {
		fmt.Println(s)
	}
}

func Test_GetXXX(t *testing.T) {
	fmt.Println(ReplaceAllString("11111/*dsfsdfs8**/dfdddd", "\\/\\*.*\\*\\/", ""))
	fmt.Println(ReplaceAllString("11111/*dsfsdfs8**/;dfdddd", "\\/\\*.*\\*\\/;", ""))
}


func Test_WalkDir(t *testing.T) {
	var fileList []string

	var walkFunc = func (path string, info os.FileInfo, err error) error {
		if !info.IsDir() {
			if strings.HasSuffix(info.Name(), ".jpg") ||
				strings.HasSuffix(info.Name(), ".png") ||
				strings.HasSuffix(info.Name(), ".gif") {
				fileList = append(fileList, path)
			}
		}
		return nil
	}
	filepath.Walk("D:\\xmly", walkFunc)
	for _,fp := range fileList {
		fileutil.CopyFile(fp, "D:/imageScanner/" + filepath.Base(fp))
	}
}