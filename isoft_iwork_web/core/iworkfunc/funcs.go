package iworkfunc

import (
	"fmt"
	"github.com/pkg/errors"
	"isoft/isoft_iwork_web/core/iworkutil/datatypeutil"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_utils/common/chiperutil"
	"isoft/isoft_utils/common/stringutil"
	"math/rand"
	"net/url"
	"path"
	"path/filepath"
	"reflect"
	"strconv"
	"strings"
	"time"
)

var callers []map[string]string

func init() {
	fproxy := &IWorkFuncProxy{}
	callers = fproxy.GetFuncCallers()
}

func CheckFuncNameValid(funcName string) bool {
	for _, caller := range callers {
		if funcDemo := caller["funcDemo"]; strings.HasPrefix(funcDemo, funcName+"(") {
			return true
		}
	}
	return false
}

type IWorkFuncProxy struct{}

func (t *IWorkFuncProxy) GetFuncCallers() []map[string]string {
	return []map[string]string{
		{"group": "string", "funcDemo": "stringsEq($str1,$str2)", "funcDesc": "字符串相等比较"},
		{"group": "string", "funcDemo": "stringsNotEq($str1,$str2)", "funcDesc": "字符串不相等比较"},
		{"group": "string", "funcDemo": "stringsToUpper($str)", "funcDesc": "字符串转大写函数"},
		{"group": "string", "funcDemo": "stringsToLower($str)", "funcDesc": "字符串转小写函数"},
		{"group": "string", "funcDemo": "stringsJoin($str1,$str2)", "funcDesc": "字符串拼接函数"},
		{"group": "string", "funcDemo": "stringsJoinWithSep($str1,$str2)", "funcDesc": "字符串拼接函数"},
		{"group": "string", "funcDemo": "stringsContains($str1,$str2)", "funcDesc": "字符串包含函数"},
		{"group": "string", "funcDemo": "stringsHasPrefix($str1,$str2)", "funcDesc": "字符串前缀判断函数"},
		{"group": "string", "funcDemo": "stringsHasSuffix($str1,$str2)", "funcDesc": "字符串后缀判断函数"},
		{"group": "string", "funcDemo": "stringsTrimSuffix($str1,$suffix)", "funcDesc": "字符串去除后缀"},
		{"group": "string", "funcDemo": "stringsTrimPrefix($str1,$prefix)", "funcDesc": "字符串去除前缀"},
		{"group": "string", "funcDemo": "stringsSplit($str1, $sep)", "funcDesc": "根据分隔符分割字符串"},
		{"group": "string", "funcDemo": "stringsRepeatQuestion($count)", "funcDesc": "'?,'重复 count 次,最后一次不带,"},
		{"group": "string", "funcDemo": "stringsRepeat($str,$count)", "funcDesc": "字符串 str 重复 count 次"},
		{"group": "string", "funcDemo": "stringsRepeatWithSep($str,$sep,$count)", "funcDesc": "字符串 str 重复 count 次，用 sep 进行分割"},

		{"group": "math", "funcDemo": "int64Gt($int1,$int2)", "funcDesc": "判断数字1是否大于数字2"},
		{"group": "math", "funcDemo": "int64Lt($int1,$int2)", "funcDesc": "判断数字1是否小于数字2"},
		{"group": "math", "funcDemo": "int64Eq($int1,$int2)", "funcDesc": "判断数字1是否等于数字2"},
		{"group": "math", "funcDemo": "int64NotEq($int1,$int2)", "funcDesc": "判断数字1是否不等于数字2"},
		{"group": "math", "funcDemo": "int64Add($int1,$int2)", "funcDesc": "数字相加函数"},
		{"group": "math", "funcDemo": "int64Sub($int1,$int2)", "funcDesc": "数字相减函数"},
		{"group": "math", "funcDemo": "int64Multi($int1,$int2)", "funcDesc": "数字相乘函数"},

		{"group": "url", "funcDemo": "getFileNameFromUrl($url)", "funcDesc": "根据 url 地址获取文件名, egg 格式 http://www.linkknown.com/files/helloworld.jpg"},

		{"group": "default", "funcDemo": "stringsOneOf($str1,$str2,$checkStr)", "funcDesc": "判断字符串 checkStr 是否存在于字符数组中"},

		{"group": "default", "funcDemo": "and($bool1,$bool2)", "funcDesc": "判断bool1和bool2同时满足"},
		{"group": "default", "funcDemo": "or($bool,$bool2)", "funcDesc": "判断bool1和bool2只要一个满足即可"},
		{"group": "default", "funcDemo": "not($bool)", "funcDesc": "bool值取反"},
		{"group": "default", "funcDemo": "uuid()", "funcDesc": "生成随机UUID信息"},
		{"group": "default", "funcDemo": "isEmpty($var)", "funcDesc": "判断变量或者字符串是否为空"},
		{"group": "default", "funcDemo": "isNotEmpty($var)", "funcDesc": "判断变量或者字符串是否非空"},
		{"group": "default", "funcDemo": "getDirPath($filepath)", "funcDesc": "获取当前文件父级目录的绝对路径"},
		{"group": "default", "funcDemo": "pathJoin($path1,$path2)", "funcDesc": "文件路径拼接"},
		{"group": "default", "funcDemo": "ifThenElse($condition,$var1,$var2)", "funcDesc": "三目运算符,条件满足返回$var1,不满足返回$var2"},
		{"group": "default", "funcDemo": "getRequestParameter($url,$paramName)", "funcDesc": "从url地址中根据参数名获取参数值"},
		{"group": "default", "funcDemo": "getRequestParameters($url,$paramName)", "funcDesc": "从url地址中根据参数名获取参数值,返回的是数组"},
		{"group": "default", "funcDemo": "getDomain($url)", "funcDesc": "从url地址中获取 domain 信息"},
		{"group": "default", "funcDemo": "getNotEmpty($var1,$var2)", "funcDesc": "从参数列表中获取第一个非空值"},
		{"group": "default", "funcDemo": "fmtSprintf($formatStr,$var)", "funcDesc": "字符串格式化操作,如 fmt.Sprintf(`%03d`, a)"},
		{"group": "default", "funcDemo": "formatNowTimeToYYYYMMDD()", "funcDesc": "当前日期格式化成 YYYYMMSS 格式"},
		{"group": "default", "funcDemo": "parseTimestampStrToDate($str)", "funcDesc": "将字符串转行成日期"},
		{"group": "default", "funcDemo": "bcryptGenerateFromPassword($password)", "funcDesc": "对密码进行加密,密码对比时需要使用 bcryptCompareHashAndPassword 进行对比"},
		{"group": "default", "funcDemo": "bcryptCompareHashAndPassword($hashedPassword, $password)", "funcDesc": "密码对比,密文密码($hashedPassword)和明文($password)对比,返回是否相等"},
		{"group": "default", "funcDemo": "generateMap($key1, $value1, $key2, $value2)", "funcDesc": "产生 map 对象"},
		{"group": "default", "funcDemo": "aesEncrypt($origData, $key)", "funcDesc": "aes 加密算法,用于生成密文密码,origData为明文,key为密钥(秘钥字符串长度必须是16/24/32),返回值为密文"},
		{"group": "default", "funcDemo": "aesDecrypt($crypted, $key)", "funcDesc": "aes 解密算法,用于解密密文密码,crypted为密文,key为密钥(秘钥字符串长度必须是16/24/32),返回值为明文"},
		{"group": "default", "funcDemo": "randNumberString($len)", "funcDesc": "生成指定长度的随机数"},
		{"group": "default", "funcDemo": "sliceLen($slices)", "funcDesc": "计算数组长度"},
		{"group": "default", "funcDemo": "switchCase($bool1,$val1,$bool2,$val2,$bool3,$val3)", "funcDesc": "bool 条件满足时返回 val"},
		{"group": "default", "funcDemo": "true()", "funcDesc": "返回布尔 true 值"},
		{"group": "default", "funcDemo": "false()", "funcDesc": "返回布尔 false 值"},
		{"group": "default", "funcDemo": "int64($str)", "funcDesc": "将支持转为 int64"},
		{"group": "default", "funcDemo": "getByteSizeForMB($int64)", "funcDesc": "产生指定大小 MB 的字节数"},
		{"group": "default", "funcDemo": "batchSqlBinding($varOrSlice1,$varOrSlice2,$varOrSlice3)", "funcDesc": "批量插入参数准备"},

		{"group": "sql", "funcDemo": "BATCH[$values]", "funcDesc": "批量插入值"},
		{"group": "sql", "funcDemo": "__AND__", "funcDesc": "动态识别 and 连接"},
	}
}

func (t *IWorkFuncProxy) GetFileNameFromUrl(args []interface{}) interface{} {
	sli := strings.Split(strings.TrimSpace(args[0].(string)), "/")
	fileName := sli[len(sli)-1]
	if fileName != "" {
		return fileName
	}
	panic(errors.New(fmt.Sprintf("[url=%s] 未找到有效的文件名", args[0].(string))))
}

func (t *IWorkFuncProxy) True(args []interface{}) interface{} {
	return true
}

func (t *IWorkFuncProxy) False(args []interface{}) interface{} {
	return false
}

func (t *IWorkFuncProxy) RandNumberString(args []interface{}) interface{} {
	arr := parseArgsToInt64Arr(args)
	width := arr[0]
	numeric := [10]byte{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
	r := len(numeric)
	rand.Seed(time.Now().UnixNano())

	var sb strings.Builder
	for i := 0; i < int(width); i++ {
		fmt.Fprintf(&sb, "%d", numeric[rand.Intn(r)])
	}
	return sb.String()
}

func (t *IWorkFuncProxy) GenerateMap(args []interface{}) interface{} {
	if len(args)%2 == 0 {
		m := make(map[string]interface{}, 0)
		for index, _ := range args {
			if index%2 == 0 {
				m[args[index].(string)] = args[index+1]
			}
		}
		return m
	} else {
		panic("GenerateMap args length is mismatch!")
	}
}

func (t *IWorkFuncProxy) AesEncrypt(args []interface{}) interface{} {
	return chiperutil.AesEncryptToStr(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) AesDecrypt(args []interface{}) interface{} {
	return chiperutil.AesDecryptToStr(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) BcryptGenerateFromPassword(args []interface{}) interface{} {
	hashedPassword, err := chiperutil.BcryptGenerateFromPassword(args[0].(string))
	errorutil.CheckError(err)
	return hashedPassword
}

func (t *IWorkFuncProxy) BcryptCompareHashAndPassword(args []interface{}) interface{} {
	err := chiperutil.BcryptCompareHashAndPassword(args[0].(string), args[1].(string))
	return err == nil
}

func (t *IWorkFuncProxy) SliceLen(args []interface{}) interface{} {
	return reflect.ValueOf(args[0]).Len()
}

func (t *IWorkFuncProxy) FormatNowTimeToYYYYMMDD(args []interface{}) interface{} {
	return time.Now().Format("20060102")
}

func (t *IWorkFuncProxy) ParseTimestampStrToDate(args []interface{}) interface{} {
	timestamp := args[0].(string)
	ts, err := strconv.ParseInt(timestamp, 10, 64)
	if err == nil {
		//时间戳 to 时间
		tm := time.Unix(ts/1e3, 0)
		return tm.Local()
	} else {
		panic(err)
	}
}

func (t *IWorkFuncProxy) FmtSprintf(args []interface{}) interface{} {
	return fmt.Sprintf(args[0].(string), args[1:]...)
}

func (t *IWorkFuncProxy) GetNotEmpty(args []interface{}) interface{} {
	for _, arg := range args {
		if argStr, ok := arg.(string); ok && argStr == "" {
			continue
		}
		if arg != nil {
			return arg
		}
	}
	return nil
}

func (t *IWorkFuncProxy) GetDomain(args []interface{}) interface{} {
	url := args[0].(string)
	if arr := strings.Split(url, "//"); len(arr) > 1 {
		return strings.Split(arr[1], "/")[0]
	}
	return ""
}

func (t *IWorkFuncProxy) GetRequestParameters(args []interface{}) interface{} {
	urlAddress := args[0].(string)
	paramName := args[1].(string)
	u, err := url.Parse(urlAddress)
	if err != nil {
		panic(err)
	}
	values, err := url.ParseQuery(u.RawQuery)
	if err != nil {
		panic(err)
	}
	return values[paramName]
}

func (t *IWorkFuncProxy) GetRequestParameter(args []interface{}) interface{} {
	return t.GetRequestParameters(args).([]string)[0]
}

func (t *IWorkFuncProxy) StringsOneOf(args []interface{}) interface{} {
	sargs := make([]string, 0)
	for _, arg := range args {
		sargs = append(sargs, arg.(string))
	}
	return stringutil.CheckContains(sargs[len(sargs)-1], sargs[:len(sargs)-1])
}

func (t *IWorkFuncProxy) StringsTrimPrefix(args []interface{}) interface{} {
	return strings.TrimPrefix(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsRepeatQuestion(args []interface{}) interface{} {
	s := strings.Repeat("?,", args[0].(int))
	if strings.HasSuffix(s, ",") {
		return strings.TrimSuffix(s, ",")
	}
	return s
}

func (t *IWorkFuncProxy) StringsRepeatWithSep(args []interface{}) interface{} {
	s := strings.Repeat(args[0].(string)+args[1].(string), args[2].(int))
	return s[:len(s)-1]
}

func (t *IWorkFuncProxy) StringsRepeat(args []interface{}) interface{} {
	s := strings.Repeat(args[0].(string), args[1].(int))
	return s
}

func (t *IWorkFuncProxy) StringsSplit(args []interface{}) interface{} {
	return strings.Split(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsTrimSuffix(args []interface{}) interface{} {
	return strings.TrimSuffix(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsEq(args []interface{}) interface{} {
	return args[0].(string) == args[1].(string)
}

func (t *IWorkFuncProxy) StringsNotEq(args []interface{}) interface{} {
	return args[0].(string) != args[1].(string)
}

func (t *IWorkFuncProxy) StringsContains(args []interface{}) interface{} {
	return strings.Contains(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsHasSuffix(args []interface{}) interface{} {
	return strings.HasSuffix(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsHasPrefix(args []interface{}) interface{} {
	return strings.HasPrefix(args[0].(string), args[1].(string))
}

func (t *IWorkFuncProxy) StringsToLower(args []interface{}) interface{} {
	return strings.ToLower(args[0].(string))
}

func (t *IWorkFuncProxy) StringsToUpper(args []interface{}) interface{} {
	return strings.ToUpper(args[0].(string))
}

func (t *IWorkFuncProxy) StringsJoin(args []interface{}) interface{} {
	sargs := make([]string, 0)
	for _, arg := range args {
		if arr, err := strconvToSlice(arg); err == nil {
			sargs = append(sargs, arr...)
		} else {
			panic(err)
		}
	}
	return strings.Join(sargs, "")
}

func strconvToSlice(s interface{}) ([]string, error) {
	result := make([]string, 0)
	if arr, ok := s.([]string); ok {
		for _, val := range arr {
			result = append(result, val)
		}
	} else if val, ok := s.(string); ok {
		result = append(result, val)
	} else {
		return nil, errors.New(fmt.Sprintf(`convert error, %s is not string or []string`, s))
	}
	return result, nil
}

func (t *IWorkFuncProxy) Int64Add(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Add", sargs, 2)
	return sargs[0] + sargs[1]
}

func (t *IWorkFuncProxy) Int64Sub(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Sub", sargs, 2)
	return sargs[0] - sargs[1]
}

func (t *IWorkFuncProxy) Int64Gt(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Gt", sargs, 2)
	return sargs[0] > sargs[1]
}

func (t *IWorkFuncProxy) Int64Lt(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Lt", sargs, 2)
	return sargs[0] < sargs[1]
}

func (t *IWorkFuncProxy) Int64Eq(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Eq", sargs, 2)
	return sargs[0] == sargs[1]
}

func (t *IWorkFuncProxy) Int64NotEq(args []interface{}) interface{} {
	return !(t.Int64Eq(args).(bool))
}

func (t *IWorkFuncProxy) Int64Multi(args []interface{}) interface{} {
	sargs := parseArgsToInt64Arr(args)
	checkArgsAmount("Int64Multi", sargs, 2)
	return sargs[0] * sargs[1]
}

func (t *IWorkFuncProxy) StringsJoinWithSep(args []interface{}) interface{} {
	sargs := make([]string, 0)
	for _, arg := range args {
		sargs = append(sargs, arg.(string))
	}
	return strings.Join(sargs[:len(args)-1], sargs[len(args)-1])
}

func (t *IWorkFuncProxy) Or(args []interface{}) interface{} {
	sargs := make([]bool, 0)
	for _, arg := range args {
		if arg == nil {
			sargs = append(sargs, false)
		} else {
			sargs = append(sargs, arg.(bool))
		}
	}
	return sargs[0] || sargs[1]
}

func (t *IWorkFuncProxy) And(args []interface{}) interface{} {
	sargs := make([]bool, 0)
	for _, arg := range args {
		if arg == nil {
			sargs = append(sargs, false)
		} else {
			sargs = append(sargs, arg.(bool))
		}
	}
	return sargs[0] && sargs[1]
}

func (t *IWorkFuncProxy) Not(args []interface{}) interface{} {
	sargs := make([]bool, 0)
	for _, arg := range args {
		if arg == nil {
			sargs = append(sargs, false)
		} else {
			sargs = append(sargs, arg.(bool))
		}
	}
	return !sargs[0]
}

func (t *IWorkFuncProxy) Uuid(args []interface{}) interface{} {
	return stringutil.RandomUUID()
}

func (t *IWorkFuncProxy) IsEmpty(args []interface{}) interface{} {
	if val, ok := args[0].(string); ok {
		return val == ""
	}
	return args[0] == nil
}

func (t *IWorkFuncProxy) IsNotEmpty(args []interface{}) interface{} {
	return !t.IsEmpty(args).(bool)
}

func (t *IWorkFuncProxy) PathJoin(args []interface{}) string {
	sli := make([]string, 0)
	for _, arg := range args {
		sli = append(sli, arg.(string))
	}
	return path.Join(sli...)
}

func (t *IWorkFuncProxy) GetDirPath(args []interface{}) string {
	return filepath.Dir(args[0].(string))
}

func (t *IWorkFuncProxy) IfThenElse(args []interface{}) interface{} {
	if args[0] == nil { // 参数为空条件为假
		return args[2]
	} else {
		if bol, ok := args[0].(bool); ok {
			if bol {
				return args[1] // bool 值且 true
			} else {
				return args[2] // bool 值且 false
			}
		} else { // 非空且不是 bool 值为真
			return args[1]
		}
	}
}

func checkArgsAmount(funcName string, sargs []int64, amount int) {
	if len(sargs) < amount {
		panic(errors.New(fmt.Sprintf(`%s 函数参数个数不足或者参数类型有误！`, funcName)))
	}
}

func parseArgsToInt64Arr(args []interface{}) []int64 {
	sargs := make([]int64, 0)
	for _, arg := range args {
		if _arg, ok := arg.(int64); ok {
			sargs = append(sargs, _arg)
		} else if _arg, ok := arg.(int); ok {
			sargs = append(sargs, int64(_arg))
		} else if _arg, ok := arg.(string); ok {
			if _arg, err := strconv.ParseInt(_arg, 10, 64); err == nil {
				sargs = append(sargs, _arg)
			}
		}
	}
	return sargs
}

func (t *IWorkFuncProxy) SwitchCase(args []interface{}) interface{} {
	if len(args)%2 != 0 {
		panic("参数格式必须是偶数个!")
	}
	for index, arg := range args {
		if index%2 == 0 {
			if checkFlag := arg.(bool); checkFlag && checkFlag == true {
				return args[index+1]
			}
		}
	}
	panic("所有条件都不满足")
}

func (t *IWorkFuncProxy) Int64(args []interface{}) interface{} {
	if _arg, ok := args[0].(int64); ok {
		return _arg
	} else if _arg, ok := args[0].(int); ok {
		return int64(_arg)
	} else if _arg, ok := args[0].(string); ok {
		if _arg, err := strconv.ParseInt(strings.TrimSpace(_arg), 10, 64); err == nil {
			return _arg
		}
	}
	panic(fmt.Sprintf("%v 转换成 int64 失败", args[0]))
}

func (t *IWorkFuncProxy) GetByteSizeForMB(args []interface{}) interface{} {
	if _arg, ok := args[0].(int64); ok {
		return 1024 * 1024 * _arg
	}
	panic("参数错误，需要 int64 类型的数字")
}

func (t *IWorkFuncProxy) BatchSqlBinding(args []interface{}) interface{} {
	maxLen := 0
	for _, arg := range args {
		ml := len(datatypeutil.InterfaceConvertToSlice(arg))
		if ml > maxLen {
			maxLen = ml
		}
	}
	ss := make([][]interface{}, 0)
	for _, arg := range args {
		ss = append(ss, padWithFirst(datatypeutil.InterfaceConvertToSlice(arg), maxLen))
	}
	return batchSqlBindingRender(ss, maxLen)
}

func padWithFirst(args []interface{}, maxLen int) []interface{} {
	s := make([]interface{}, 0)
	for i := 0; i < maxLen; i++ {
		s = append(s, args[0])
	}
	for i := 0; i < len(args); i++ {
		s[i] = args[i]
	}
	return s
}

func batchSqlBindingRender(args [][]interface{}, maxLen int) []interface{} {
	s := make([]interface{}, 0)
	for i := 0; i < maxLen; i++ {
		for index, _ := range args {
			s = append(s, args[index][i])
		}
	}
	return s
}
