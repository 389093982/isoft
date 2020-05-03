<SqlMigrate>
	<Id>114</Id>
	<AppId>5</AppId>
	<MigrateName>20200419172352_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column third_user_type varchar(50) COLLATE utf8_bin NOT NULL DEFAULT &#39;linkknown&#39; COMMENT &#39;第三方登录类型&#39;;</MigrateSql>
	<MigrateHash>jjdnP1hdcneaohDi0is/RmmgpEP6l0NEOajKC7PbnjA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-19T17:23:53+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-03T15:41:57+08:00</LastUpdatedTime>
</SqlMigrate>