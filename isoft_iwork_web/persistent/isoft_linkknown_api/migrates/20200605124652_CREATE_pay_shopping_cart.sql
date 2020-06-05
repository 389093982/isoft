<SqlMigrate>
	<Id>128</Id>
	<AppId>5</AppId>
	<MigrateName>20200605124652_CREATE_pay_shopping_cart.sql</MigrateName>
	<MigrateSql>create table pay_shopping_cart&#xA;(&#xA;  id                                  int auto_increment primary key,&#xA;  user_name                   varchar(255) default &#39;&#39;      not null comment &#39;用户名&#39;,&#xA;  goods_id                       varchar(255) default &#39;&#39;      not null comment &#39;商品ID&#39;,&#xA;  goods_type                   varchar(255) default &#39;&#39;      not null comment &#39;商品类型&#39;,&#xA;  goods_count                 varchar(255) default &#39;0&#39;      not null comment &#39;商品数量&#39;,&#xA;  goods_price_on_add    decimal(9, 2) default &#39;0.00&#39; not null comment &#39;商品价格&#39;,&#xA;  add_time                      datetime                not null comment &#39;添加时间&#39;&#xA;)&#xA;  collate = utf8_bin;</MigrateSql>
	<MigrateHash>vtnzZf7Kag1tAUnVTZKYBfbEiUt8IMBQxh/t61jowfw=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-05T12:46:52+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-05T15:02:30+08:00</LastUpdatedTime>
</SqlMigrate>