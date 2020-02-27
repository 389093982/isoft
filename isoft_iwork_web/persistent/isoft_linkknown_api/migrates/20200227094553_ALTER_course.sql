<SqlMigrate>
	<Id>87</Id>
	<AppId>5</AppId>
	<MigrateName>20200227094553_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course drop column course_short_des;&#xA;alter table job_detail add column job_detail varchar(25) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;学历&#39;;&#xA;alter table job_detail add column job_tags varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;岗位标签&#39;;</MigrateSql>
	<MigrateHash>w6O9xTEn7+x42MkiW30H9Y9hhAZKyk3IoeYZlhQDw0A=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-27T09:45:54+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-27T09:45:54+08:00</LastUpdatedTime>
</SqlMigrate>