<SqlMigrate>
	<Id>85</Id>
	<AppId>5</AppId>
	<MigrateName>20200221101617_ALTER_book_article.sql</MigrateName>
	<MigrateSql>alter table book_article add column views int(11) NOT NULL DEFAULT 0 COMMENT &#39;阅读次数&#39;;</MigrateSql>
	<MigrateHash>MVzz5bK3wdrcqrcpTQ9LEiiF9FRmZaN6g3cdbJxs+vw=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-21T10:16:17+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-21T10:16:17+08:00</LastUpdatedTime>
</SqlMigrate>