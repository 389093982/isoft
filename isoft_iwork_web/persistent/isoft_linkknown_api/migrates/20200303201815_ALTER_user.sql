<SqlMigrate>
	<Id>95</Id>
	<AppId>5</AppId>
	<MigrateName>20200303201815_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user drop column birthday;&#xA;alter table user add column birthday date COLLATE utf8_bin;</MigrateSql>
	<MigrateHash>3mKRKiyMpJ7RjZt15bLYJXsaLU+fQh8+Xr6UTSPM1Gs=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-03T20:18:15+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-03T20:18:15+08:00</LastUpdatedTime>
</SqlMigrate>