<SqlMigrate>
	<Id>56</Id>
	<AppId>7</AppId>
	<MigrateName>20200123182645_CREATE_verify_code.sql</MigrateName>
	<MigrateSql>CREATE TABLE `verify_code` (&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;用户名&#39;,&#xA;`verify_code` INT(11) NOT NULL DEFAULT -1 COMMENT &#39;验证码&#39;,&#xA;verify_code_expired DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT &#39;验证码过期时间&#39;,&#xA;PRIMARY KEY (`user_name`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>kvFK6n6Y9sovaRcVYhVmX3yRCXxdeGkQ4UoYFIsrD/I=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-23T18:26:46+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-23T18:28:37+08:00</LastUpdatedTime>
</SqlMigrate>