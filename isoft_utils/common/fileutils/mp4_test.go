package fileutils

import (
	"fmt"
	"os"
	"path/filepath"
	"testing"
)

func Test_Mp4Duration(t *testing.T) {
	file, err := os.Open(`D:\linkknown\linkknown_upload\O7k4+3AEnj5F9TOzfMrpla6WUW4EwvNbDBFC5HsqOcE=.mp4`)
	if err != nil {
		panic(err)
	}
	duration, err := GetMP4Duration(file)
	if err != nil {
		panic(err)
	}
	fmt.Println("~~~~~~~~~~~~~", filepath.Base(os.Args[1]), duration)
}
