<SqlMigrate>
	<Id>108</Id>
	<AppId>5</AppId>
	<MigrateName>20200403085432_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column isCharge varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table course add column preListFree int(11) NOT NULL DEFAULT 0;&#xA;alter table course add column price decimal(9,2) NOT NULL DEFAULT 0;</MigrateSql>
	<MigrateHash>pzSYOJH8D/k4joV2KswNYxsiHl2n1GmGND78STCc+HM=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-03T08:54:32+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-03T08:54:32+08:00</LastUpdatedTime>
</SqlMigrate>