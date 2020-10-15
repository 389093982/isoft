<SqlMigrate>
	<Id>145</Id>
	<AppId>5</AppId>
	<MigrateName>20201015170443_ALTER_user_site_record.sql</MigrateName>
	<MigrateSql>alter table user_site_record add column user_name varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>+VjGy//o4YBh9Qlt98sOLgWy9gzLHni2cx5cH1M3oYo=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-10-15T17:04:44+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-10-15T17:04:44+08:00</LastUpdatedTime>
</SqlMigrate>