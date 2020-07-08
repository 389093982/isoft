<SqlMigrate>
	<Id>140</Id>
	<AppId>5</AppId>
	<MigrateName>20200707080324_CREATE_user_link_agent.sql</MigrateName>
	<MigrateSql>create table user_link_agent(&#xA;  id                    int auto_increment primary key,&#xA;  user_name             varchar(255) default &#39;&#39; not null comment &#39;用户名&#39;,&#xA;  agent_user_name              varchar(255) default &#39;&#39; not null comment &#39;上级代理账号&#39;,&#xA;  return_rate      decimal(9, 2) default &#39;0.00&#39; not null comment &#39;回报费率&#39;,&#xA;  settlement_time datetime                           not null comment &#39;结算日期&#39;,&#xA;  bind_time      datetime                           not null comment &#39;绑定时间&#39;,&#xA;  state varchar(255) default &#39;off&#39; not null comment &#39;状态&#39;&#xA;)&#xA;  collate = utf8_bin;</MigrateSql>
	<MigrateHash>MlKIcPNI9TutvpUj4E62Al9ebD0o7MC8MFt0IrMteo4=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-07-07T08:03:24+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-07-07T08:03:24+08:00</LastUpdatedTime>
</SqlMigrate>