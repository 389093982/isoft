<SqlMigrate>
	<Id>101</Id>
	<AppId>5</AppId>
	<MigrateName>20200312125306_CREATE_vote_tag.sql</MigrateName>
	<MigrateSql>CREATE TABLE `vote_tag` (&#xA;`referer_type` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;投票标签类型&#39;,&#xA;`referer_id` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;投票标签关联id&#39;,&#xA;`tag_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;标签名称&#39;,&#xA;`vote_number`  int(11) NOT NULL DEFAULT 0 COMMENT &#39;投票数量&#39;,&#xA;PRIMARY KEY (`referer_type`,`referer_id`,`tag_name`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;&#xA;CREATE TABLE `vote_taglog` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`referer_type` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;投票标签类型&#39;,&#xA;`referer_id` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;投票标签关联id&#39;,&#xA;`tag_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;标签名称&#39;,&#xA;`access_ip`  VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;访问 ip&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>CWgmH4SWUW0uwBx3X4xjXVwohJ7BdD03gFpAiDmiIsI=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-12T12:53:07+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-12T13:56:50+08:00</LastUpdatedTime>
</SqlMigrate>