<SqlMigrate>
	<Id>131</Id>
	<AppId>5</AppId>
	<MigrateName>20200611082112_CREATE_user_attention.sql</MigrateName>
	<MigrateSql>create table user_attention&#xA;(&#xA;id int auto_increment primary key,&#xA;user_name varchar(255) default &#39;&#39; not null comment &#39;用户名&#39;,&#xA;attention_object_type varchar(255) default &#39;&#39; not null comment &#39;关注对象的类型&#39;,&#xA;attention_object_id varchar(255) default &#39;&#39; not null comment &#39;关注对象的ID&#39;,&#xA;attention_time datetime not null comment &#39;关注时间&#39;,&#xA;state varchar(255) default &#39;&#39; not null comment &#39;状态&#39;&#xA;)&#xA;collate = utf8_bin;</MigrateSql>
	<MigrateHash>GZfy3L7eDFVyKx1akfSbpzD4P5xHMzUp6+ikIDnDzA0=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-11T08:21:12+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-11T08:21:12+08:00</LastUpdatedTime>
</SqlMigrate>