<SqlMigrate>
	<Id>65</Id>
	<AppId>5</AppId>
	<MigrateName>20200205160512_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column custom_tag varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;自定义标签,用于首页客制化分类&#39;;</MigrateSql>
	<MigrateHash>yFJkgkk1cB6sy3ERaP7sLHQkefg6AeQu/KkuBpHaz0M=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-05T16:05:12+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-05T16:06:42+08:00</LastUpdatedTime>
</SqlMigrate>