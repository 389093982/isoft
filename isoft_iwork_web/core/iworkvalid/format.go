package iworkvalid

import (
	"isoft/isoft_iwork_web/core/iworkfunc"
)

type ParamValueFormatChecker struct {
	ParamName  string
	PureText   bool
	ParamValue string
}

// 定义规则：函数中所有的值都必须是变量,不能是字符串
// 变量定义区域
func (this *ParamValueFormatChecker) Check() (bool, error) {
	if this.PureText {
		return true, nil
	}
	multiExpression, err := iworkfunc.SplitWithLexerAnalysis(this.ParamValue)
	if err != nil {
		return false, err
	}
	for _, expression := range multiExpression {
		callers, err := iworkfunc.ParseToFuncCallers(expression)
		if err != nil {
			return false, err
		}
		for _, caller := range callers {
			if !iworkfunc.CheckFuncNameValid(caller.FuncName) {
				return false, err
			}
		}
	}
	return true, nil
}
