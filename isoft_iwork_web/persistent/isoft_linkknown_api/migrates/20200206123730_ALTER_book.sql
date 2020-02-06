<SqlMigrate>
	<Id>66</Id>
	<AppId>5</AppId>
	<MigrateName>20200206123730_ALTER_book.sql</MigrateName>
	<MigrateSql>alter table book add column custom_tag varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;自定义标签,用于首页客制化分类&#39;;</MigrateSql>
	<MigrateHash>ifyks98qHq6/uN0sw0fqXhVJMDMI/ix0vxjkq4IWiBA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-06T12:37:31+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-06T12:37:31+08:00</LastUpdatedTime>
</SqlMigrate>