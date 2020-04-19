<SqlMigrate>
	<Id>114</Id>
	<AppId>5</AppId>
	<MigrateName>20200419172352_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column third_user_type varchar(50) COLLATE utf8_bin NOT NULL DEFAULT &#39;lk&#39; COMMENT &#39;第三方登录类型&#39;;</MigrateSql>
	<MigrateHash>26q5NwC/ZHOupJE8w9s3/ZqDNGU1kSnt47e9D3jdCCA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-19T17:23:53+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-19T17:56:04+08:00</LastUpdatedTime>
</SqlMigrate>