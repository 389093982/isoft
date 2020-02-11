<SqlMigrate>
	<Id>74</Id>
	<AppId>5</AppId>
	<MigrateName>20200211150405_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column course_label varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;课程标签语&#39;;</MigrateSql>
	<MigrateHash>yyIwIlaaY0cws9I7ziCyejNen1WffZ0/r3Dq7qkRD+E=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-11T15:04:05+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-11T15:04:05+08:00</LastUpdatedTime>
</SqlMigrate>