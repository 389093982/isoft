<SqlMigrate>
	<Id>61</Id>
	<AppId>5</AppId>
	<MigrateName>20200203155027_ALTER_resource.sql</MigrateName>
	<MigrateSql>alter table resource add column resource_catalog varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>lB2auHIcLyAjo71IvB76SkJKMEp+yTBWReyUVHUIJL8=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-03T15:50:28+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-03T15:50:28+08:00</LastUpdatedTime>
</SqlMigrate>