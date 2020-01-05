package interfaces

import "net/http"

type IFileUploadDownload interface {
	SaveFile(suffixs []string) (tempFileName, fileName, tempFilePath string)
	GetWriter() http.ResponseWriter
	WriteRequestHeader(key, value string)
	WriteResponseHeader(key, value string)
}
