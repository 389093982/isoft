package controllers

import (
	"fmt"
	"github.com/astaxie/beego/context"
	setutil "github.com/deckarep/golang-set"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/entry"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkrun"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"isoft/isoft_utils/common/stringutil"
	"net/http"
	"path"
	"sync"
	"time"
)

func GenerateErrorMap(err interface{}) map[string]interface{} {
	return map[string]interface{}{
		"status":   "ERROR",
		"errorMsg": errorutil.ToError(err).Error(),
	}
}

// 示例地址: http://localhost:6001/api/iwork/httpservice/isoft_linkknown_api/test_iblog_table_migrate?author0=admin1234567
func (this *WorkController) PublishSerivce() {
	defer func() {
		if err := recover(); err != nil {
			this.Data["json"] = GenerateErrorMap(err)
			// 此处不用 defer 是为了性能
			this.ServeJSON()
		}
	}()
	app_name := this.Ctx.Input.Param(":app_name")
	work_name := this.Ctx.Input.Param(":work_name")
	// A-Z: 65-90
	if work_name[0] < 65 || work_name[0] > 90 {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": "The api interface is not accessible!"}
		this.ServeJSON()
		return
	}
	appId, _ := memory.GetAppIdWithCache(-1, app_name)
	workCache, err := iworkcache.GetWorkCacheWithName(appId.Id, work_name)
	checkError(err)
	receiver, trackingId := this.getReceiverFromRunOrMemory(workCache)
	if receiver != nil {
		receiver.TmpDataMap[iworkconst.TRACKING_ID] = trackingId
		this.ResponseUploadFile(receiver)
		this.Data["json"] = &receiver.TmpDataMap
	} else {
		// 没有 receiver,即没有返回 trackingId,一定代表执行失败(panic)
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", iworkconst.TRACKING_ID: trackingId, "errorMsg": "Empty Response"}
	}
	this.ServeJSON()
}

// 运行 work 或者从缓存中获取 receiver
func (this *WorkController) getReceiverFromRunOrMemory(workCache *iworkcache.WorkCache) (receiver *entry.Receiver, trackingId string) {
	// 获取请求参数
	mapData := ParseParam(workCache, this.Ctx, workCache.Steps[0])
	// 传递 request 对象
	mapData[iworkconst.HTTP_REQUEST_OBJECT] = this.Ctx.Request
	// 传递文件上传对象
	mapData[iworkconst.HTTP_REQUEST] = this

	chacheKey := fmt.Sprintf("%v%v", workCache.WorkId, mapData)

	if workCache.Work.CacheResult && memory.CacheEngine != nil && memory.CacheEngine.IsExist(chacheKey) {
		receiver = memory.CacheEngine.Get(chacheKey).(*entry.Receiver)
		return
	}
	trackingId, receiver = iworkrun.RunOneWork(workCache.WorkId, &entry.Dispatcher{TmpDataMap: mapData})
	if workCache.Work.CacheResult && memory.CacheEngine != nil {
		// 将影响数据进行进行缓存,需要放置并发(理论上需要加锁)
		memory.CacheEngine.Put(chacheKey, receiver, 60*10*time.Second)
	}
	return receiver, trackingId
}

func (this *WorkController) ResponseUploadFile(receiver *entry.Receiver) {
	tempDataMap := receiver.TmpDataMap
	if data, ok := tempDataMap[iworkconst.DO_RESPONSE_RECEIVE_FILE]; ok {
		tmpDataMap := data.(map[string]interface{})
		receiver.TmpDataMap["fileName"] = tmpDataMap["fileName"].(string) // 将临时文件的数据刷新成正式数据
		receiver.TmpDataMap["fileServerPath"] = tmpDataMap["fileServerPath"].(string)
		receiver.TmpDataMap["duration"] = tmpDataMap["duration"].(int64)
		receiver.TmpDataMap["fileSize"] = tmpDataMap["fileSize"].(int64)
		receiver.TmpDataMap["status"] = "SUCCESS"
		if errorMsg, ok := tmpDataMap["errorMsg?"].(string); ok {
			receiver.TmpDataMap["errorMsg"] = errorMsg
			receiver.TmpDataMap["status"] = "ERROR"
		}
		delete(receiver.TmpDataMap, iworkconst.DO_RESPONSE_RECEIVE_FILE)
	}
}

func checkError(err error) {
	if err != nil {
		panic(err)
	}
}

func (this *WorkController) LoadRecordParamData() {
	work_id, _ := this.GetInt64("work_id", -1)
	if recordParams, ok := RecordParamMap.Load(work_id); ok {
		recordParamArr := recordParams.([]interface{})
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "record_param_fields": recordParamArr}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": "no recordParams was found!"}
	}
	this.ServeJSON()
}

var RecordParamMap sync.Map

func ParseParam(workCache *iworkcache.WorkCache, ctx *context.Context, step models.WorkStep) map[string]interface{} {
	// 识别并记录获取到的请求参数
	recordParams, _ := RecordParamMap.LoadOrStore(workCache.WorkId, []interface{}{})
	recordParamArr := recordParams.([]interface{})
	ctx.Request.ParseForm()
	for k, _ := range ctx.Request.Form {
		recordParamArr = append(recordParamArr, k)
	}
	RecordParamMap.Store(workCache.WorkId, setutil.NewSet(recordParamArr...).ToSlice())

	mapData := make(map[string]interface{}, 0)
	if step.WorkStepType == iworkconst.NODE_TYPE_WORK_START {
		inputSchema := node.GetCacheParamInputSchema(&step)
		for _, item := range inputSchema.ParamInputSchemaItems {
			// 默认参数类型都当成 string 类型
			mapData[item.ParamName] = ctx.Input.Query(item.ParamName) // 传递参数允许为空串
		}
	}
	return mapData
}

func (this *WorkController) GetWriter() http.ResponseWriter {
	return this.Ctx.ResponseWriter
}

func (this *WorkController) WriteResponseHeader(key, value string) {
	this.Ctx.ResponseWriter.Header().Add(key, value)
}

func (this *WorkController) SaveFile(suffixs []string, file_size int64) (tempFileName, fileName, tempFilePath string, fileSize int64, err error) {
	defer func() {
		if err1 := recover(); err1 != nil {
			err = errorutil.ToError(err1)
		}
	}()
	// 判断是否是文件上传
	f, h, err := this.GetFile("file")
	checkError(err)
	if !stringutil.AnyOf("*", suffixs) && !stringutil.AnyOf(path.Ext(h.Filename), suffixs) {
		panic("文件后缀不满足要求!")
	}
	if h.Size > file_size {
		panic("文件大小超过上传限制!")
	}
	//得到文件实际大小
	fileSize = h.Size
	//关闭上传的文件，不然的话会出现临时文件不能清除的情况
	defer f.Close()
	tempFileName = stringutil.RandomUUID() + path.Ext(h.Filename)
	//得到文件的名称
	fileName = h.Filename
	//保存文件到指定的位置,static/uploadfile,这个是文件的地址,第一个static前面不要有/
	tempFilePath = path.Join(sysconfig.FileSavePath, tempFileName)
	err = this.SaveToFile("file", tempFilePath)
	checkError(err)
	return tempFileName, fileName, tempFilePath, fileSize, nil
}
