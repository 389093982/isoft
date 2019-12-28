package node

import (
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/datastore"
	"isoft/isoft_iwork_web/core/iworkdata/entry"
	"isoft/isoft_iwork_web/core/iworklog"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_utils/common/stringutil"
	"strings"
)

type BlockStepOrdersRunner struct {
	ParentStepId int64
	WorkCache    *iworkcache.WorkCache
	TrackingId   string
	LogWriter    *iworklog.CacheLoggerWriter
	Store        *datastore.DataStore
	Dispatcher   *entry.Dispatcher
	RunOneStep   interfaces.RunOneStep
}

func (this *BlockStepOrdersRunner) recordLog(err interface{}) {
	var (
		_err         interface{}
		workStepName string
		errorMsg     string
	)
	if wsError, ok := err.(interfaces.WorkStepError); ok {
		_err = wsError.Err
		workStepName = wsError.WorkStepName
	} else {
		_err = err
	}

	errorMsg = strings.Join([]string{
		"~~~~~~~~~~~~~~~~~~~~~~~~ internal error trace stack ~~~~~~~~~~~~~~~~~~~~~~~~~~",
		errorutil.PanicTraceForHtml(4), // 记录 4 kb大小的堆栈信息
		errorutil.FormatInternalError(_err),
		"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",
	},
		"<br/>")
	this.LogWriter.Write(this.TrackingId, workStepName, iworkconst.LOG_LEVEL_ERROR, errorMsg)
}

func (this *BlockStepOrdersRunner) Run() (receiver *entry.Receiver) {
	defer func() {
		if err := recover(); err != nil {
			this.recordLog(err)
			panic(err)
		}
	}()
	return this.runDetail()
}

func (this *BlockStepOrdersRunner) runDetail() (receiver *entry.Receiver) {
	// 存储前置步骤 afterJudgeInterrupt 属性
	afterJudgeInterrupt := false
	for _, blockStep := range this.WorkCache.BlockStepOrdersMap[this.ParentStepId] {
		// empty 节点就没必要执行了,后面计划不支持 empty 节点
		if blockStep.Step.WorkStepType == "empty" {
			continue
		}

		args := &interfaces.RunOneStepArgs{
			TrackingId: this.TrackingId,
			Logwriter:  this.LogWriter,
			BlockStep:  blockStep,
			Datastore:  this.Store,
			Dispatcher: this.Dispatcher,
			WorkCache:  this.WorkCache,
		}
		// 遇到 if 必定可以执行
		if blockStep.Step.WorkStepType == "if" {
			receiver = this.RunOneStep(args)
			// 支持完成后当前 blockStep 会获得 AfterJudgeInterrupt 属性
			afterJudgeInterrupt = blockStep.AfterJudgeInterrupt
		} else if stringutil.CheckContains(blockStep.Step.WorkStepType, []string{"elif", "else"}) { // 遇到 elif 和 else
			if !afterJudgeInterrupt {
				receiver = this.RunOneStep(args)
				afterJudgeInterrupt = blockStep.AfterJudgeInterrupt
			}
		} else { // 非 if、elif、else 节点必定执行
			receiver = this.RunOneStep(args)
			afterJudgeInterrupt = false // 不阻断后续步骤执行
		}
	}
	return receiver
}
