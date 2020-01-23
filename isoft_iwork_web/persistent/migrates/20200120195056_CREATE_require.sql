<SqlMigrate>
	<Id>53</Id>
	<AppId>6</AppId>
	<MigrateName>20200120195056_CREATE_require.sql</MigrateName>
	<MigrateSql>CREATE TABLE `requires` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`require_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求名称&#39;,&#xA;`require_detail` VARCHAR(4000) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求描述&#39;,&#xA;`require_start_time` DATE NOT NULL COMMENT &#39;需求开始时间&#39;,&#xA;`require_end_time` DATE NOT NULL COMMENT &#39;需求结束时间&#39;,&#xA;`require_status` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求状态&#39;,&#xA;`require_owner` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求责任人&#39;,&#xA;`require_user` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求实现人&#39;,&#xA;`require_mark` VARCHAR(4000) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;需求备注&#39;,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>cejn/VXRV0gvLuLYKN9ri226bmKsEZKE63sdeLe193o=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-20T19:50:57+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-23T08:42:25+08:00</LastUpdatedTime>
</SqlMigrate>