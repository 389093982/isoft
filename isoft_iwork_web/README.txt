~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~ powerd by linkknown ~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1、创建数据库： iwork_db
2、创建数据库账号： iwork/iwork_demo
    CREATE USER 'iwork'@'%' IDENTIFIED BY 'iwork_demo';
    GRANT ALL ON *.* TO 'iwork'@'%';
    FLUSH PRIVILEGES;

    update user set password=password('iwork_demo') where user='iwork' and host='%';
    flush privileges;
3、修改配置：iwork.persistent.dir