<SqlMigrate>
	<Id>122</Id>
	<AppId>5</AppId>
	<MigrateName>20200507123435_CREATE_pay_activity_coupon.sql</MigrateName>
	<MigrateSql>create table pay_activity&#xA;(&#xA;  id int auto_increment primary key,&#xA;  activity_id         varchar(255) default &#39;&#39; not null&#xA;  comment &#39;活动ID&#39;,&#xA;  activity_desc       varchar(255) default &#39;&#39; not null &#xA;  comment &#39;活动描述&#39;,&#xA;  activity_type       varchar(255) default &#39;&#39; not null&#xA;  comment &#39;活动类型&#39;,&#xA;  type_entity_account int default &#39;0&#39;         not null,&#xA;  organizer           varchar(255) default &#39;&#39; not null&#xA;  comment &#39;举办人&#39;,&#xA;  start_date          varchar(255) default &#39;&#39; not null&#xA;  comment &#39;活动开始日期&#39;,&#xA;  end_date            varchar(255) default &#39;&#39; not null&#xA;  comment &#39;活动结束日期&#39;,&#xA;  created_by          varchar(255) default &#39;&#39; not null,&#xA;  created_time        varchar(255) default &#39;&#39; not null,&#xA;  last_updated_by     varchar(255) default &#39;&#39; not null,&#xA;  last_updated_time   varchar(255) default &#39;&#39; not null&#xA;)&#xA;  collate = utf8_bin;&#xA;create table pay_coupon&#xA;(&#xA;  id int auto_increment  primary key,&#xA;  activity_id       varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;活动ID&#39;,&#xA;  coupon_id         varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;券ID&#39;,&#xA;  coupon_type       varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;券类型&#39;,&#xA;  target_type       varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;被使用对象类型&#39;,&#xA;  target_id         varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;被使用对象ID&#39;,&#xA;  youhui_type       varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;优惠方式&#39;,&#xA;  discount_rate     varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;折扣率&#39;,&#xA;  coupon_amount     decimal(9, 2) default &#39;0.00&#39; not null&#xA;  comment &#39;券的面值金额&#39;,&#xA;  goods_min_amount  decimal(9, 2) default &#39;0.00&#39; not null&#xA;  comment &#39;商品最低金额&#39;,&#xA;  coupon_owner      varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;券被领取者&#39;,&#xA;  coupon_state      varchar(255) default &#39;&#39;      not null&#xA;  comment &#39;券使用状态&#39;,&#xA;  created_by        varchar(255) default &#39;&#39;      not null,&#xA;  created_time      varchar(255) default &#39;&#39;      not null,&#xA;  last_updated_by   varchar(255) default &#39;&#39;      not null,&#xA;  last_updated_time varchar(255) default &#39;&#39;      not null&#xA;)&#xA;  collate = utf8_bin;</MigrateSql>
	<MigrateHash>sl3y2lw4uyE/+LFSh9SpB6EqXJA7MtGF0q80aLO40Kk=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-07T12:34:35+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-07T12:34:35+08:00</LastUpdatedTime>
</SqlMigrate>