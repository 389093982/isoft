package dbutil

import (
	"database/sql"
	"fmt"
	"net/url"
	"testing"
)

func Test_Connection(t *testing.T) {
	db, err := GetConnection("root", "123456", "193.112.162.61", 3306, "mysql")
	if err != nil {
		fmt.Println(fmt.Sprintf("connection failed, %s", err.Error()))
	} else {
		fmt.Println("connection success...")
	}

	defer db.Close()

	_, err = db.Exec("select * from ddd")
	if err != nil {
		fmt.Println(err.Error())
	} else {
		fmt.Println("successful...")
	}
}

func Test_Connection2(t *testing.T) {
	fmt.Println(url.QueryEscape("Asia/Shanghai"))
	db, err := sql.Open("mysql", "linker:Linker2020@tcp(127.0.0.1:3306)/isoft_linkknown?allowNativePasswords=true&charset=utf8&loc=Asia%2FShanghai")
	if err != nil {
		panic(err)
	}
	fmt.Println(db.Ping())
}
