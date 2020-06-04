<SqlMigrate>
	<Id>127</Id>
	<AppId>5</AppId>
	<MigrateName>20200604080503_ALTER_pay_order_modify_column.sql</MigrateName>
	<MigrateSql>alter table pay_order change  column goods_price paid_amount decimal(9, 2) default &#39;0.00&#39; not null;</MigrateSql>
	<MigrateHash>aKAqLhQRYMdTDUoE7WEnEO3BJFsFH01rHHLSe5MqEfw=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-04T08:05:03+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-04T08:05:03+08:00</LastUpdatedTime>
</SqlMigrate>