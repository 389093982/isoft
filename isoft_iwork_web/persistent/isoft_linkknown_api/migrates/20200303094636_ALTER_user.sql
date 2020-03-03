<SqlMigrate>
	<Id>94</Id>
	<AppId>5</AppId>
	<MigrateName>20200303094636_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user drop column sex;&#xA;alter table user drop column occupation;&#xA;alter table user add column gender varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>MRGZxGELRlDSOfvYp3tiscclPskHqKorNL47IuhVvTc=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-03T09:46:37+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-03T09:46:37+08:00</LastUpdatedTime>
</SqlMigrate>