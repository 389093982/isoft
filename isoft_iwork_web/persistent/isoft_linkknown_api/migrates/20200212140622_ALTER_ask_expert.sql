<SqlMigrate>
	<Id>78</Id>
	<AppId>5</AppId>
	<MigrateName>20200212140622_ALTER_ask_expert.sql</MigrateName>
	<MigrateSql>alter table ask_expert change anser_number answer_number INT(11) NOT NULL DEFAULT 0 COMMENT &#39;回答数&#39;;&#xA;alter table answer_expert drop column bad_number;</MigrateSql>
	<MigrateHash>vZ9iGE1Rz01oAe0dlUttKsasDu9uydrtcr8I+Xeyykc=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-12T14:06:23+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-12T14:06:23+08:00</LastUpdatedTime>
</SqlMigrate>