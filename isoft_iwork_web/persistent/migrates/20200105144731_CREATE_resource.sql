<SqlMigrate>
	<Id>52</Id>
	<MigrateName>20200105144731_CREATE_resource.sql</MigrateName>
	<MigrateSql>CREATE TABLE `resource` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`resource_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;资源名称&#39;,&#xA;`resource_desc` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;资源描述&#39;,&#xA;`resource_path` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;资源访问地址&#39;,&#xA;`downloads` int(11) NOT NULL DEFAULT 0 COMMENT &#39;下载次数&#39;,&#xA;`points` int(11) NOT NULL DEFAULT 0 COMMENT &#39;下载所需积分&#39;,&#xA;`created_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` DATETIME NOT NULL,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>ek2nO3T+yeUN67uUTQwwLXgpy6zJ2Ah8qbbNg7hXeOA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-05T14:47:32+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-07T21:09:52+08:00</LastUpdatedTime>
</SqlMigrate>