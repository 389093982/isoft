<SqlMigrate>
	<Id>82</Id>
	<AppId>5</AppId>
	<MigrateName>20200218170456_ALTER_course.sql</MigrateName>
	<MigrateSql>alter table course change  column updated_by last_updated_by varchar(255);&#xA;alter table course change  column updated_time last_updated_time varchar(255);</MigrateSql>
	<MigrateHash>wwzDOYjcnJKgOwGVnc5FS9pxSvmPEh9lQ8Bx7O+HA54=</MigrateHash>
	<Effective>true</Effective>
	<CreatedBy>SYSTEM</CreatedBy>
	<CreatedTime>2020-02-18T17:04:56+08:00</CreatedTime>
	<LastUpdatedBy>SYSTEM</LastUpdatedBy>
	<LastUpdatedTime>2020-02-18T17:04:56+08:00</LastUpdatedTime>
</SqlMigrate>