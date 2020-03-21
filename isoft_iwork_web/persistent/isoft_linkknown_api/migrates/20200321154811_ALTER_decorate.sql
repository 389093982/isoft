<SqlMigrate>
	<Id>102</Id>
	<AppId>5</AppId>
	<MigrateName>20200321154811_ALTER_decorate.sql</MigrateName>
	<MigrateSql>alter table decorate drop column decorate_icon;&#xA;alter table decorate_item modify decorate_text  longtext COLLATE utf8_bin NOT NULL COMMENT &#39;装饰文本&#39;;&#xA;alter table decorate_item modify link_href varchar(500) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;链接地址&#39;;&#xA;alter table good add column highlights varchar(4000) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39; COMMENT &#39;亮点介绍&#39;;</MigrateSql>
	<MigrateHash>g9QF4n800U4N8YoKUXYvcZaGPM+pLl4pRAoD6uWzPAU=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-03-21T15:48:12+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-03-21T15:48:12+08:00</LastUpdatedTime>
</SqlMigrate>