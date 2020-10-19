~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~ powerd by linkknown ~~~~~~~~~~~~~~~~~~~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1、创建数据库： iwork
2、创建数据库账号： iwork/iwork123
    CREATE USER 'iwork'@'%' IDENTIFIED BY 'iwork123';
    GRANT ALL ON *.* TO 'iwork'@'%';
    FLUSH PRIVILEGES;

    update user set password=password('iwork123') where user='iwork' and host='%';
    flush privileges;

    update user set authentication_string=password('iwork123') where user='iwork' and host='%';
    flush privileges;
3、修改配置：iwork.persistent.dir