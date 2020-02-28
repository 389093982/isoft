<SqlMigrate>
	<Id>90</Id>
	<AppId>5</AppId>
	<MigrateName>20200228184731_ALTER_blog_article.sql</MigrateName>
	<MigrateSql>alter table blog_article add column comments bigint(11) NOT NULL DEFAULT 0;</MigrateSql>
	<MigrateHash>Gnt57acxdj9+D8//S2fcka89kM9O2opxGu5Xf7rNja4=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-28T18:47:32+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-28T18:48:15+08:00</LastUpdatedTime>
</SqlMigrate>