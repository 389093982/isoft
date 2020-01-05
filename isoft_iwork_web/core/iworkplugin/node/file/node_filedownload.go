package file

import (
	"bufio"
	"io"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
	"net/http"
	"net/url"
	"strconv"
)

type DoDownloadFileNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *DoDownloadFileNode) Execute(trackingId string) {
	filename := this.TmpDataMap[iworkconst.STRING_PREFIX+"filename"].(string)
	urlStr := this.TmpDataMap[iworkconst.STRING_PREFIX+"url"].(string)
	buffLen := 1024 * 50
	if buff, ok := this.TmpDataMap[iworkconst.STRING_PREFIX+"buff?"].(string); ok {
		buffLen, _ = strconv.Atoi(buff)
	}
	fileDownload := this.Dispatcher.TmpDataMap[iworkconst.HTTP_REQUEST].(interfaces.IFileUploadDownload)
	// 设置附件响应头
	fileDownload.WriteResponseHeader("Content-Disposition", "attachment; filename="+url.PathEscape(filename))
	fileDownload.WriteRequestHeader("isDoDownloadFileNode", "isDoDownloadFileNode")
	// 发送网络请求读取资源
	resp, _ := http.Get(urlStr)
	// 缓冲读
	bufReader := bufio.NewReader(resp.Body)
	// 缓冲写
	bufWriter := bufio.NewWriter(fileDownload.GetWriter())
	buff := make([]byte, buffLen)
	for {
		len, err := bufReader.Read(buff)
		bufWriter.Write(buff[:len])
		if err == io.EOF {
			break
		}
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, map[string]interface{}{})
}

func (this *DoDownloadFileNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.STRING_PREFIX + "filename", "下载文件名,以附件形式下载"},
		2: {iworkconst.STRING_PREFIX + "url", "下载文件网络地址"},
		3: {iworkconst.STRING_PREFIX + "buff?", "缓冲大小,以字节为单位"},
	}
	choiceMap := map[string][]string{iworkconst.BOOL_PREFIX + "calHash?": {"`true`", "`false`"}}
	return this.BPIS1(paramMap, choiceMap)
}
