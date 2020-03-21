<SqlMigrate>
	<Id>103</Id>
	<AppId>5</AppId>
	<MigrateName>20200321181814_ALTER_course_video.sql</MigrateName>
	<MigrateSql>alter table course_video add column duration int(11) NOT NULL DEFAULT -1 COMMENT &#39;视频时长&#39;;</MigrateSql>
	<MigrateHash>HJTMKO0gNNGq9cwX1jvvnWsZM1LVTAtcZCZkAOTyeR8=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-21T18:18:15+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-21T18:18:15+08:00</LastUpdatedTime>
</SqlMigrate>