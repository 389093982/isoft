package file

import (
	"errors"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/param"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_utils/common/fileutils"
	"isoft/isoft_utils/common/hashutil"
	"isoft/isoft_utils/common/stringutil"
	"path"
	"strings"
)

type DoReceiveFileNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *DoReceiveFileNode) Execute(trackingId string) {
	fileUpload := this.Dispatcher.TmpDataMap[iworkconst.HTTP_REQUEST].(interfaces.IFileUploadDownload)
	suffixStr := this.TmpDataMap[iworkconst.STRING_PREFIX+"suffixs"].(string)
	// fileServerAddr := "http://localhost:6001/api/files/"
	fileServerAddr := this.TmpDataMap[iworkconst.STRING_PREFIX+"fileServerAddr"].(string)
	suffixs := strings.Split(suffixStr, ",")
	file_size := this.TmpDataMap[iworkconst.INT_PREFIX+"file_size"].(int64)
	tempFileName, fileName, tempFilePath, err := fileUpload.SaveFile(suffixs, file_size)
	if err != nil {
		if catchError := this.TmpDataMap[iworkconst.BOOL_PREFIX+"throwInsensitiveError?"].(string); catchError == "true" {
			panic(&interfaces.InsensitiveError{Error: errors.New(err.Error())})
		} else {
			panic(err)
		}
	}
	paramMap := map[string]interface{}{
		"fileName":       fileName,
		"tempFileName":   tempFileName,
		"fileExt":        path.Ext(fileName),
		"tempFilePath":   tempFilePath,
		"fileServerAddr": fileServerAddr,
		"duration":       fileutils.GetMP4FileDuration(tempFilePath),
	}
	if calHash := this.TmpDataMap[iworkconst.BOOL_PREFIX+"calHash?"].(string); calHash == "true" {
		hash, _ := hashutil.CalculateHashWithFile(tempFilePath)

		paramMap["hash"] = hash
		paramMap["handleSpecialHash"], _ = stringutil.ReplaceAllString(hash, "/", "-")
	}
	this.DataStore.CacheDatas(this.WorkStep.WorkStepName, paramMap)
}

func (this *DoReceiveFileNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.BOOL_PREFIX + "calHash?", "是否计算hash值"},
		2: {iworkconst.STRING_PREFIX + "suffixs", "上传文件支持的后缀名,*表示支持任意类型的后缀,多个后缀用逗号分隔"},
		3: {iworkconst.INT_PREFIX + "file_size", "上传文件大小"},
		4: {iworkconst.STRING_PREFIX + "fileServerAddr", "上传文件服务器访问路径"},
		5: {iworkconst.BOOL_PREFIX + "throwInsensitiveError?", "是否将可能出现的异常静默成脱敏后的异常？"},
	}
	choiceMap := map[string][]string{
		iworkconst.BOOL_PREFIX + "calHash?":               {"`true`", "`false`"},
		iworkconst.BOOL_PREFIX + "throwInsensitiveError?": {"`true`", "`false`"},
	}
	return this.BPIS1(paramMap, choiceMap)
}

func (this *DoReceiveFileNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{"fileName", "tempFileName", "fileExt", "tempFilePath", "fileServerAddr", "duration"})
}

func (this *DoReceiveFileNode) GetRuntimeParamOutputSchema() *iworkmodels.ParamOutputSchema {
	pos := &iworkmodels.ParamOutputSchema{}
	calHash := param.GetStaticParamValueWithStep(this.AppId, iworkconst.BOOL_PREFIX+"calHash?", this.WorkStep).(string)
	if calHash == "true" || calHash == "`true`" {
		pos.ParamOutputSchemaItems = append(pos.ParamOutputSchemaItems, iworkmodels.ParamOutputSchemaItem{
			ParamName: "hash",
		})
		pos.ParamOutputSchemaItems = append(pos.ParamOutputSchemaItems, iworkmodels.ParamOutputSchemaItem{
			ParamName: "handleSpecialHash",
		})
	}
	return pos
}

type DoResponseReceiveFileNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *DoResponseReceiveFileNode) Execute(trackingId string) {
	this.TmpDataMap["fileName"] = this.TmpDataMap["fileName"]
	this.TmpDataMap["fileServerPath"] = this.TmpDataMap["fileServerPath"]
	this.TmpDataMap["errorMsg"] = this.TmpDataMap["errorMsg?"]
	this.TmpDataMap["duration"] = this.TmpDataMap["duration?"]
	this.DataStore.CacheDatas(iworkconst.DO_RESPONSE_RECEIVE_FILE, map[string]interface{}{iworkconst.DO_RESPONSE_RECEIVE_FILE: this.TmpDataMap})
	// 直接提交 dataStore
	this.SubmitParamOutputSchemaDataToDataStore(this.WorkStep, this.DataStore, this.TmpDataMap)
}

func (this *DoResponseReceiveFileNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {"fileName", "最终的上传文件名称"},
		2: {"fileServerPath", "最终的服务器地址"},
		3: {"duration?", "视频时长"},
		4: {"errorMsg?", "异常信息"},
	}
	return this.BPIS1(paramMap)
}

func (this *DoResponseReceiveFileNode) GetDefaultParamOutputSchema() *iworkmodels.ParamOutputSchema {
	return this.BPOS1([]string{"fileName", "fileServerPath", "duration", "errorMsg"})
}
