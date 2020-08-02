<SqlMigrate>
	<Id>141</Id>
	<AppId>5</AppId>
	<MigrateName>20200802100842_CREATE_app_version.sql</MigrateName>
	<MigrateSql>create table app_version(&#xA;  id int auto_increment primary key,&#xA;  app_show_version varchar(255) default &#39;&#39; not null comment &#39;app显示版本&#39;,&#xA;  app_padding_version varchar(255) default &#39;&#39; not null comment &#39;app对比版本,左补0&#39;,&#xA;  android_download_url  varchar(500) default &#39;&#39; not null comment &#39;app下载地址&#39;,&#xA;  ios_download_url  varchar(500) default &#39;&#39; not null comment &#39;app下载地址&#39;,&#xA;  created_by VARCHAR(255) COLLATE utf8_bin NOT NULL DEFAULT &#39;&#39;,&#xA;  force_update varchar(10) default &#39;Y&#39; not null comment &#39;是否强制更新,Y强制更新,非Y不强制更新&#39;,&#xA;  created_time DATETIME NOT NULL&#xA;)&#xA;  collate = utf8_bin;</MigrateSql>
	<MigrateHash>s521QDfld2sX8ZXH641leLlAA+tDBOzC1EfC0Vqb6w0=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-08-02T10:08:43+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-08-02T10:12:08+08:00</LastUpdatedTime>
</SqlMigrate>