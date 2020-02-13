<SqlMigrate>
	<Id>79</Id>
	<AppId>5</AppId>
	<MigrateName>20200213112630_ALTER_advertisement.sql</MigrateName>
	<MigrateSql>alter table advertisement add column size_type int(11) NOT NULL DEFAULT 0 COMMENT &#39;广告尺寸类型&#39;;&#xA;alter table advertisement add column size_desc VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;广告尺寸描述&#39;;&#xA;alter table advertisement add column is_valid int(1) NOT NULL DEFAULT 0 COMMENT &#39;广告生效状态&#39;;</MigrateSql>
	<MigrateHash>l0FKlvjAYAwBXJ/B7kFV2lvnY66jtmyLWs567Xle+vg=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-13T11:26:31+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-13T11:30:28+08:00</LastUpdatedTime>
</SqlMigrate>