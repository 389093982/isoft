<SqlMigrate>
	<Id>105</Id>
	<AppId>5</AppId>
	<MigrateName>20200324183420_CREATE_contact_message.sql</MigrateName>
	<MigrateSql>CREATE TABLE `contact_message` (&#xA;  `id` bigint(20) NOT NULL AUTO_INCREMENT,&#xA;  `user_name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户&#39;,&#xA;  `contact_user_name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;关联用户&#39;,&#xA;  `message_type` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;消息类型&#39;,&#xA;  `message_text` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;消息内容&#39;,&#xA;  `last_updated_time` datetime NOT NULL,&#xA;  PRIMARY KEY (`id`)&#xA;) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>S1rgTXcFtVVLvaD614ldDLKcY3d06xzetfSGi4WXRfs=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-24T18:34:21+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-24T18:34:21+08:00</LastUpdatedTime>
</SqlMigrate>