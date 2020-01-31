<SqlMigrate>
	<Id>58</Id>
	<AppId>5</AppId>
	<MigrateName>20200129122242_CREATE_job_apply.sql</MigrateName>
	<MigrateSql>CREATE TABLE `job_apply` (&#xA;`job_id` INT(11) NOT NULL DEFAULT -1 COMMENT &#39;岗位 id&#39;,&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;投递用户信息&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`job_id`,`user_name`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>mOhaSyKghTH1fYgErekjpa/cXFpR//3hCyu8Grtxt6I=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-29T12:22:43+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-29T12:22:43+08:00</LastUpdatedTime>
</SqlMigrate>