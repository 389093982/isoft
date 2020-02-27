package framework

import (
	"github.com/pkg/errors"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkconst"
	"isoft/isoft_iwork_web/core/iworkdata/block"
	"isoft/isoft_iwork_web/core/iworkdata/entry"
	"isoft/isoft_iwork_web/core/iworkmodels"
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/models"
)

type PanicErrorNode struct {
	node.BaseNode
	WorkStep *models.WorkStep
}

func (this *PanicErrorNode) Execute(trackingId string) {
	expression := this.TmpDataMap[iworkconst.BOOL_PREFIX+"panic_expression"].(bool)
	if expression {
		errorMsg := this.TmpDataMap[iworkconst.STRING_PREFIX+"panic_errorMsg?"].(string)
		panic(&interfaces.InsensitiveError{Error: errors.New(errorMsg)})
	}
}

func isError(errorMsg string) bool {
	if errorMsg == "" {
		return false
	}
	return true
}

func (this *PanicErrorNode) GetDefaultParamInputSchema() *iworkmodels.ParamInputSchema {
	paramMap := map[int][]string{
		1: {iworkconst.BOOL_PREFIX + "panic_expression", "抛出异常的条件,值为 bool 类型!"},
		2: {iworkconst.STRING_PREFIX + "panic_errorMsg?", "抛出异常的信息,值为字符串类型!"},
	}
	return this.BPIS1(paramMap)
}

type CatchErrorNode struct {
	node.BaseNode
	BlockStep        *block.BlockStep
	WorkStep         *models.WorkStep
	BlockStepRunFunc func(args *interfaces.RunOneStepArgs) (receiver *entry.Receiver)
}

func (this *CatchErrorNode) Execute(trackingId string) {
	// 未捕获到异常,则不执行子流程
	if isNoError, ok := this.DataStore.DataNodeStoreMap["Error"].NodeOutputDataMap["isNoError"].(bool); ok && isNoError {
		return
	}
	if this.BlockStep.HasChildren {
		bsoRunner := node.BlockStepOrdersRunner{
			ParentStepId: this.WorkStep.WorkStepId,
			WorkCache:    this.WorkCache,
			TrackingId:   trackingId,
			LogWriter:    this.LogWriter,
			Store:        this.DataStore, // 获取数据中心
			Dispatcher:   this.Dispatcher,
			RunOneStep:   this.BlockStepRunFunc,
		}
		bsoRunner.Run()
	}
}
