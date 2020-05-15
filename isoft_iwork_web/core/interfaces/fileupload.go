package interfaces

import "net/http"

type IFileUploadDownload interface {
	SaveFile(suffixs []string, file_size int64) (tempFileName, fileName, tempFilePath string,fileSize int64, err error)
	GetWriter() http.ResponseWriter
	WriteResponseHeader(key, value string)
}
