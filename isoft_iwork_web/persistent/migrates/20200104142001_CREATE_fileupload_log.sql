<SqlMigrate>
	<Id>51</Id>
	<MigrateName>20200104142001_CREATE_fileupload_log.sql</MigrateName>
	<MigrateSql>CREATE TABLE `fileupload_log` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`file_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;文件名称&#39;,&#xA;`file_hash` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;文件hash&#39;,&#xA;`file_addr` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;文件访问地址&#39;,&#xA;`created_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` DATETIME NOT NULL,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>+pruowEfgHDVuNWlkF0JJzvHjUBigsGxdr1Ll9Kezj8=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-04T14:20:02+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-04T14:20:02+08:00</LastUpdatedTime>
</SqlMigrate>