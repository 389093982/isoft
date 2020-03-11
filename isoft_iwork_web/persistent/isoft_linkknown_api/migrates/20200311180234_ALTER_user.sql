<SqlMigrate>
	<Id>100</Id>
	<AppId>5</AppId>
	<MigrateName>20200311180234_ALTER_user.sql</MigrateName>
	<MigrateSql>alter table user add column hat varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table user add column hat_in_use varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;N&#39;;</MigrateSql>
	<MigrateHash>m/iKx/JasN9u/U9vwnJcUyruVSEHrQ2X5DZex8Bw48M=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-11T18:02:35+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-11T18:02:35+08:00</LastUpdatedTime>
</SqlMigrate>