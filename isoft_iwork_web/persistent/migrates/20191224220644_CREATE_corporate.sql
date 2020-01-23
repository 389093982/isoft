<SqlMigrate>
	<Id>46</Id>
	<AppId>5</AppId>
	<MigrateName>20191224220644_CREATE_corporate.sql</MigrateName>
	<MigrateSql>CREATE TABLE `corporate_detail` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`corporate_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司名称&#39;,&#xA;`corporate_site` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司主页&#39;,&#xA;`corporate_logo` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司 logo&#39;,&#xA;`corporate_size` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司规模&#39;,&#xA;`job_type` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;招聘类型&#39;,&#xA;`job_type_detail` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;招聘具体类型&#39;,&#xA;`salary_range` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;薪酬范围&#39;,&#xA;`corporate_desc` LONGTEXT COLLATE utf8_bin COMMENT &#39;公司简介&#39;,&#xA;`job_desc` LONGTEXT COLLATE utf8_bin COMMENT &#39;职位简介&#39;,&#xA;`corporate_welfare`  VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司福利&#39;,&#xA;`corporate_address` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;公司地址&#39;,&#xA;`created_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` DATETIME NOT NULL,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>lCNLqV6teTPIIpCIvR+PhTQPA2f2W07EeGAf/9Gg23c=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2019-12-24T22:06:44+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2019-12-24T22:14:04+08:00</LastUpdatedTime>
</SqlMigrate>