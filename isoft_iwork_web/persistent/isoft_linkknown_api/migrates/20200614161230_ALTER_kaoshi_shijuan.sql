<SqlMigrate>
	<Id>134</Id>
	<AppId>5</AppId>
	<MigrateName>20200614161230_ALTER_kaoshi_shijuan.sql</MigrateName>
	<MigrateSql>alter table kaoshi_shijuan add column classify_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;分类名称&#39;;</MigrateSql>
	<MigrateHash>xDXRpm7300qzvyyli2GQwCWFDNpqCLD+XYoeVx29vhQ=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-14T16:12:30+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-14T16:12:30+08:00</LastUpdatedTime>
</SqlMigrate>