<SqlMigrate>
	<Id>106</Id>
	<AppId>5</AppId>
	<MigrateName>20200325100154_ALTER_contact_message.sql</MigrateName>
	<MigrateSql>alter table contact_message add column last_updated_timestamp bigint(20) NOT NULL DEFAULT &#39;-1&#39;;</MigrateSql>
	<MigrateHash>KI6uxGD0MehTqW2VHo2FVCp2QuAYfkrGbwO/dDnAaBw=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-25T10:01:54+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-25T10:13:34+08:00</LastUpdatedTime>
</SqlMigrate>