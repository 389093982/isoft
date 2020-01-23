<SqlMigrate>
	<Id>54</Id>
	<AppId>5</AppId>
	<MigrateName>20200123105450_CREATE_verify_code.sql</MigrateName>
	<MigrateSql>CREATE TABLE `verify_code` (&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;,&#xA;`verify_code` INT(11) NOT NULL DEFAULT -1 COMMENT &#39;验证码&#39;,&#xA;verify_code_expired DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT &#39;验证码过期时间&#39;,&#xA;PRIMARY KEY (`user_name`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;&#xA;alter table user drop column verify_code;&#xA;alter table user drop column verify_code_expired;</MigrateSql>
	<MigrateHash>3xDTHpJGJSTKAw1Nx/CEqko5d9z9msYSZIMrt07WJEA=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-23T10:54:50+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-23T10:54:50+08:00</LastUpdatedTime>
</SqlMigrate>