<SqlMigrate>
	<Id>130</Id>
	<AppId>5</AppId>
	<MigrateName>20200606112410_ALTER_pay_order_add_column.sql</MigrateName>
	<MigrateSql>alter table pay_order add column order_time datetime not null comment &#39;下单时间&#39;;&#xA;alter table pay_order add column code_url varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>pCB1pXHHBbM9wa00AHvQ7rQ2pRa78Lz3d+VKHRwUIMo=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-06T11:24:11+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-06T12:29:45+08:00</LastUpdatedTime>
</SqlMigrate>