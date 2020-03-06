<SqlMigrate>
	<Id>96</Id>
	<AppId>5</AppId>
	<MigrateName>20200306174453_ALTER_good.sql</MigrateName>
	<MigrateSql>alter table good add column good_type VARCHAR(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;商品分类&#39;;&#xA;alter table good add column good_tag VARCHAR(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;商品标签&#39;;</MigrateSql>
	<MigrateHash>ODZjwhl8YJN6IIxYnsiIWWLyaB5SL/inuhsC/bJANtY=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-06T17:44:53+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-06T17:44:53+08:00</LastUpdatedTime>
</SqlMigrate>