<SqlMigrate>
	<Id>136</Id>
	<AppId>5</AppId>
	<MigrateName>20200616162134_ALTER_user_attention.sql</MigrateName>
	<MigrateSql>alter table user add column attention_counts int(11) NOT NULL DEFAULT 0 comment &#39;关注的数量&#39;;</MigrateSql>
	<MigrateHash>bDc0pNicenhMaKEvWsmkbcyyPa9lkvzOnf88yTDCyHA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-16T16:21:34+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-16T16:21:34+08:00</LastUpdatedTime>
</SqlMigrate>