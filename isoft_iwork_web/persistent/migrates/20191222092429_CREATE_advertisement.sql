<SqlMigrate>
	<Id>45</Id>
	<MigrateName>20191222092429_CREATE_advertisement.sql</MigrateName>
	<MigrateSql>CREATE TABLE `advertisement` (&#xA;`id` bigint(20) NOT NULL AUTO_INCREMENT,&#xA;`advertisement_label` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;广告标签语&#39;,&#xA;`linked_type` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;广告链接类型&#39;,&#xA;`linked_refer` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;广告链接地址&#39;,&#xA;`linked_img` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;广告链接图片&#39;,&#xA;`contact` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;联系人&#39;,&#xA;`created_by` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` datetime NOT NULL,&#xA;`last_updated_by` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` datetime NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>jmSjxBaKsgSqfvSmgugIFNOWyeyDfhMmoJbRhSajxrc=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2019-12-22T09:24:29+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2019-12-22T09:24:29+08:00</LastUpdatedTime>
</SqlMigrate>