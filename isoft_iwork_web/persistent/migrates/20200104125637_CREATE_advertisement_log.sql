<SqlMigrate>
	<Id>50</Id>
	<MigrateName>20200104125637_CREATE_advertisement_log.sql</MigrateName>
	<MigrateSql>CREATE TABLE `advertisement_log` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`advertisement_id`  int(11) NOT NULL DEFAULT 0 COMMENT &#39;广告 id&#39;,&#xA;`access_ip` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;访问 ip&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;&#xA;alter table advertisement add column access_num int(11) NOT NULL DEFAULT 0 COMMENT &#39;访问次数&#39;;</MigrateSql>
	<MigrateHash>ZQSzBGYvwmQikHXVddSjd1XcsEjvM9ed5HJbm1udk6c=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-04T12:56:37+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-04T12:56:37+08:00</LastUpdatedTime>
</SqlMigrate>