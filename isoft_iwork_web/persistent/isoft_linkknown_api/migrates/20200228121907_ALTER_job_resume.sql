<SqlMigrate>
	<Id>90</Id>
	<AppId>5</AppId>
	<MigrateName>20200228121907_ALTER_job_resume.sql</MigrateName>
	<MigrateSql>alter table job_resume change user_name user_name varchar(50) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;;&#xA;alter table job_resume change age age int(3) NOT NULL DEFAULT 0 COMMENT &#39;年龄&#39;;&#xA;alter table job_resume change sex sex int(1) NOT NULL DEFAULT 0 COMMENT &#39;性别,1男2女&#39;;&#xA;alter table job_resume change job_start_time job_start_time datetime NOT NULL COMMENT &#39;开始工作时间&#39;;&#xA;alter table job_resume change birthday birthday datetime NOT NULL COMMENT &#39;出生年月&#39;;</MigrateSql>
	<MigrateHash>F4mcWqxvNibsCyb3OaDWMWDt5PqDdn4quMFoOGLTu3k=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-28T12:19:08+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-28T12:19:08+08:00</LastUpdatedTime>
</SqlMigrate>