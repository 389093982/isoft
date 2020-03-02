package task

import (
	"fmt"
	"github.com/astaxie/beego/toolbox"
	"isoft/isoft_iwork_web/core/interfaces"
	"isoft/isoft_iwork_web/core/iworkcache"
	"isoft/isoft_iwork_web/core/iworkutil/errorutil"
	"isoft/isoft_iwork_web/core/logutil"
	"isoft/isoft_iwork_web/models"
	"isoft/isoft_iwork_web/startup/dipool/pool"
	"strings"
)

func init() {
	toolbox.StartTask()
}

func RefreshCronTask(app_id int64) {
	// 先删除旧的任务
	for k, _ := range toolbox.AdminTaskList {
		if strings.HasPrefix(k, fmt.Sprintf("%d-", app_id)) {
			toolbox.DeleteTask(k)
		}
	}
	// 再导入新的任务
	if metas, err := models.QueryAllCronMeta(app_id, true); err == nil {
		for _, meta := range metas {
			tk := toolbox.NewTask(fmt.Sprintf(`%d-%s`, app_id, meta.TaskName), meta.CronStr, func() error {
				job := &Job{
					meta: &meta,
				}
				return job.Run()
			})
			toolbox.AddTask(fmt.Sprintf(`%d-%s`, app_id, meta.TaskName), tk)
		}

	}
}

// 执行任务的 job
type Job struct {
	meta *models.CronMeta
}

// 运行 job 的方法
func (this *Job) Run() (err error) {
	logutil.Info("执行定时任务开始: %d - %s", this.meta.AppId, this.meta.TaskName)
	defer logutil.Info("执行定时任务结束: %d - %s", this.meta.AppId, this.meta.TaskName)
	defer func() {
		if err1 := recover(); err1 != nil {
			err = errorutil.ToError(err)
		}
	}()
	workCache, err := iworkcache.GetWorkCacheWithName(this.meta.AppId, this.meta.TaskName)
	if err != nil {
		return err
	}

	iworkRunImpl := pool.Container.GetSingleton("iworkRunImpl").(interfaces.IworkRun)
	return iworkRunImpl.RunWork(workCache)
}
