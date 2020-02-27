<SqlMigrate>
	<Id>89</Id>
	<AppId>5</AppId>
	<MigrateName>20200227111551_ALTER_job_detail.sql</MigrateName>
	<MigrateSql>alter table job_detail drop column job_detail;&#xA;alter table job_detail add column job_education varchar(25) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;学历&#39;;</MigrateSql>
	<MigrateHash>erJHMneyvRbqA2PuSPcl+spf4HZGHbvkUOVbOl3a54c=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-27T11:15:51+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-27T11:15:51+08:00</LastUpdatedTime>
</SqlMigrate>