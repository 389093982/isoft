<SqlMigrate>
	<Id>71</Id>
	<AppId>5</AppId>
	<MigrateName>20200207122042_ALTER_advise.sql</MigrateName>
	<MigrateSql>alter table advise add column advise_type varchar(10) COLLATE utf8_bin NOT NULL DEFAULT &#39;advise&#39; COMMENT &#39;意见或吐槽&#39;;</MigrateSql>
	<MigrateHash>erzbj7hPYApKz4obAwH0FYstspSWMAM+wy4wflY3aNU=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-07T12:20:43+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-07T13:04:26+08:00</LastUpdatedTime>
</SqlMigrate>