<SqlMigrate>
	<Id>77</Id>
	<AppId>5</AppId>
	<MigrateName>20200212104334_ALTER_answer_expert.sql</MigrateName>
	<MigrateSql>rename table anser_expert to answer_expert;&#xA;alter table answer_expert change anser answer longtext COLLATE utf8_bin NOT NULL COMMENT &#39;回答内容&#39;;&#xA;alter table answer_expert add column good_number int(11) NOT NULL DEFAULT 0 COMMENT &#39;好评数&#39;;&#xA;alter table answer_expert add column bad_number int(11) NOT NULL DEFAULT 0 COMMENT &#39;差评数&#39;;</MigrateSql>
	<MigrateHash>Z+HmicAx6lFNx7+zaPAZE7aRAvErQkxQRXq5omhuQmg=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-12T10:43:35+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-12T10:43:35+08:00</LastUpdatedTime>
</SqlMigrate>