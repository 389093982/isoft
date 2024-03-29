package iworkfunc

import (
	"errors"
	"fmt"
	"isoft/isoft_utils/common/stringutil"
	"regexp"
	"strings"
	"sync"
)

// 正则表达式 ~ 正则表达式对应的词语
var regexMap = map[string]string{
	"^[a-zA-Z0-9]+\\(": "func(",
	"^\\)":             ")",
	"^`.*?`":           "S",
	"^(-)*[0-9]+":      "N",
	"^\\$[a-zA-Z_0-9]+(\\.[a-zA-Z0-9\\-_]+)*": "V",
	"^,":                ",",
	"^;":                ";",
	"^[a-zA-Z0-9]+\\::": "name::",
}

var funcCallersMap sync.Map

// 将结果存储到 sync.Map 中去
func ParseToFuncCallers(expression string) ([]*FuncCaller, error) {
	// 先从缓存中获取
	if callers, ok := funcCallersMap.Load(expression); ok {
		return callers.([]*FuncCaller), nil
	}
	// 缓存中没有则实时解析获取
	callers, err := ParseFuncCallers(expression)
	if err == nil {
		// 存入缓存
		funcCallersMap.Store(expression, callers)
	}
	return callers, err
}

// 返回 uuid 和 funcCaller
func ParseFuncCallers(expression string) ([]*FuncCaller, error) {
	callers := make([]*FuncCaller, 0)
	for {
		if isUUIDFuncVar(expression) {
			break // 已经被提取完了
		}
		// 对 expression 表达式进行词法分析
		metas, lexers, err := analysisLexer(expression)
		if err != nil {
			return callers, err
		}
		// 提取优先级最高的 func
		caller, err := GetPriorityFuncCaller(strings.Join(metas, ""), strings.Join(lexers, ""))
		if err != nil { // 提取失败
			return callers, err
		}
		if caller == nil { // 未提取到 func
			if !isStringNumberOrVar(expression) {
				return nil, errors.New(fmt.Sprintf(`%s 词法解析失败,格式不正确!`, expression))
			}
			return nil, nil
		}
		// 填充 func 额外参数
		if _expression, err := fillFuncCallerExtra(metas, lexers, caller, expression); err != nil {
			return nil, err
		} else {
			callers = append(callers, caller)
			expression = _expression
		}
	}
	return callers, nil
}

// 填充 func 额外参数
func fillFuncCallerExtra(metas []string, lexers []string, caller *FuncCaller, expression string) (string, error) {
	// 函数左边部分
	funcLeft := metas[:lexerAt(lexers, caller.FuncLeftIndex)]
	// 函数右边部分
	funcRight := metas[lexerAt(lexers, caller.FuncRightIndex)+1:]
	// 函数部分
	funcArea := metas[lexerAt(lexers, caller.FuncLeftIndex) : lexerAt(lexers, caller.FuncRightIndex)+1]
	// 将 caller 函数替换成 $func.uuid,以便下一轮提取 func 使用
	expression = strings.Join(funcLeft, "") + "$func." + caller.FuncUUID + strings.Join(funcRight, "")
	// 去除函数名中的 (
	caller.FuncName = strings.Replace(funcArea[0], "(", "", -1)
	// 参数需要过滤掉 ,
	caller.FuncArgs = stringutil.RemoveItemFromSlice(funcArea[1:len(funcArea)-1], ",")
	for _, arg := range caller.FuncArgs {
		if !isStringNumberOrVar(arg) {
			return "", errors.New(fmt.Sprintf(`%s 词法解析失败,格式不正确!`, arg))
		}
	}
	return expression, nil
}

// 判断当前索引在整个 lexers 切片中的位置
func lexerAt(lexers []string, index int) int {
	// 统计总长度
	sumIndex := 0
	for _index, lexer := range lexers {
		if index >= sumIndex && index < sumIndex+len(lexer) {
			return _index
		}
		// 总长度增长
		sumIndex += len(lexer)
	}
	return -1
}

// 对字符串进行词法分析
func analysisLexer(s string) (metas []string, lexers []string, err error) {
	metas, lexers = make([]string, 0), make([]string, 0)
	// 不断地进行词法解析,直到解析完或者报错
	for {
		s = strings.TrimSpace(s)
		if s == "" {
			// 解析完
			return metas, lexers, nil
		}
		// 标识是否分析到一个词语
		flag := false
		for regex, lexer := range regexMap {
			reg := regexp.MustCompile(regex)
			if findStr := reg.FindString(s); findStr != "" { // 找到一个词语
				metas, lexers = append(metas, findStr), append(lexers, lexer)
				s = strings.Replace(s, findStr, "", 1)
				flag = true
				break
			}
		}
		// 解析报错
		if !flag {
			return metas, lexers, errors.New(fmt.Sprintf("%s 词法解析失败,格式不正确!", s))
		}
	}
}

// 根据词法分析并根据 ; 进行多值分割
func SplitWithLexerAnalysis(expression string) ([]string, error) {
	multiExpressions := make([]string, 0)
	metas, lexers, err := analysisLexer(expression)
	if err != nil { // 词法分析失败
		return nil, err
	}
	if !strings.Contains(expression, ";") { // 不包含 ; 表示单个值
		if strings.TrimSpace(expression) != "" {
			multiExpressions = append(multiExpressions, expression)
		}
		return multiExpressions, nil
	}
	for {
		hasSeparator := false
		for index, lexer := range lexers {
			if lexer == ";" {
				hasSeparator = true
				_expression := strings.TrimSpace(strings.Join(metas[:index], ""))
				if _expression != "" {
					multiExpressions = append(multiExpressions, _expression)
				}
				metas = metas[index+1:]
				lexers = lexers[index+1:]
				break
			}
		}
		if !hasSeparator {
			_expression := strings.Join(metas, "")
			if _expression != "" {
				multiExpressions = append(multiExpressions, _expression)
			} else {
				break
			}
		}
	}
	return multiExpressions, nil
}

func isUUIDFuncVar(s string) bool {
	if !strings.HasPrefix(s, "$func.") {
		return false
	}
	return len(stringutil.GetNoRepeatSubStringWithRegexp(s, "^\\$[a-zA-Z_0-9]+\\.[a-zA-Z0-9\\-]+$")) == 1
}

func isStringNumberOrVar(s string) bool {
	if _, lexers, err := analysisLexer(s); err == nil && len(lexers) == 1 {
		return true
	}
	return false
}
