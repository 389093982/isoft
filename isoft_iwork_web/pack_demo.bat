% start 命令用于在 DOS 下打开系统应用程序 %
% x即解压 %
% -y：表示覆盖相同文件 %
start winrar x -y ./isoft_iwork_web.tar.gz ./temp_isoft_iwork_web/


% 延迟 5s 执行 %
choice /t 5 /d y /n >nul
% 修改操作 %
% 删除文件夹 %
rd /s /q .\temp_isoft_iwork_web\persistent\isoft_linkknown_api
% 删除文件 %
del /f /q .\temp_isoft_iwork_web\conf\app.conf
del /f /q .\temp_isoft_iwork_web\conf\dev.conf
del /f /q .\temp_isoft_iwork_web\conf\test.conf
del /f /q .\temp_isoft_iwork_web\conf\prod.conf
move .\temp_isoft_iwork_web\conf\template\*.conf .\temp_isoft_iwork_web\conf\
rd /s /q .\temp_isoft_iwork_web\conf\template


% a是压缩命令,添加文件到压缩文件 %
% ag[格式]  使用当前日期生成压缩文件名 %
% k 锁定压缩文件 %
% r 递归子目录 %
% s 创建固实压缩文件 %
% ep1:排除基准文件夹，不然压缩包会包含待压缩文件夹所在的完整路径 %
% o+:覆盖已经存在的文件 %
% p:密码，后面红色的部分就是密码，紧贴这个参数P，看起来有点怪 %
% inul:禁止出错信息 %
% r:连同子文件夹操作 %
% ibck:后台模式运行 %
start winrar a -ep1 -o+ -inul -ag -r -s -ibck ./isoft_iwork_web.zip ./temp_isoft_iwork_web/
