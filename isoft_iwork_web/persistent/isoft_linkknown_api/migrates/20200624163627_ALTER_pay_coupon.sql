<SqlMigrate>
	<Id>139</Id>
	<AppId>5</AppId>
	<MigrateName>20200624163627_ALTER_pay_coupon.sql</MigrateName>
	<MigrateSql>alter table pay_coupon add column target_name &#xA;varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; comment &#39;被使用对象名称&#39;;</MigrateSql>
	<MigrateHash>bfhcL8/da/ImrTdKOWSMYKK80TyCVtoDtN+hcJrmyvc=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-24T16:36:27+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-24T16:36:27+08:00</LastUpdatedTime>
</SqlMigrate>