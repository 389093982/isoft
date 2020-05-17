<SqlMigrate>
	<Id>126</Id>
	<AppId>5</AppId>
	<MigrateName>20200517095732_ALTER_modify_column_pay_order.sql</MigrateName>
	<MigrateSql>alter table pay_order modify column goods_price decimal(9, 2) default &#39;0.00&#39; not null;&#xA;alter table pay_order modify column goods_original_price decimal(9, 2) default &#39;0.00&#39; not null;</MigrateSql>
	<MigrateHash>gRXCkXo0o/PRrrINzU8I096/RZDdH2yqRB9YNmA/AO0=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-17T09:57:33+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-17T09:57:33+08:00</LastUpdatedTime>
</SqlMigrate>