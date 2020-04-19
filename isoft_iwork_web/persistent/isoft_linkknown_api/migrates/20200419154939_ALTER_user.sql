<SqlMigrate>
	<Id>111</Id>
	<AppId>5</AppId>
	<MigrateName>20200419154939_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column third_name varchar(50) COLLATE utf8_bin NOT NULL DEFAULT &#39;lk&#39; COMMENT &#39;第三方登录类型&#39;;</MigrateSql>
	<MigrateHash>A+if8Vg+8fYLFkZoUhW+Ej5cOyLNtfHw8rZtQd3tpx4=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-19T15:49:39+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-19T15:49:39+08:00</LastUpdatedTime>
</SqlMigrate>