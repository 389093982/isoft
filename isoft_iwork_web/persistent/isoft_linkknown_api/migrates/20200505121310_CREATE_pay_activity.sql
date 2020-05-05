<SqlMigrate>
	<Id>115</Id>
	<AppId>5</AppId>
	<MigrateName>20200505121310_CREATE_pay_activity.sql</MigrateName>
	<MigrateSql>create table pay_activity(&#xA;id int auto_increment primary key,&#xA;activity_id varchar(255) default &#39;&#39; not null comment &#39;活动ID&#39;,&#xA;activity_type varchar(255) default &#39;&#39; not null comment &#39;活动类型&#39;,&#xA;type_entity_acount int(11) default 0 not null comment &#39;类型实体对应的数量&#39;,&#xA;organizer varchar(255) default &#39;&#39; not null comment &#39;举办人&#39;,&#xA;start_date varchar(255) default &#39;&#39; not null comment &#39;活动开始日期&#39;,&#xA;end_date varchar(255) default &#39;&#39; not null comment &#39;活动结束日期&#39;,&#xA;created_by varchar(255) default &#39;&#39; not null,&#xA;created_time varchar(255) default &#39;&#39; not null,&#xA;last_updated_by varchar(255) default &#39;&#39; not null,&#xA;last_updated_time varchar(255) default &#39;&#39; not null&#xA;)&#xA;collate = utf8_bin;</MigrateSql>
	<MigrateHash>Q61Y1ZEjXAStauCkNTO9GzUb++HxXqVvqiAJdH3GLPk=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-05T12:13:10+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-05T12:13:10+08:00</LastUpdatedTime>
</SqlMigrate>