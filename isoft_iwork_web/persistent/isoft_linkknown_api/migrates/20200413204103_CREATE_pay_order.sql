<SqlMigrate>
	<Id>110</Id>
	<AppId>5</AppId>
	<MigrateName>20200413204103_CREATE_pay_order.sql</MigrateName>
	<MigrateSql>create table pay_order(&#xA;  id int auto_increment primary key,&#xA;  order_id varchar(255) default &#39;&#39; not null,&#xA;  trans_time varchar(255) default &#39;&#39; not null,&#xA;  user_name varchar(255) default &#39;&#39; not null,&#xA;  goods_type varchar(255) default &#39;&#39; not null,&#xA;  goods_id varchar(255) default &#39;&#39; not null,&#xA;  goods_desc varchar(255) default &#39;&#39; not null,&#xA;  goods_price bigint default &#39;0&#39; not null,&#xA;  goods_img varchar(255) default &#39;&#39; not null,&#xA;  pay_result varchar(255) default &#39;&#39; not null&#xA;)&#xA;collate = utf8_bin;</MigrateSql>
	<MigrateHash>2y+HYutq4FmjHZ6Wwurp0eAbrOzfSlaIQF2EcHhxaG0=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-04-13T20:41:03+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-04-13T20:41:03+08:00</LastUpdatedTime>
</SqlMigrate>