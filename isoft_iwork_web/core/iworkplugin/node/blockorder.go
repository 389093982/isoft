package node

import (
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/block"
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
			// 判断同级别块有没有 catch_error 节点捕获异常
			if catchBlockStep := this.getBlockStepByWorkStepType("catch_error"); catchBlockStep != nil {
				receiver = this.runDetail(catchBlockStep)
			} else {
				if this.Dispatcher.ExistParentWork {
					// 继续抛出异常到父级流程
					panic(err)
				} else {
					this.recordLog(err)
					// 重置 parentStepId,并执行 end 节点
					this.ParentStepId = iworkconst.PARENT_STEP_ID_FOR_START_END
					receiver = this.runDetail(this.getBlockStepByWorkStepType("work_end"))
				}
			}
		}
	}()
	return this.runDetail()
}

func (this *BlockStepOrdersRunner) getBlockStepByWorkStepType(work_step_type string) *block.BlockStep {
	for _, blockStep := range this.WorkCache.BlockStepOrdersMap[this.ParentStepId] {
		if blockStep.Step.WorkStepType == work_step_type {
			return blockStep
		}
	}
	return nil
}

// runDetail 用于运行一个块,当 blockSteps 不为空时表示运行该块的一个指定节点,该节点前的都不执行,节点后的还会执行
func (this *BlockStepOrdersRunner) runDetail(blockSteps ...*block.BlockStep) (receiver *entry.Receiver) {
	// end 节点异常暂不抛出
	if len(blockSteps) > 0 && blockSteps[0].Step.WorkStepType == "work_end" {
		defer func() {
			if err := recover(); err != nil {
				this.recordLog(err)
			}
		}()
	}
	// 存储前置步骤 afterJudgeInterrupt 属性
	afterJudgeInterrupt := false
	for _, blockStep := range this.WorkCache.BlockStepOrdersMap[this.ParentStepId] {
		// 当指定块中的某一个节点时,该节点之前的所有兄弟节点都不需要执行了
		if len(blockSteps) > 0 && blockStep.Step.WorkStepId < blockSteps[0].Step.WorkStepId {
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
		if blockStep.Step.WorkStepType == "if" { // 遇到 if 必定可以执行
			receiver = this.RunOneStep(args)
			afterJudgeInterrupt = blockStep.AfterJudgeInterrupt
		} else if stringutil.CheckContains(blockStep.Step.WorkStepType, []string{"elif", "else"}) { // 遇到 elif 和 else
			if !afterJudgeInterrupt {
				receiver = this.RunOneStep(args)
				afterJudgeInterrupt = blockStep.AfterJudgeInterrupt
			}
		} else { // 非 if、elif、else 节点必定执行
			receiver = this.RunOneStep(args)
			afterJudgeInterrupt = false
		}
	}
	return receiver
}
