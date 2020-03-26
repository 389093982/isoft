package controllers

import (
	"encoding/xml"
	"fmt"
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
	"io/ioutil"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/core/iworkutil/fileutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/sysconfig"
	"isoft/isoft_utils/common/fileutils"
	"isoft/isoft_utils/common/xmlutil"
	"path"
	"path/filepath"
	"reflect"
	"runtime"
	"time"
)

var persistentDirPath string

func initPersistentDirPath() {
	if sysconfig.PERSISTENT_DIR != "" {
		if ok, _ := fileutils.PathExists(sysconfig.PERSISTENT_DIR); ok {
			persistentDirPath = sysconfig.PERSISTENT_DIR
			return
		}
	}
	// 获取 persistent 目录
	_, file, _, _ := runtime.Caller(0)
	persistentDirPath = fileutils.ChangeToLinuxSeparator(fmt.Sprintf("%s/persistent", filepath.Dir(filepath.Dir(file))))
}

func init() {
	initPersistentDirPath()
}

func persistentToFile() {
	logs.Info("==> 将 iwork 框架 DB 中的数据导入到 %s 目录下!", persistentDirPath)

	appids, _ := models.GetAllAppIds()
	// 进行备份操作
	fileutils.CopyDir(persistentDirPath, fmt.Sprintf(`%s_backup/%s`, persistentDirPath, time.Now().Format("20060102150405")))
	// 进行清理操作
	fileutils.RemoveFileOrDirectory(persistentDirPath)
	persistentAppIdsToFile(appids)
	persistentFiltersToFile(appids)
	persistentModulesToFile(appids)
	persistentGlobalVarsToFile(appids)
	persistentQuartzsToFile(appids)
	persistentResourcesToFile(appids)
	persistentMigratesToFile(appids)
	persistentWorkCahcesToFile(appids)
	persistentAuditTasksToFile(appids)
}

func getAppName(appids []models.AppId, app_id int64) string {
	for _, appid := range appids {
		if appid.Id == app_id {
			return appid.AppName
		}
	}
	return string(app_id)
}

func persistentAppIdsToFile(appids []models.AppId) {
	for _, appid := range appids {
		_persistentDirPath := path.Join(persistentDirPath, appid.AppName)
		filepath := path.Join(_persistentDirPath, "appid", fmt.Sprintf(`%s.appid`, appid.AppName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(appid))
	}
}

func persistentAuditTasksToFile(appids []models.AppId) {
	tasks, _ := models.QueryAllAuditTasks(orm.NewOrm())
	for _, task := range tasks {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, task.AppId))
		filepath := path.Join(_persistentDirPath, "audits", fmt.Sprintf(`%s.audit`, task.TaskName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(task))
	}
}

func persistentModulesToFile(appids []models.AppId) {
	modules, _ := models.QueryAllModules(-1)
	for _, module := range modules {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, module.AppId))
		filepath := path.Join(_persistentDirPath, "modules", fmt.Sprintf(`%s.module`, module.ModuleName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(module))
	}
}

func persistentFiltersToFile(appids []models.AppId) {
	filters, _ := models.QueryAllFilters(-1)
	for _, filter := range filters {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, filter.AppId))
		filepath := path.Join(_persistentDirPath, "filters", fmt.Sprintf(`%s.filter`, filter.FilterWorkName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(filter))
	}
}

func persistentGlobalVarsToFile(appids []models.AppId) {
	globalVars := models.QueryAllGlobalVar(-1)
	for _, globalVar := range globalVars {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, globalVar.AppId))
		filepath := path.Join(_persistentDirPath, "globalVars", fmt.Sprintf(`%s_%s.globalVar`, globalVar.Name, globalVar.EnvName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(globalVar))
	}
}

func persistentQuartzsToFile(appids []models.AppId) {
	metas, _ := models.QueryAllCronMeta(-1)
	for _, meta := range metas {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, meta.AppId))
		filepath := path.Join(_persistentDirPath, "quartzs", fmt.Sprintf(`%s.quartz`, meta.TaskName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(meta))
	}
}

func persistentResourcesToFile(appids []models.AppId) {
	resources := models.QueryAllResource(-1)
	for _, resource := range resources {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, resource.AppId))
		filepath := path.Join(_persistentDirPath, "resources", fmt.Sprintf(`%s.resource`, resource.ResourceName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(resource))
	}
}

func persistentMigratesToFile(appids []models.AppId) {
	migrates, _ := models.QueryAllSqlMigrate(-1)
	for _, migrate := range migrates {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, migrate.AppId))
		filepath := path.Join(_persistentDirPath, "migrates", fmt.Sprintf(`%s`, migrate.MigrateName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(migrate))
	}
}

func persistentWorkCahcesToFile(appids []models.AppId) {
	workCahces := iworkcache.GetAllWorkCache(-1)
	for _, workCahce := range workCahces {
		_persistentDirPath := path.Join(persistentDirPath, getAppName(appids, workCahce.Work.AppId))
		filepath := path.Join(_persistentDirPath, "works", fmt.Sprintf(`%s.work`, workCahce.Work.WorkName))
		logs.Info("存储文件 %s 完成!", filepath)
		fileutil.WriteFileSecurity(filepath, xmlutil.RenderToString(workCahce))
	}
}

// 长度过长可能会批量导入失败,需要进一步拆分
func persistentWorkFilesToDB(dirPath string) {
	defer logs.Info("导入目录 %s 完成!", dirPath)
	filepaths, _, _ := fileutils.GetAllSubFile(dirPath)
	var err error
	works := make([]models.Work, 0)
	workSteps := make([]models.WorkStep, 0)
	for _, filepath := range filepaths {
		works, workSteps = parseWorkFile(filepath, works, workSteps)
	}
	if len(works) > 0 {
		_, err = orm.NewOrm().InsertMulti(len(works), works)
		errorutil.CheckError(err)
	}
	if len(workSteps) > 0 {
		_, err = orm.NewOrm().InsertMulti(len(workSteps), workSteps)
		errorutil.CheckError(err)
	}
}

func parseWorkFile(filepath string, works []models.Work, workSteps []models.WorkStep) ([]models.Work, []models.WorkStep) {
	workCache := iworkcache.WorkCache{}
	bytes, _ := ioutil.ReadFile(filepath)
	err := xml.Unmarshal(bytes, &workCache)
	errorutil.CheckError(err)
	work := workCache.Work
	work.CreatedTime = time.Now()
	work.LastUpdatedTime = time.Now()
	works = append(works, work)
	errorutil.CheckError(err)
	for _, step := range workCache.Steps {
		step.CreatedTime = time.Now()
		step.LastUpdatedTime = time.Now()
		workSteps = append(workSteps, step)
	}
	return works, workSteps
}

func Insert(slice interface{}, pos int, value interface{}) interface{} {
	v := reflect.ValueOf(slice)
	appendSlice := reflect.MakeSlice(reflect.SliceOf(reflect.TypeOf(value)), 1, 1)
	appendSlice.Index(0).Set(reflect.ValueOf(value))
	v = reflect.AppendSlice(v.Slice(0, pos), reflect.AppendSlice(appendSlice, v.Slice(pos, v.Len())))
	return v.Interface()
}

func Append(slice interface{}, value interface{}) interface{} {
	v := reflect.ValueOf(slice)
	return Insert(slice, v.Len(), value)
}

// 批量插入 DB
func persistentMultiToDB(dirPath string, tp reflect.Type) {
	defer logs.Info("导入目录 %s 完成!", dirPath)
	filepaths, _, _ := fileutils.GetAllSubFile(dirPath)
	if filepaths == nil || len(filepaths) == 0 {
		return
	}
	// reflect.PtrTo 返回类型t的指针的类型
	// reflect.SliceOf 返回类型t的切片的类型
	rows := reflect.MakeSlice(reflect.SliceOf(reflect.PtrTo(tp)), 0, 0).Interface()
	for _, filepath := range filepaths {
		bytes, _ := ioutil.ReadFile(filepath)
		// reflect.New 返回一个Value类型值,该值持有一个指向类型为typ的新申请的零值的指针,返回值的Type为PtrTo(typ)
		val := reflect.New(tp).Interface()
		xml.Unmarshal(bytes, val)
		rows = Append(rows, val)
	}
	_, err := orm.NewOrm().InsertMulti(len(filepaths), rows)
	errorutil.CheckError(err)
}

var tableSli = []string{"app_id", "sql_migrate", "work", "work_step", "filters", "cron_meta", "resource", "module", "global_var", "audit_task"}

func truncateTables() {
	defer func() {
		if err := recover(); err != nil {
			logs.Info(errorutil.ToError(err).Error())
		}
	}()
	// 清空表
	for _, table := range tableSli {
		orm.NewOrm().Raw(fmt.Sprintf("truncate TABLE %s;", table)).Exec()
	}
}

func importProject() {
	logs.Info("==> 将 %s 目录下的项目导入的 iwork 框架 DB！", persistentDirPath)
	truncateTables()
	// 再导入每个 AppID 下面的数据
	if files, _, err := fileutils.GetAllSubFile(persistentDirPath); err == nil {
		for _, appNameFilePath := range files {
			persistentMultiToDB(fmt.Sprintf("%s/appid", appNameFilePath), reflect.TypeOf(models.AppId{}))
			persistentMultiToDB(fmt.Sprintf("%s/filters", appNameFilePath), reflect.TypeOf(models.Filters{}))
			persistentMultiToDB(fmt.Sprintf("%s/quartzs", appNameFilePath), reflect.TypeOf(models.CronMeta{}))
			persistentMultiToDB(fmt.Sprintf("%s/resources", appNameFilePath), reflect.TypeOf(models.Resource{}))
			persistentMultiToDB(fmt.Sprintf("%s/modules", appNameFilePath), reflect.TypeOf(models.Module{}))
			persistentMultiToDB(fmt.Sprintf("%s/globalVars", appNameFilePath), reflect.TypeOf(models.GlobalVar{}))
			persistentMultiToDB(fmt.Sprintf("%s/migrates", appNameFilePath), reflect.TypeOf(models.SqlMigrate{}))
			persistentMultiToDB(fmt.Sprintf("%s/audits", appNameFilePath), reflect.TypeOf(models.AuditTask{}))
			persistentWorkFilesToDB(fmt.Sprintf("%s/works", appNameFilePath))
		}
	}
}
