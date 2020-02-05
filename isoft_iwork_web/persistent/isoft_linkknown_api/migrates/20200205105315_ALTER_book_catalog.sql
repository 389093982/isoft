<SqlMigrate>
	<Id>64</Id>
	<AppId>5</AppId>
	<MigrateName>20200205105315_ALTER_book_catalog.sql</MigrateName>
	<MigrateSql>alter table book_catalog drop column created_by;&#xA;alter table book_catalog drop column created_time;&#xA;alter table book_article drop column created_by;&#xA;alter table book_article drop column created_time;&#xA;alter table book_catalog add column catalog_order int(11) NOT NULL DEFAULT 0 COMMENT &#39;顺序&#39;;</MigrateSql>
	<MigrateHash>b5Rj/YeSs/CFaZ2Xv2m/NGbGDZTs8BmKinGM3vSE0gI=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-05T10:53:15+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-05T10:53:15+08:00</LastUpdatedTime>
</SqlMigrate>