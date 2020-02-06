<SqlMigrate>
	<Id>68</Id>
	<AppId>5</AppId>
	<MigrateName>20200206125527_ALTER_book.sql</MigrateName>
	<MigrateSql>alter table book add column views int(11) NOT NULL DEFAULT 0 COMMENT &#39;阅读次数&#39;;&#xA;alter table blog_article drop column book_id;</MigrateSql>
	<MigrateHash>kHBAu4l44uulMqZsJFDajMZkndwsc4eSvQ2XSQ0+/+g=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-06T12:55:28+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-06T12:58:44+08:00</LastUpdatedTime>
</SqlMigrate>