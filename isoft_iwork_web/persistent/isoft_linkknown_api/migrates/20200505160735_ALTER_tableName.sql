<SqlMigrate>
	<Id>118</Id>
	<AppId>5</AppId>
	<MigrateName>20200505160735_ALTER_tableName.sql</MigrateName>
	<MigrateSql>rename table ask_expert to expert_ask;&#xA;rename table answer_expert to expert_answer;&#xA;drop table app_register;&#xA;drop table configuration;&#xA;rename table corporate_detail to job_corporate_detail;&#xA;rename table element to placement_element;&#xA;rename table favorite to common_favorite;&#xA;rename table fileupload_log to common_fileupload_log;&#xA;rename table history to common_history;&#xA;rename table good to goods;&#xA;rename table login_record to user_login_record;&#xA;rename table verify_code to user_verify_code;&#xA;rename table vote_tag to common_vote_tag;&#xA;rename table vote_taglog to common_vote_taglog;</MigrateSql>
	<MigrateHash>Pv1hllk8ouMVlgiNw12R8DWjbdtyuucb7NMT1LBvsko=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-05-05T16:07:35+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-05-05T16:07:35+08:00</LastUpdatedTime>
</SqlMigrate>