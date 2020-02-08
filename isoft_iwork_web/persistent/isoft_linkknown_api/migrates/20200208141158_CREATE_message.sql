<SqlMigrate>
	<Id>72</Id>
	<AppId>5</AppId>
	<MigrateName>20200208141158_CREATE_message.sql</MigrateName>
	<MigrateSql>CREATE TABLE `message` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;,&#xA;`message_type` VARCHAR(20) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;消息类型&#39;,&#xA;`message_text` VARCHAR(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;消息内容&#39;,&#xA;`last_updated_time` datetime NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>HLuRkQ0DRjSJD2rzvvIz4Bi1Yl7okP9Z0KzaUyiwrvE=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-08T14:11:59+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-08T14:13:34+08:00</LastUpdatedTime>
</SqlMigrate>