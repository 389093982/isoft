
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class RegistPage extends StatefulWidget {

  @override
  _RegistPageState createState() => _RegistPageState();

}

class _RegistPageState extends State<RegistPage> with TickerProviderStateMixin {

  String gender = "";

  final TextEditingController _nickNameController = TextEditingController(text: "ok");
  final TextEditingController _userNameController = TextEditingController();
  final TextEditingController _verifyCodeController = TextEditingController();
  final TextEditingController _passwdController = TextEditingController();
  final TextEditingController _comfirmPasswdController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('注册'),
        centerTitle: true,
        backgroundColor: Colors.red,
        elevation: 0,
        brightness: Brightness.light,
        leading: Container( // 绘制返回键
            margin: EdgeInsets.all(10), // 设置边距
            child: IconButton(
              icon: Icon(
                Icons.arrow_back,
                size: 20,
              ),
              onPressed: () {
                // 返回首页
                NavigatorUtil.goMainPage(context);
              },
            )
        ),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.only(
            left: ScreenUtil().setWidth(80),
            right: ScreenUtil().setWidth(80),
            top: ScreenUtil().setWidth(30),
          ),
          child: Column(
            children: <Widget>[
              Container(
                // 居中对齐
                alignment: Alignment.center,
                margin: EdgeInsets.only(top: 30),
                // 设置图片
                child: Image.asset("images/linkknown.jpg", width: 80, height: 80,),
              ),
              VEmptyView(40),
              TextField(
                controller: _nickNameController,
                decoration: InputDecoration(
                  labelText: '昵称',
                ),
                onChanged: (String value) {
//                  blogTitle = value;
                },
              ),
              VEmptyView(40),
              TextField(
                controller: _userNameController,
                decoration: InputDecoration(
                  labelText: '账号[手机号/邮箱]',
                ),
                onChanged: (String value) {
//                  blogTitle = value;
                },
              ),
              TextField(
                controller: _verifyCodeController,
                decoration: InputDecoration(
                  labelText: '验证码',
                ),
                onChanged: (String value) {
//                  blogTitle = value;
                },
              ),
              TextField(
                controller: _passwdController,
                decoration: InputDecoration(
                  labelText: '密码',
                ),
                onChanged: (String value) {
//                  blogTitle = value;
                },
              ),
              TextField(
                controller: _comfirmPasswdController,
                decoration: InputDecoration(
                  labelText: '确认密码',
                ),
                onChanged: (String value) {
//                  blogTitle = value;
                },
              ),
              SizedBox(height: 10,),
              Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Radio(
                  value: "1",
                  activeColor: Colors.red,
                  groupValue: this.gender,
                  onChanged: (value) {
                    setState(() {
                      this.gender = value;
                    });
                  },
                ),
                Text('男'),
                SizedBox(width: 10,),
                Radio(
                  value: "2",
                  activeColor: Colors.red,
                  groupValue: this.gender,
                  onChanged: (value) {
                    setState(() {
                      this.gender = value;
                    });
                  },
                ),
                Text('女'),
              ],
            ),
              SizedBox(height: 10,),
              CommonButton(
                callback: () {
                  regist();
                },
                content: '注 册',
                width: double.infinity,
              ),
              VEmptyView(10),
              Container(
                alignment: Alignment.center,
                margin: EdgeInsets.only(top: 10),
                child: GestureDetector(
                  onTap: (){
                    NavigatorUtil.goLoginPage(context);
                  },
                  child: Text(
                    '已有账号？登录',
                    style: TextStyle(
                      color: Colors.grey,
                      fontSize: 18,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }


  regist(){
    UIUtils.showToast(_nickNameController.text);
  }

}