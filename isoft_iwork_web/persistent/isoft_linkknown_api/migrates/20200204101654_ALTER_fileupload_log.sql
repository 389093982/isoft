<SqlMigrate>
	<Id>62</Id>
	<AppId>5</AppId>
	<MigrateName>20200204101654_ALTER_fileupload_log.sql</MigrateName>
	<MigrateSql>alter table fileupload_log add column table_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;文件所在表信息&#39;;&#xA;alter table fileupload_log add column table_field varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;文件所在字段信息&#39;;</MigrateSql>
	<MigrateHash>4banB+x9hk8ZcFefgnHEEaPdjbgqTPxMkLdS2t3TiTI=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-04T10:16:55+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-04T10:19:32+08:00</LastUpdatedTime>
</SqlMigrate>