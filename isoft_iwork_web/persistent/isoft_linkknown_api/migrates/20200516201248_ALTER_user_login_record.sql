<SqlMigrate>
	<Id>125</Id>
	<AppId>5</AppId>
	<MigrateName>20200516201248_ALTER_user_login_record.sql</MigrateName>
	<MigrateSql>alter table user_login_record add column client_type varchar(10) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;  comment &#39;客户端类型&#39;;</MigrateSql>
	<MigrateHash>ZfBUdyfNwkm8enhGJFhw5P8PwOzk3mAI7vyLAjxDLgI=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-16T20:12:49+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-16T20:12:49+08:00</LastUpdatedTime>
</SqlMigrate>