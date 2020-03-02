<SqlMigrate>
	<Id>92</Id>
	<AppId>5</AppId>
	<MigrateName>20200302121039_ALTER_comment.sql</MigrateName>
	<MigrateSql>alter table comment add column org_parent_id int(11) NOT NULL DEFAULT 0;</MigrateSql>
	<MigrateHash>pYwAUG3C7JMrL5uNfh8LfObAa8VwYVcieuiuE0vetmA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-02T12:10:40+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-02T12:10:40+08:00</LastUpdatedTime>
</SqlMigrate>