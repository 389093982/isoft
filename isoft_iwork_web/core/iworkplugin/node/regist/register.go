package regist

import (
	"isoft/isoft_iwork_web/core/iworkplugin/node"
	"isoft/isoft_iwork_web/core/iworkplugin/node/bash"
	"isoft/isoft_iwork_web/core/iworkplugin/node/chiper"
	"isoft/isoft_iwork_web/core/iworkplugin/node/file"
	"isoft/isoft_iwork_web/core/iworkplugin/node/framework"
	"isoft/isoft_iwork_web/core/iworkplugin/node/http"
	"isoft/isoft_iwork_web/core/iworkplugin/node/json"
	"isoft/isoft_iwork_web/core/iworkplugin/node/mail"
	"isoft/isoft_iwork_web/core/iworkplugin/node/os"
	"isoft/isoft_iwork_web/core/iworkplugin/node/sql"
	"isoft/isoft_iwork_web/core/iworkplugin/node/zip"
	"reflect"
	"strings"
)

var typeMap = make(map[string]reflect.Type, 0)

func GetNodeMeta() []map[string]string {
	return []map[string]string{
		{"group": "sql", "name": "sql_query", "icon": "ios-cube-outline"},
		{"group": "sql", "name": "sql_execute", "icon": "ios-crop-outline"},
		{"group": "sql", "name": "db_parser", "icon": "ios-crop-outline"},

		{"group": "json", "name": "json_render", "icon": "ios-git-branch"},
		{"group": "json", "name": "json_parser", "icon": "ios-git-compare"},

		{"group": "http", "name": "http_request", "icon": "ios-globe-outline"},
		{"group": "http", "name": "http_request_parser", "icon": "ios-globe-outline"},
		{"group": "http", "name": "http_wirter", "icon": "ios-globe-outline"},
		{"group": "http", "name": "http_href_parser", "icon": "ios-ionitron-outline"},

		{"group": "file", "name": "file_read", "icon": "ios-book-outline"},
		{"group": "file", "name": "file_write", "icon": "ios-create-outline"},
		{"group": "file", "name": "file_delete", "icon": "ios-log-out"},
		{"group": "file", "name": "file_sync", "icon": "md-paper"},

		{"group": "mail", "name": "send_mail", "icon": "md-hammer"},

		{"group": "chiper", "name": "base64_encode", "icon": "ios-magnet"},
		{"group": "chiper", "name": "base64_decode", "icon": "ios-magnet-outline"},
		{"group": "chiper", "name": "create_jwt", "icon": "md-hammer"},
		{"group": "chiper", "name": "parse_jwt", "icon": "md-hammer"},
		{"group": "chiper", "name": "cal_hash", "icon": "ios-flower-outline"},

		{"group": "os", "name": "set_env", "icon": "ios-nuclear-outline"},
		{"group": "os", "name": "get_env", "icon": "ios-nuclear"},

		{"group": "text", "name": "template", "icon": "md-hammer"},

		{"group": "framework", "name": "work_start", "icon": "ios-arrow-dropright"},
		{"group": "framework", "name": "work_end", "icon": "ios-arrow-dropleft"},
		{"group": "framework", "name": "if", "icon": "md-code-working"},
		{"group": "framework", "name": "elif", "icon": "md-code-working"},
		{"group": "framework", "name": "else", "icon": "md-code-working"},
		{"group": "framework", "name": "empty", "icon": "ios-mail-open-outline"},
		{"group": "framework", "name": "work_sub", "icon": "logo-buffer"},
		{"group": "framework", "name": "mapper", "icon": "ios-infinite"},
		{"group": "framework", "name": "foreach", "icon": "md-hammer"},
		{"group": "framework", "name": "panic_error", "icon": "md-hammer"},
		{"group": "framework", "name": "catch_error", "icon": "md-hammer"},

		{"group": "default", "name": "do_receive_file", "icon": "ios-book-outline"},
		{"group": "default", "name": "do_download_file", "icon": "ios-book-outline"},
		{"group": "default", "name": "do_response_receive_file", "icon": "ios-book-outline"},
		{"group": "default", "name": "entity_parser", "icon": "ios-refresh-circle-outline"},
		{"group": "default", "name": "run_cmd", "icon": "md-bonfire"},
		{"group": "default", "name": "sftp_upload", "icon": "md-arrow-up"},
		{"group": "default", "name": "ssh_shell", "icon": "ios-cloud-upload-outline"},
		{"group": "default", "name": "targz_uncompress", "icon": "ios-aperture"},
		{"group": "default", "name": "targz_compress", "icon": "ios-aperture-outline"},
		{"group": "default", "name": "ini_read", "icon": "ios-fastforward"},
		{"group": "default", "name": "ini_write", "icon": "ios-aperture-outline"},
		{"group": "default", "name": "define_var", "icon": "md-hammer"},
		{"group": "default", "name": "assign_var", "icon": "md-hammer"},
		{"group": "default", "name": "map", "icon": "md-hammer"},
		{"group": "default", "name": "do_error_filter", "icon": "md-hammer"},
	}
}

func RegistNodes() {
	vs := []interface{}{
		sql.SQLExecuteNode{},
		sql.SQLQueryNode{},
		sql.DBParserNode{},
		json.JsonRenderNode{},
		json.JsonParserNode{},
		http.HttpRequestNode{},
		file.FileReadNode{},
		file.FileWriteNode{},
		file.FileSyncNode{},
		file.FileDeleteNode{},
		file.DoReceiveFileNode{},
		file.DoResponseReceiveFileNode{},
		file.DoDownloadFileNode{},
		http.HttpHrefParserNode{},
		http.HttpRequestParserNode{},
		http.HttpWirterNode{},
		chiper.CalHashNode{},
		os.SetEnvNode{},
		os.GetEnvNode{},
		bash.RunCmdNode{},
		os.SftpUploadNode{},
		bash.SSHShellNode{},
		zip.TarGzUnCompressNode{},
		zip.TarGzCompressNode{},
		file.IniReadNode{},
		file.IniWriteNode{},
		framework.EntityParserNode{},
		framework.WorkStartNode{},
		framework.WorkEndNode{},
		framework.WorkSubNode{},
		framework.MapperNode{},
		framework.IFNode{},
		framework.ElIfNode{},
		framework.ElseNode{},
		framework.DoErrorFilterNode{},
		framework.DefineVarNode{},
		framework.AssignVarNode{},
		framework.MapNode{},
		framework.ForeachNode{},
		framework.PanicErrorNode{},
		framework.CatchErrorNode{},
		framework.TemplateNode{},
		chiper.Base64EncodeNode{},
		chiper.Base64DecodeNode{},
		chiper.CreateJWTNode{},
		chiper.ParseJWTNode{},
		mail.SendMailNode{},
	}
	for _, v := range vs {
		t := reflect.ValueOf(v).Type()
		typeMap[strings.ToUpper(t.Name())] = t
	}
	node.Regist(typeMap)
}
