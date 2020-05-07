<SqlMigrate>
	<Id>120</Id>
	<AppId>5</AppId>
	<MigrateName>20200507121326_ALTER_pay_activity_and_coupon.sql</MigrateName>
	<MigrateSql>alter table pay_activity add column activity_desc varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table pay_coupon add column youhui_type varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;&#xA;alter table pay_coupon add column discount_rate varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;;</MigrateSql>
	<MigrateHash>ALRSxdUJzX62h8m2rAy8smfBNslYwwtZ+dw6Rsy1PdE=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-07T12:13:26+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-07T12:13:26+08:00</LastUpdatedTime>
</SqlMigrate>