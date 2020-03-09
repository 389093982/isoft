<SqlMigrate>
	<Id>99</Id>
	<AppId>5</AppId>
	<MigrateName>20200309152238_CREATE_decorate.sql</MigrateName>
	<MigrateSql>CREATE TABLE `decorate` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`referer_type` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰关联类型&#39;,&#xA;`referer_id` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰关联id&#39;,&#xA;`decorate_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰名称&#39;,&#xA;`decorate_icon` VARCHAR(20) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰图标&#39;,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` datetime NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;&#xA;CREATE TABLE `decorate_item` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`decorate_id` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰表关联id&#39;,&#xA;`media_path` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;媒体路径&#39;,&#xA;`decorate_text` VARCHAR(20) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;装饰文本&#39;,&#xA;`link_href` VARCHAR(20) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;链接地址&#39;,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` datetime NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>cM5U8o3RdanJ1MX5PV4nAcLttRkRxeAXJ4ObMqcE3DU=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-09T15:22:39+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-09T15:22:39+08:00</LastUpdatedTime>
</SqlMigrate>