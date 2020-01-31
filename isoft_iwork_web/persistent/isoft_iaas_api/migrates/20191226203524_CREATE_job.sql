<SqlMigrate>
	<Id>47</Id>
	<AppId>5</AppId>
	<MigrateName>20191226203524_CREATE_job.sql</MigrateName>
	<MigrateSql>CREATE TABLE `job_detail` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`corporate_id` int(11) NOT NULL DEFAULT -1 COMMENT &#39;公司id&#39;,&#xA;`job_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;工作名称&#39;,&#xA;`job_age` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;工作年限&#39;,&#xA;`job_address` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;工作地点&#39;,&#xA;`salary_range` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;薪酬范围&#39;,&#xA;`created_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` DATETIME NOT NULL,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>W4arb5LovsZHhlhhzpkViZQXN8yLF1N4SkKpSgfQje8=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2019-12-26T20:35:24+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2019-12-26T20:35:24+08:00</LastUpdatedTime>
</SqlMigrate>