<SqlMigrate>
	<Id>124</Id>
	<AppId>5</AppId>
	<MigrateName>20200515083908_ALTER_common_fileupload_log.sql</MigrateName>
	<MigrateSql>alter table common_fileupload_log add column file_size varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>+tIsUAe+6IZQiiP0gro+7R/2xIjfEkMh6I4qarFW+pQ=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-15T08:39:09+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-15T08:39:09+08:00</LastUpdatedTime>
</SqlMigrate>