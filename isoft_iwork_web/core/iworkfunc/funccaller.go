package iworkfunc

import (
	"fmt"
	"github.com/pkg/errors"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/core/logutil"
	"isoft/isoft_utils/common/stringutil"
	"strings"
)

// 函数执行类
type FuncCaller struct {
	FuncUUID       string // 函数唯一性 id
	FuncName       string // 函数实际名称
	FuncArgs       []string
	FuncLeftIndex  int // 函数整体在表达式中的左索引位置
	FuncRightIndex int // 函数整体在表达式中的右索引位置
}

// 获取优先级最高的函数执行体
// 含有 func( 必然有优先函数执行体
func GetPriorityFuncCaller(metasExpression, lexersExpression string) (*FuncCaller, error) {
	if !strings.Contains(lexersExpression, "func(") && !strings.Contains(lexersExpression, ")") {
		// 非函数类型表达式值
		return nil, nil
	}
	// 获取表达式中所有左括号的索引
	for _, leftBracketIndex := range GetAllLeftBracketIndex(lexersExpression) {
		// 判断表达式 expression 中左括号索引 leftBracketIndex 后面是否有直接右括号
		if bol, rightBracketIndex := CheckHasNearRightBracket(leftBracketIndex, lexersExpression); bol == true {
			return &FuncCaller{
				FuncUUID:       stringutil.RandomUUID(),
				FuncLeftIndex:  leftBracketIndex - len("func"),
				FuncRightIndex: rightBracketIndex,
			}, nil
		}
	}
	return nil, errors.New(fmt.Sprintf(`%s 语法解析失败,未找到有效的函数!`, metasExpression))
}

// 获取表达式中所有左括号的索引
func GetAllLeftBracketIndex(lexersExpression string) []int {
	leftBracketIndexs := make([]int, 0)
	for i := 0; i < len(lexersExpression); i++ {
		if lexersExpression[i] == '(' {
			leftBracketIndexs = append(leftBracketIndexs, i)
		}
	}
	return leftBracketIndexs
}

// 判断表达式 expression 中左括号索引 leftBracketIndex 后面是否有直接右括号
// 返回是否直接跟随右括号,以及右括号的索引位置
func CheckHasNearRightBracket(leftBracketIndex int, lexersExpression string) (bool, int) {
	for i := leftBracketIndex + 1; i < len(lexersExpression); i++ {
		if lexersExpression[i] == '(' {
			return false, -1
		} else if lexersExpression[i] == ')' {
			return true, i
		}
	}
	return false, -1
}

func ExecuteFuncCaller(caller *FuncCaller, args []interface{}) interface{} {
	defer func() {
		if err := recover(); err != nil {
			logutil.Error(string(errorutil.PanicTrace(4)))
			panic(errors.Wrap(err.(error), fmt.Sprintf(`caller.FuncName is %s, caller.FuncArgs %v`, caller.FuncName, caller.FuncArgs)))
		}
	}()
	// 将 funcName 首字母变成大写
	funcName := stringutil.ToUpperCaseFirstOne(caller.FuncName)
	// 执行函数
	return Invoke(funcName, args)
}

// 编码特殊字符, // 对转义字符 \, \; \( \) 等进行编码
func EncodeSpecialForParamVaule(paramVaule string) string {
	//paramVaule = strings.Replace(paramVaule, "\\\\n", "__newline__", -1)
	//paramVaule = strings.Replace(paramVaule, "\\(", "__leftBracket__", -1)
	//paramVaule = strings.Replace(paramVaule, "\\)", "__rightBracket__", -1)
	//paramVaule = strings.Replace(paramVaule, "\\,", "__comma__", -1)
	//paramVaule = strings.Replace(paramVaule, "\\;", "__semicolon__", -1)
	paramVaule = strings.Replace(paramVaule, "\\`", "__ENCODE_1__", -1)
	return paramVaule
}

// 解码特殊字符
func DncodeSpecialForParamVaule(paramVaule string) string {
	//paramVaule = strings.Replace(paramVaule, "__newline__", "\n", -1)
	//paramVaule = strings.Replace(paramVaule, "__leftBracket__", "(", -1)
	//paramVaule = strings.Replace(paramVaule, "__rightBracket__", ")", -1)
	//paramVaule = strings.Replace(paramVaule, "__comma__", ",", -1)
	//paramVaule = strings.Replace(paramVaule, "__semicolon__", ";", -1)
	paramVaule = strings.Replace(paramVaule, "__ENCODE_1__", "`", -1)
	return paramVaule
}
