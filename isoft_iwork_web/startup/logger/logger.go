package logger

import (
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
	"github.com/astaxie/beego/toolbox"
	"isoft/isoft_utils/common/apppath"
	"isoft/isoft_utils/common/fileutils"
	"os"
	"strconv"
	"time"
)

func init() {
	var logDir string
	if beego.BConfig.RunMode == "dev" || beego.BConfig.RunMode == "txyun" {
		logDir = "../../../isoft_iwork_web_log"
	} else {
		// 日志文件所在目录
		logDir = fileutils.ChangeToLinuxSeparator(apppath.GetAPPRootPath() + "/isoft_iwork_web_log")
	}
	if ok, _ := fileutils.PathExists(logDir); !ok {
		err := os.MkdirAll(logDir, os.ModePerm)
		if err != nil {
			fmt.Println(err.Error())
		}
	}

	// 控制台输出
	logs.SetLogger(logs.AdapterConsole)
	// 多文件输出
	logs.SetLogger(logs.AdapterMultiFile,
		`{"filename":"`+logDir+`/isoft_iwork_web.log","separate":["emergency", "alert", "critical", "error", "warning", "notice", "info", "debug"]}`)
	// 输出文件名和行号
	logs.EnableFuncCallDepth(true)
	// 异步输出日志
	logs.Async(1e3)

	//定时清理四张日志表
	//cronExpress := "0/10 * * * * *"  //每隔10秒执行
	cronExpress := "0 0 0 * * ?" //每天凌晨0时0分0秒执行
	clearLogTask := toolbox.NewTask("clearLogTask", cronExpress, ClearLog)
	toolbox.AddTask("clearLogTask", clearLogTask)
	toolbox.StartTask()
	logs.Info("clearLogTask started ...")

}

//定时清理四张日志表--每日凌晨0点执行
func ClearLog() error {
	//获取7天前的日期
	now := time.Now()
	preDays, _ := strconv.Atoi(beego.AppConfig.String("iwork.clearRunAndValidateLog.preDays"))
	oldTime := now.AddDate(0, 0, -preDays)

	logs.Info("开始清理日志表 runlog_detail") //只保留最近100w条
	orm.NewOrm().Raw("DELETE FROM runlog_detail WHERE id < (SELECT id FROM (SELECT id FROM runlog_detail ORDER BY id DESC LIMIT 1000000,1) tt)")

	logs.Info("开始清理日志表 validatelog_detail")
	filter := orm.NewOrm().QueryTable("validatelog_detail").Filter("last_updated_time__lt", oldTime)
	delCount, _ := filter.Delete()
	logs.Info("实际删除条数:" + strconv.Itoa(int(delCount)))

	logs.Info("开始清理日志表 validatelog_record")
	filter = orm.NewOrm().QueryTable("validatelog_record").Filter("last_updated_time__lt", oldTime)
	delCount, _ = filter.Delete()
	logs.Info("实际删除条数:" + strconv.Itoa(int(delCount)))
	return nil
}
