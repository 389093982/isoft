<SqlMigrate>
	<Id>135</Id>
	<AppId>5</AppId>
	<MigrateName>20200616135327_ALTER_kaoshi_shijuan.sql</MigrateName>
	<MigrateSql>alter table kaoshi_shijuan add column is_completed  INT(1) COLLATE utf8_bin NOT NULL DEFAULT 0 COMMENT &#39;考试是否完成&#39;;</MigrateSql>
	<MigrateHash>TGEirH63b3i3kXIkgK8T7Pbr4lJJD9wUJj3ThO2FxOc=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-16T13:53:27+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-16T13:53:27+08:00</LastUpdatedTime>
</SqlMigrate>