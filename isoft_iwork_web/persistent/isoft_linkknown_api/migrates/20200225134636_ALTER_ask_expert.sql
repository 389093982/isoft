<SqlMigrate>
	<Id>86</Id>
	<AppId>5</AppId>
	<MigrateName>20200225134636_ALTER_ask_expert.sql</MigrateName>
	<MigrateSql>alter table ask_expert add column view_number int(11) NOT NULL DEFAULT 0 COMMENT &#39;浏览次数&#39;;</MigrateSql>
	<MigrateHash>cIZGwkhLxjU82maO1mlse2sSMwJnxUZOwP86v6rghD0=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-25T13:46:36+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-25T13:46:36+08:00</LastUpdatedTime>
</SqlMigrate>