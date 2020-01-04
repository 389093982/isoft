<SqlMigrate>
	<Id>49</Id>
	<MigrateName>20200104100327_ALTER_book_catalog.sql</MigrateName>
	<MigrateSql>alter table book_catalog add column grades varchar(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;完整目录等级&#39;;&#xA;alter table book_catalog add column grade_1 int(11) NOT NULL DEFAULT 0 COMMENT &#39;一级目录&#39;;&#xA;alter table book_catalog add column grade_2 int(11) NOT NULL DEFAULT 0 COMMENT &#39;二级目录&#39;;&#xA;alter table book_catalog add column grade_3 int(11) NOT NULL DEFAULT 0 COMMENT &#39;三级目录&#39;;</MigrateSql>
	<MigrateHash>QISNcjGMw2cAf+aN6gVaqLSHitUCIQFooHqoP3mjues=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-01-04T10:03:28+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-01-04T10:03:28+08:00</LastUpdatedTime>
</SqlMigrate>