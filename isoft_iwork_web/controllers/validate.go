package controllers

import (
	"encoding/json"
	"fmt"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkdata/block"
	"isoft/isoft_iwork_web/core/iworkfunc"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkutil"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/core/iworkvalid"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/service"
	"isoft/isoft_iwork_web/startup/memory"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"isoft/isoft_utils/common/stringutil"
	"strconv"
	"strings"
	"sync"
	"time"
)

// 加载校验信息,校验失败异常信息也要返回给 UI,如缓存中的 BlockStep 信息获取不到(校验异常)也要提示出来
func (this *WorkController) LoadValidateResult() {
	defer this.ServeJSON()
	defer func() {
		if err := recover(); err != nil {
			this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": errorutil.ToError(err).Error()}
		}
	}()
	serviceArgs := make(map[string]interface{}, 0)
	work_id, _ := this.GetInt64("work_id", -1)
	app_id, _ := this.GetInt64("app_id", -1)
	validateWorks(app_id, work_id) // 触发校验
	serviceArgs["work_id"] = work_id
	if result, err := service.ExecuteResultServiceWithTx(serviceArgs, service.LoadValidateResultService); err == nil {
		this.Data["json"] = &map[string]interface{}{"status": "SUCCESS", "details": result["details"]}
	} else {
		this.Data["json"] = &map[string]interface{}{"status": "ERROR", "errorMsg": err.Error()}
	}

}

func (this *WorkController) ValidateWork() {
	workId, _ := this.GetInt64("work_id", -1)
	app_id, _ := this.GetInt64("app_id", -1)
	validateWorks(app_id, workId) // 校验全部或者只校验单个 workId
	this.Data["json"] = &map[string]interface{}{"status": "SUCCESS"}
	this.ServeJSON()
}

func validateWorks(app_id, workId int64) {
	completeFlag := make(chan int) // 校验并记录日志完成标识
	trackingId := stringutil.RandomUUID()
	// 记录校验耗费时间
	defer recordCostTimeLog(trackingId, time.Now())
	// 待校验的所有 work 信息
	workMap := prepareValiateWorks(app_id, workId)
	// 记录日志
	recordValidatelogRecord(trackingId, workId)
	logCh := make(chan *models.ValidatelogDetail, 50) // 指定容量
	go func() {
		recordValidatelogDetails(logCh, trackingId, workMap) // 开协程保证读和写同时进行
		completeFlag <- 1
	}()
	var wg sync.WaitGroup
	for work, workSteps := range workMap {
		wg.Add(1)
		go func(work models.Work, workSteps []models.WorkStep) {
			validateWork(&work, workSteps, logCh, &wg)
		}(work, workSteps)
	}
	wg.Wait()
	// 所有 work 执行完成后关闭 logCh
	close(logCh)
	<-completeFlag

}

func prepareValiateWorks(app_id, workId int64) map[models.Work][]models.WorkStep {
	// 从缓存中读取
	dataMap := make(map[models.Work][]models.WorkStep, 0)
	if workId > 0 {
		workCache, err := iworkcache.GetWorkCache(workId)
		if err != nil {
			panic(err)
		}
		dataMap[workCache.Work] = workCache.Steps
	} else {
		workCaches := iworkcache.GetAllWorkCache(app_id)
		for _, workCache := range workCaches {
			dataMap[workCache.Work] = workCache.Steps
		}
	}
	return dataMap
}

// 校验单个 work
func validateWork(work *models.Work, steps []models.WorkStep, logCh chan *models.ValidatelogDetail, workWg *sync.WaitGroup) {
	defer workWg.Done()
	defer func(start time.Time) {
		logCh <- &models.ValidatelogDetail{
			WorkId:      work.Id,
			Detail:      fmt.Sprintf(`validate %s work cost %d ms!`, work.WorkName, time.Now().Sub(start).Nanoseconds()/1e6),
			SuccessFlag: true,
		}
	}(time.Now())

	// 验证流程必须以 work_start 开始,以 work_end 结束
	checkBeginAndEnd(steps, logCh, work)
	var wg sync.WaitGroup
	wg.Add(len(steps))
	for _, step := range steps {
		go func(step models.WorkStep) {
			validateStep(work, &step, logCh, &wg)
		}(step)
	}
	wg.Wait()
}

func checkBeginAndEnd(steps []models.WorkStep, logCh chan *models.ValidatelogDetail, work *models.Work) {
	checkError := func(step *models.WorkStep, workStepName string, errorMsg string) {
		if step.WorkStepType != workStepName {
			logCh <- &models.ValidatelogDetail{
				WorkId:     work.Id,
				WorkStepId: step.WorkStepId,
				Detail:     errorMsg,
			}
		}
	}
	if len(steps) > 0 {
		checkError(&steps[0], "work_start", "work must start with a work_start node!")
		checkError(&steps[len(steps)-1], "work_end", "work must end with a work_end node!")
	}
	return
}

func parseToValidatelogDetail(step *models.WorkStep, err interface{}) *models.ValidatelogDetail {
	var detail string
	if _, ok := err.(error); ok {
		detail = string(errorutil.PanicTrace(sysconfig.IWORK_PANICTRACE_SIZE))
	} else if _err, ok := err.(string); ok {
		detail = _err
	} else if _err, ok := err.(models.ValidatelogDetail); ok {
		detail = _err.Detail
	}
	return &models.ValidatelogDetail{
		WorkId:     step.WorkId,
		WorkStepId: step.WorkStepId,
		Detail:     detail,
	}
}

// 校验单个 step,并将校验不通过的信息放入 logCh 中
func validateStep(work *models.Work, step *models.WorkStep, logCh chan *models.ValidatelogDetail, stepWg *sync.WaitGroup) {
	defer stepWg.Done()
	defer func() {
		if err := recover(); err != nil {
			logCh <- parseToValidatelogDetail(step, errorutil.ToError(string(errorutil.PanicTrace(sysconfig.IWORK_PANICTRACE_SIZE))))
		}
	}()

	for _, checkResult := range getCheckResultsForStep(work, step) {
		var paramName, checkResultMsg string
		checkResultMap := make(map[string]string)
		if err := json.Unmarshal([]byte(checkResult), &checkResultMap); err == nil {
			paramName = checkResultMap["paramName"]
			checkResultMsg = checkResultMap["checkResultMsg"]
		} else {
			paramName = ""
			checkResultMsg = checkResult
		}

		logCh <- &models.ValidatelogDetail{
			WorkId:     step.WorkId,
			WorkStepId: step.WorkStepId,
			ParamName:  paramName,
			Detail:     checkResultMsg,
		}
	}
}

func getCheckResultsForStep(work *models.Work, step *models.WorkStep) (checkResult []string) {
	checkResult = make([]string, 0)
	checkResultCh := make(chan string, 10)
	wg := new(sync.WaitGroup)
	wg.Add(4)
	go func() {
		defer wg.Done()
		// 校验 step 中的参数是否为空
		for _, s := range iworkvalid.CheckEmpty(step, &node.ParamSchemaParser{WorkStep: step, ParamSchemaParser: &node.WorkStepFactory{WorkStep: step}}) {
			checkResultCh <- s
		}
	}()
	go func() {
		defer wg.Done()
		checkVariableRelationShip(work, step, checkResultCh)
	}()
	go func() {
		defer wg.Done()
		// 校验使用的函数名是否有效
		checkFuncNameInvalid(work, step, checkResultCh)
	}()
	go func() {
		defer wg.Done()
		// 定制化校验
		for _, s := range CheckCustom(work.AppId, step) {
			checkResultCh <- s
		}
	}()
	wg.Wait()
	close(checkResultCh)
	for s := range checkResultCh {
		checkResult = append(checkResult, s)
	}
	return
}

func CheckCustom(app_id int64, step *models.WorkStep) (checkResult []string) {
	defer func() {
		if err := recover(); err != nil {
			checkResult = append(checkResult,
				fmt.Sprintf("%s - %s", errorutil.ToError(err).Error(), string(errorutil.PanicTrace(sysconfig.IWORK_PANICTRACE_SIZE))))
		}
	}()
	factory := &node.WorkStepFactory{WorkStep: step}
	return factory.ValidateCustom(app_id)
}

// 校验变量的引用关系
func checkVariableRelationShip(work *models.Work, step *models.WorkStep, checkResultCh chan string) {
	defer func() {
		if err := recover(); err != nil {
			checkResultCh <- errorutil.ToError(err).Error()
		}
	}()
	inputSchema := node.GetCacheParamInputSchema(step)
	for _, item := range inputSchema.ParamInputSchemaItems {
		checkVariableRelationShipDetail(work.AppId, item, step.WorkId, step.WorkStepId, checkResultCh)
	}
	return
}

func checkFuncNameInvalid(work *models.Work, step *models.WorkStep, checkResultCh chan string) {
	defer func() {
		if err := recover(); err != nil {
			checkResultCh <- errorutil.ToError(err).Error()
		}
	}()
	inputSchema := node.GetCacheParamInputSchema(step)
	for _, item := range inputSchema.ParamInputSchemaItems {
		if !item.PureText && strings.TrimSpace(item.ParamValue) != "" {
			multiExpression, err := iworkfunc.SplitWithLexerAnalysis(item.ParamValue)
			if err != nil {
				checkResultCh <- fmt.Sprintf(`%s 格式有误！`, item.ParamName)
			}
			for _, expression := range multiExpression {
				callers, _ := iworkfunc.ParseToFuncCallers(expression)
				for _, caller := range callers {
					if !iworkfunc.CheckFuncNameValid(caller.FuncName) {
						checkResultCh <- fmt.Sprintf(`%s 无效的函数名！`, caller.FuncName)
					}
				}
			}
		}
	}
	return
}

func checkVariableRelationShipDetail(app_id int64, item *iworkmodels.ParamInputSchemaItem, work_id, work_step_id int64, checkResultCh chan string) {
	// 根据正则找到关联的节点名和字段名
	refers := iworkutil.GetRelativeValueWithReg(item.ParamValue)
	if len(refers) > 0 {
		preStepNodeNames := getAllPreStepNodeName(work_id, work_step_id)
		skipNodeNames := []string{"RESOURCE", "WORK", "Error"}
		for _, refer := range refers {
			referNodeName, referFiledName := parseReferNodeAndFiledName(refer)
			if stringutil.CheckContains(referNodeName, skipNodeNames) {
				continue
			}
			if referNodeName == "Global" {
				checkVariableRelationShipForGlobal(app_id, item.ParamName, referFiledName, checkResultCh)
			} else {
				checkVariableRelationShipForNode(item.ParamName, referNodeName, referFiledName, preStepNodeNames, checkResultCh, work_id)
			}
		}
	}
	return
}

func getAllPreStepNodeName(work_id, work_step_id int64) []string {
	workCache, _ := iworkcache.GetWorkCache(work_id)
	// 当前步骤信息
	var currentWorkStep models.WorkStep
	// 前置步骤
	var preSteps = make([]models.WorkStep, 0)
	// 所有步骤信息
	allSteps := workCache.Steps
	for _, step := range workCache.Steps {
		if step.WorkStepId == work_step_id {
			currentWorkStep = step
		}
		if step.WorkStepId < work_step_id {
			preSteps = append(preSteps, step)
		}
	}
	result := make([]string, 0)
	parser := block.BlockParser{Steps: allSteps}
	_, blockStepMapper := parser.ParseToBlockSteps()
	currentBlockStep := blockStepMapper[currentWorkStep.WorkStepId]
	for _, preStep := range preSteps {
		// 判断前置 preStep 在块范围内是否是可访问的
		if block.CheckBlockAccessble(currentBlockStep, preStep.WorkStepId) {
			result = append(result, preStep.WorkStepName)
		}
	}
	return result
}

func checkVariableRelationShipForGlobal(app_id int64, paramName, referFiledName string, checkResultCh chan string) {
	_, ok1 := memory.GlobalVarMap.Load(fmt.Sprintf("%d_%s_%s", app_id, referFiledName, sysconfig.ENV_ONUSE))
	_, ok2 := memory.GlobalVarMap.Load(fmt.Sprintf("%d_%s_%s", app_id, referFiledName, "dev"))
	if !ok1 && !ok2 {
		bytes, _ := json.Marshal(&map[string]string{
			"paramName":      paramName,
			"checkResultMsg": fmt.Sprintf("Invalid referFiledName relationship for %s was found!", referFiledName),
		})
		checkResultCh <- string(bytes)
	}
}

func checkVariableRelationShipForNode(paramName, referNodeName, referFiledName string, preStepNodeNames []string, checkResultCh chan string, work_id int64) {
	// 判断节点名称是否有效
	if !stringutil.CheckContains(referNodeName, preStepNodeNames) {
		bytes, _ := json.Marshal(&map[string]string{
			"paramName":      paramName,
			"checkResultMsg": fmt.Sprintf("Invalid referNodeName relationship for %s was found!", referNodeName),
		})
		checkResultCh <- string(bytes)
		return
	}
	// $NodeName;
	// 只引用节点名的空字段直接跳过
	if referFiledName == "" {
		return
	}
	checkVariableRelationShipForNodeDetail(work_id, referNodeName, referFiledName, checkResultCh)
}

func checkVariableRelationShipForNodeDetail(work_id int64, referNodeName, referFiledName string, checkResultCh chan string) {
	workCache, _ := iworkcache.GetWorkCache(work_id)
	var (
		referStepFlag bool
		referStep     models.WorkStep
	)
	for _, step := range workCache.Steps {
		if step.WorkStepName == referNodeName {
			referStepFlag, referStep = true, step
			break
		}
	}
	// 判断字段名称是否有效
	if referStepFlag {
		outputSchema := node.GetCacheParamOutputSchema(&referStep)
		exist := false
		for _, item := range outputSchema.ParamOutputSchemaItems {
			if item.ParamName == referFiledName || item.ParentPath+"."+item.ParamName == referFiledName {
				exist = true
				break
			}
		}
		if !exist {
			checkResultCh <- fmt.Sprintf("Invalid referFiledName relationship for %s was found!", referFiledName)
		}
	} else {
		checkResultCh <- fmt.Sprintf("Invalid referNodeName %s was found!", referNodeName)
	}
}

func parseReferNodeAndFiledName(refer string) (referNodeName, referFiledName string) {
	// 去除前后可能存在的 $ 和 ;
	refer = strings.TrimPrefix(refer, "$")
	refer = strings.TrimSuffix(refer, ";")
	if strings.Contains(refer, ".") {
		referNodeName = refer[:strings.Index(refer, ".")]
		referFiledName = refer[strings.Index(refer, ".")+1:]
	} else {
		referNodeName = refer
		referFiledName = ""
	}
	return referNodeName, referFiledName
}

// 从 logCh 中读取日志并记录
func recordValidatelogDetails(logCh chan *models.ValidatelogDetail, trackingId string, workMap map[models.Work][]models.WorkStep) {
	workCaches := make(map[string]models.Work, 0)
	workStepCaches := make(map[string]models.WorkStep, 0)
	for work, steps := range workMap {
		// 此处不能使用 string(int64) 这种转换,造成 bug,也不能使用指针 &work
		workCaches[strconv.FormatInt(work.Id, 10)] = work
		for _, step := range steps {
			workStepCaches[strconv.FormatInt(work.Id, 10)+"_"+strconv.FormatInt(step.WorkStepId, 10)] = step
		}
	}
	details := make([]*models.ValidatelogDetail, 0)
	// 从 logCh 中循环读取校验不通过的信息,并将其写入日志表中去
	for log := range logCh {
		workCacheKey, workStepCacheKey :=
			string(strconv.FormatInt(log.WorkId, 10)), strconv.FormatInt(log.WorkId, 10)+"_"+strconv.FormatInt(log.WorkStepId, 10)
		work, _ := workCaches[workCacheKey]
		step, _ := workStepCaches[workStepCacheKey]
		details = append(details, fillWorkValidatelogDetail(log, trackingId, &work, &step))
	}
	models.InsertMultiValidatelogDetail(details)
}

func fillWorkValidatelogDetail(log *models.ValidatelogDetail, trackingId string, work *models.Work, step *models.WorkStep) *models.ValidatelogDetail {
	log.TrackingId = trackingId
	log.WorkName = work.WorkName
	log.CreatedBy = "SYSTEM"
	log.LastUpdatedBy = "SYSTEM"
	log.CreatedTime = time.Now()
	log.LastUpdatedTime = time.Now()
	if step != nil { // 不一定含有 step
		log.WorkStepName = step.WorkStepName
	}
	return log
}

func recordValidatelogRecord(trackingId string, workId int64) (int64, error) {
	return models.InsertValidatelogRecord(&models.ValidatelogRecord{
		TrackingId:      trackingId,
		WorkId:          workId,
		CreatedBy:       "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedBy:   "SYSTEM",
		LastUpdatedTime: time.Now(),
	})
}

// 统计操作所花费的时间方法
func recordCostTimeLog(trackingId string, start time.Time) {
	detail := fmt.Sprintf("validate complete! total cost %d ms!", time.Now().Sub(start).Nanoseconds()/1e6)
	log := newValidatelogDetail(trackingId, detail)
	models.InsertMultiValidatelogDetail([]*models.ValidatelogDetail{log})
}

func newValidatelogDetail(trackingId, detail string) *models.ValidatelogDetail {
	return &models.ValidatelogDetail{
		TrackingId:      trackingId,
		SuccessFlag:     true,
		Detail:          detail,
		CreatedBy:       "SYSTEM",
		LastUpdatedBy:   "SYSTEM",
		CreatedTime:     time.Now(),
		LastUpdatedTime: time.Now(),
	}
}
