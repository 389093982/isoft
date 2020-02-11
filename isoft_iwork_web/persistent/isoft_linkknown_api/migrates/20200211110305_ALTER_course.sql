<SqlMigrate>
	<Id>73</Id>
	<AppId>5</AppId>
	<MigrateName>20200211110305_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course add column custom_tag_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;客制化标签名称&#39;;</MigrateSql>
	<MigrateHash>wEZTxTJUEtizgaz7+GEDfZLadeqdDPuyGGvqgkUo/tw=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-11T11:03:05+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-11T11:03:05+08:00</LastUpdatedTime>
</SqlMigrate>