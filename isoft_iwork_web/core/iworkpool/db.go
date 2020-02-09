package iworkpool

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql" //导入mysql驱动包
	"isoft/isoft_iwork_web/models"
	"sync"
)

var dbMap sync.Map

func LoadAndCachePool() {
	models.RegisterOpenConnFunc(OpenDBConn)
	// 初始化所有数据库连接
	resources := models.QueryAllResource(-1, "db")
	for _, resource := range resources {
		OpenDBConn("mysql", resource.ResourceDsn)
	}
}

func GetDBConn(driverName, dataSourceName string) (*sql.DB, error) {
	if val, ok := dbMap.Load(driverName + "_" + dataSourceName); ok {
		return val.(*sql.DB), nil
	}
	return OpenDBConn(driverName, dataSourceName)
}

func OpenDBConn(driverName, dataSourceName string) (db *sql.DB, err error) {
	if db, err = openConn(driverName, dataSourceName); err == nil {
		dbMap.Store(driverName+"_"+dataSourceName, db)
	}
	return
}

func openConn(driverName, dataSourceName string) (*sql.DB, error) {
	db, err := sql.Open(driverName, dataSourceName)
	if err == nil {
		err = db.Ping()
	}
	return db, err
}
