<SqlMigrate>
	<Id>142</Id>
	<AppId>5</AppId>
	<MigrateName>20201014202629_CREATE_site_record.sql</MigrateName>
	<MigrateSql>create table site_record (&#xA;access_ip varchar(255) default &#39;&#39; not null comment &#39;访问 ip&#39; primary key,&#xA;access_count INT(5) COLLATE utf8_bin NOT NULL DEFAULT 0 COMMENT &#39;访问总次数&#39;,&#xA;last_updated_time DATETIME NOT NULL&#xA;)&#xA;collate = utf8_bin;</MigrateSql>
	<MigrateHash>Phpkpz6Jd4PIpHGJqKVRz4bN54jlFTCly6uyPXn58fY=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-10-14T20:26:30+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-10-14T20:41:06+08:00</LastUpdatedTime>
</SqlMigrate>