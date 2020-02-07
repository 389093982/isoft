<SqlMigrate>
	<Id>70</Id>
	<AppId>5</AppId>
	<MigrateName>20200207085908_ALTER_blog_article.sql</MigrateName>
	<MigrateSql>alter table blog_article add column custom_tag varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;自定义标签,用于首页客制化分类&#39;;</MigrateSql>
	<MigrateHash>hQ+/VBgEG62tHiZGc/hRo4GZt5+wtmjQbn93trDU8dM=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-07T08:59:09+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-07T08:59:09+08:00</LastUpdatedTime>
</SqlMigrate>