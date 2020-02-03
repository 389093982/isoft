<SqlMigrate>
	<Id>60</Id>
	<AppId>5</AppId>
	<MigrateName>20200203105658_ALTER_resource.sql</MigrateName>
	<MigrateSql>alter table resource add column recommend int(11) NOT NULL DEFAULT 0;&#xA;alter table resource add column not_recommend int(11) NOT NULL DEFAULT 0;</MigrateSql>
	<MigrateHash>jZeykvA5E+fyMmlYacdPvvGKqoDq+b9cLk1hf1jtUx8=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-03T10:56:59+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-03T10:57:19+08:00</LastUpdatedTime>
</SqlMigrate>