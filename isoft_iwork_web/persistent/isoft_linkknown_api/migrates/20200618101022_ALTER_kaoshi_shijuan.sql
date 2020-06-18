<SqlMigrate>
	<Id>137</Id>
	<AppId>5</AppId>
	<MigrateName>20200618101022_ALTER_kaoshi_shijuan.sql</MigrateName>
	<MigrateSql>alter table kaoshi_shijuan add column kaoshi_start_time DATETIME NULL comment &#39;考试开始时间&#39;;&#xA;alter table kaoshi_shijuan add column kaoshi_end_time DATETIME NULL comment &#39;考试结束时间&#39;;&#xA;alter table kaoshi_classify add column classify_image VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;分类图片&#39;;</MigrateSql>
	<MigrateHash>1coRGfvBm2gk04Y8w806S0A9tRrsdyWjelxkKthSTO4=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-06-18T10:10:23+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-06-18T11:35:09+08:00</LastUpdatedTime>
</SqlMigrate>