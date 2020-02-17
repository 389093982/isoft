<SqlMigrate>
	<Id>80</Id>
	<AppId>5</AppId>
	<MigrateName>20200217170613_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column user_signature VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;个性签名&#39;;</MigrateSql>
	<MigrateHash>bZsFl909bBzFUbFQw9qAVEJahBzjJfLzCDZCwJab8wU=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-17T17:06:13+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-17T17:06:13+08:00</LastUpdatedTime>
</SqlMigrate>