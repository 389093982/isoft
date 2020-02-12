<SqlMigrate>
	<Id>75</Id>
	<AppId>5</AppId>
	<MigrateName>20200211161038_CREATE_ask_expert.sql</MigrateName>
	<MigrateSql>CREATE TABLE `ask_expert` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;,&#xA;`question` longtext COLLATE utf8_bin NOT NULL COMMENT &#39;问题内容&#39;,&#xA;`anser_number` INT(11) NOT NULL DEFAULT 0 COMMENT &#39;回答数&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>r9qi0wJMU06H6AzM47y1/ZUmfciHlhhr5tl6qDY1eag=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-11T16:10:38+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-11T16:26:37+08:00</LastUpdatedTime>
</SqlMigrate>