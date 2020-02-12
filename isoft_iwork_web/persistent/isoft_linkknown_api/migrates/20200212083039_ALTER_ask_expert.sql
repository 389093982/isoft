<SqlMigrate>
	<Id>76</Id>
	<AppId>5</AppId>
	<MigrateName>20200212083039_ALTER_ask_expert.sql</MigrateName>
	<MigrateSql>alter table ask_expert add column short_desc varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;问题简述&#39;;&#xA;CREATE TABLE `anser_expert` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`question_id` BIGINT(20) NOT NULL COMMENT &#39;问题 id&#39;,&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;,&#xA;`anser` longtext COLLATE utf8_bin NOT NULL COMMENT &#39;回答内容&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>EJtip9AMejo59MPKzgcFj8L9IcQjQ6MFjkMob1i5qno=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-12T08:30:40+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-12T08:30:40+08:00</LastUpdatedTime>
</SqlMigrate>