<SqlMigrate>
	<Id>109</Id>
	<AppId>5</AppId>
	<MigrateName>20200406202329_ALTER_blog_article.sql</MigrateName>
	<MigrateSql>alter table blog_article add column first_img varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;博客第一张图片&#39;;</MigrateSql>
	<MigrateHash>Ha3FVlPcqYUsIdk9uL3e5/E0pbeVteoLYBzLUmx43Jg=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-06T20:23:29+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-06T20:23:29+08:00</LastUpdatedTime>
</SqlMigrate>