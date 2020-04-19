<SqlMigrate>
	<Id>114</Id>
	<AppId>5</AppId>
	<MigrateName>20200419172352_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column third_user_type varchar(50) COLLATE utf8_bin NOT NULL DEFAULT &#39;lk&#39; COMMENT &#39;第三方登录类型&#39;;&#xA;alter table user add column third_user_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;第三方登录用户&#39;;</MigrateSql>
	<MigrateHash>Yxf0M7Kmhtc0Rme2evq0A8TlXsFbyKQXF3hOz3/TUHA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-19T17:23:53+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-19T17:23:53+08:00</LastUpdatedTime>
</SqlMigrate>