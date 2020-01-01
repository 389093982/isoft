<SqlMigrate>
	<Id>48</Id>
	<MigrateName>20191229115548_CREATE_job_resume.sql</MigrateName>
	<MigrateSql>CREATE TABLE `job_resume` (&#xA;`id` BIGINT(20) NOT NULL AUTO_INCREMENT,&#xA;`head_img` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;头像&#39;,&#xA;`user_name` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;姓名&#39;,&#xA;`age` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;年龄&#39;,&#xA;`sex` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;性别&#39;,&#xA;`job_start_time` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;开始工作时间&#39;,&#xA;`contact` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;联系方式&#39;,&#xA;`email` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;邮箱&#39;,&#xA;`birthday` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;出生年月&#39;,&#xA;`personal_skills` LONGTEXT COLLATE utf8_bin COMMENT &#39;个人技能&#39;,&#xA;`project_experiences` LONGTEXT COLLATE utf8_bin COMMENT &#39;项目经验&#39;,&#xA;`other_characters` LONGTEXT COLLATE utf8_bin COMMENT &#39;其它优势&#39;,&#xA;`education` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;学历&#39;,&#xA;`employment_status` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;就业状态&#39;,&#xA;`graduate_school` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;毕业学校&#39;,&#xA;`expectant_salary` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;期望薪资&#39;,&#xA;`job_area` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;期望地点&#39;,&#xA;`current_situation` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;当前状况&#39;,&#xA;`personal_hobbies` LONGTEXT COLLATE utf8_bin COMMENT &#39;个人爱好&#39;,&#xA;`created_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`created_time` DATETIME NOT NULL,&#xA;`last_updated_by` VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;`last_updated_time` DATETIME NOT NULL,&#xA;PRIMARY KEY (`id`)&#xA;) ENGINE=INNODB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;</MigrateSql>
	<MigrateHash>DWGdOPOGbiQzE3vvHLd/Fah+x9/J0NSYHUPA9yVIRCk=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2019-12-29T11:55:49+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-01T15:11:24+08:00</LastUpdatedTime>
</SqlMigrate>