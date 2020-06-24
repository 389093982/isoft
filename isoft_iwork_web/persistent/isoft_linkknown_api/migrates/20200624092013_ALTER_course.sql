<SqlMigrate>
	<Id>138</Id>
	<AppId>5</AppId>
	<MigrateName>20200624092013_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column old_price decimal(9, 2) default &#39;0.00&#39;;&#xA;alter table course add column is_show_old_price varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;N&#39;;</MigrateSql>
	<MigrateHash>3DBhYJ+C/t2i0tAzwMnqiSGMsj04HgF+6uYmd2pCo7M=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-24T09:20:14+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-24T09:20:14+08:00</LastUpdatedTime>
</SqlMigrate>