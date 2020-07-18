import 'package:flutter/material.dart';
import 'package:linkknown/utils/cache_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class SettingPage extends StatefulWidget {
  @override
  _SettingState createState() => _SettingState();
}

class _SettingState extends State<SettingPage> {
  String cacheInfo = "";

  @override
  void initState() {
    super.initState();

    initData();
  }

  initData() async {
    String _chacheIfo = await getCacheSize();
    setState(() {
      cacheInfo = "已使用空间 ${_chacheIfo}";
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('设置'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      // 添加一层  new Builder 解决 snackbar 报错
      body: new Builder(builder: (BuildContext context) {
        return ListView(
          children: <Widget>[
            SettingItemWidget("头像", "设置我的头像"),
            SettingItemWidget("用户信息", "编辑用户基本信息"),
            SettingItemWidget("个性签名", "编辑个性签名"),
            SettingItemWidget(
              "清除缓存",
              cacheInfo,
              clickCallBack: () async {
                String _cacheInfo = await clearCache();
                setState(() {
                  cacheInfo = "清理完成，使用空间 ${_cacheInfo}";
                });
              },
            ),
            SettingItemWidget("用户协议", "查看用户协议"),
            SettingItemWidget("版本更新", "检查版本更新"),
            SettingItemWidget(
              "退出登录",
              "退出链知 app 账号",
              clickCallBack: () {
                // 登出
                LoginUtil.logout();
                // snackbar 弹框提示
                // 当BuildContext在Scaffold之前时，调用Scaffold.of(context)会报错
                final snackBar = new SnackBar(
                    backgroundColor: Colors.red,
                    duration: Duration(seconds: 2),
                    content: new Text('账号退出成功!'));
                Scaffold.of(context).showSnackBar(snackBar);
                // 延迟 2 s 后跳往登陆页面
                Future.delayed(Duration(seconds: 3), () {
                  NavigatorUtil.goLoginPage(context);
                });
              },
            ),
          ],
        );
      }),
    );
  }
}

class SettingItemWidget extends StatefulWidget {
  String header;
  String text;
  VoidCallback clickCallBack;

  SettingItemWidget(this.header, this.text, {this.clickCallBack});

  @override
  SettingItemState createState() => SettingItemState();
}

class SettingItemState extends State<SettingItemWidget> {
  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: widget.clickCallBack,
      child: Container(
        padding: EdgeInsets.all(15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              widget.header,
              style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w400,
                  color: Colors.black),
            ),
            Text(
              widget.text,
              style: TextStyle(color: Colors.grey[600]),
            ),
          ],
        ),
      ),
    );
  }
}
