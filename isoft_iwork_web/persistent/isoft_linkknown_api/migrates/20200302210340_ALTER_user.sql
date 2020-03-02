<SqlMigrate>
	<Id>93</Id>
	<AppId>5</AppId>
	<MigrateName>20200302210340_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column sex varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table user add column birthday datetime COLLATE utf8_bin;&#xA;alter table user add column occupation varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table user add column current_residence varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table user add column hometown varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>LHfVsH77DVFJ1uZY5tTd7IyGadIFmjjRkMgzW0yt0TM=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-02T21:03:40+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-02T21:03:40+08:00</LastUpdatedTime>
</SqlMigrate>