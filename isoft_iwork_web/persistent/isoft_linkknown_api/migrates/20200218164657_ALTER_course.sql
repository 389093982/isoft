<SqlMigrate>
	<Id>81</Id>
	<AppId>5</AppId>
	<MigrateName>20200218164657_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column created_by VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;创建人&#39;;&#xA;alter table course add column created_time DATETIME COLLATE utf8_bin NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT &#39;创建时间&#39;;&#xA;alter table course add column updated_by VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;更新人&#39;;&#xA;alter table course add column updated_time DATETIME COLLATE utf8_bin NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT &#39;更新时间&#39;;</MigrateSql>
	<MigrateHash>v/JjyAW4BqAwiBP1JYHTWTcsQwPhSkowSYiOjeajt/I=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-18T16:46:57+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-18T16:49:09+08:00</LastUpdatedTime>
</SqlMigrate>