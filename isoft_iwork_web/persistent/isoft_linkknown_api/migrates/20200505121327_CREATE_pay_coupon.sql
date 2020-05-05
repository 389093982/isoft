<SqlMigrate>
	<Id>116</Id>
	<AppId>5</AppId>
	<MigrateName>20200505121327_CREATE_pay_coupon.sql</MigrateName>
	<MigrateSql>create table pay_coupon(&#xA;id int auto_increment primary key,&#xA;activity_id varchar(255) default &#39;&#39; not null comment &#39;活动ID&#39;,&#xA;coupon_id varchar(255) default &#39;&#39; not null comment &#39;券ID&#39;,&#xA;coupon_type varchar(255) default &#39;&#39; not null comment &#39;券类型&#39;,&#xA;target_type varchar(255) default &#39;&#39; not null comment &#39;被使用对象类型&#39;,&#xA;target_id varchar(255) default &#39;&#39; not null comment &#39;被使用对象ID&#39;,&#xA;coupon_amount decimal(9, 2) default &#39;0.00&#39; not null comment &#39;券的面值金额&#39;,&#xA;goods_min_amount decimal(9, 2) default &#39;0.00&#39; not null comment &#39;商品最低金额&#39;,&#xA;coupon_owner varchar(255) default &#39;&#39; not null comment &#39;券被领取者&#39;,&#xA;coupon_state varchar(255) default &#39;&#39; not null comment &#39;券使用状态&#39;,&#xA;created_by varchar(255) default &#39;&#39; not null,&#xA;created_time varchar(255) default &#39;&#39; not null,&#xA;last_updated_by varchar(255) default &#39;&#39; not null,&#xA;last_updated_time varchar(255) default &#39;&#39; not null&#xA;)&#xA;collate = utf8_bin;</MigrateSql>
	<MigrateHash>RleCErRlgg7VVkGy/GqXmm5k9iy9uhRXQE7/X/aEJPY=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-05T12:13:27+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-05T12:13:27+08:00</LastUpdatedTime>
</SqlMigrate>