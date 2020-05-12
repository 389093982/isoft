<SqlMigrate>
	<Id>123</Id>
	<AppId>5</AppId>
	<MigrateName>20200512125259_ALTER_pay_order.sql</MigrateName>
	<MigrateSql>alter table pay_order add column goods_original_price bigint  not null default &#39;0&#39; comment &#39;商品原价格&#39;;&#xA;alter table pay_order add column activity_type varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; comment &#39;参与的活动类型&#39;;&#xA;alter table pay_order add column activity_type_bind_id varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; comment &#39;活动中具体使用的优惠id&#39;;</MigrateSql>
	<MigrateHash>QRu6yw3mQfKXcoc3uy+2iQ6OizaeasEndJuKFjHbNwk=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-12T12:53:00+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-12T12:53:00+08:00</LastUpdatedTime>
</SqlMigrate>