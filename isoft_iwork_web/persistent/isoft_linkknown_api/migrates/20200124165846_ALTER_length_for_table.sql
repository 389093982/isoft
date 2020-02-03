<SqlMigrate>
	<Id>57</Id>
	<AppId>5</AppId>
	<MigrateName>20200124165846_ALTER_length_for_table.sql</MigrateName>
	<MigrateSql>alter table blog_catalog modify catalog_desc varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table book modify book_desc varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table advise modify  advise longtext COLLATE utf8_bin NOT NULL;</MigrateSql>
	<MigrateHash>TtDzNbKDWdgpis0pBUr9xnprl+1BAgwGFMDkn0XW+kE=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-24T16:58:47+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-24T17:00:39+08:00</LastUpdatedTime>
</SqlMigrate>