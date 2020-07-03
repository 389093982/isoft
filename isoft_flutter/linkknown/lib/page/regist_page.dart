
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class RegistPage extends StatefulWidget {

  @override
  _RegistPageState createState() => _RegistPageState();

}

class _RegistPageState extends State<RegistPage> with TickerProviderStateMixin {

  final TextEditingController _nickNameController = TextEditingController();
  final TextEditingController _userNameController = TextEditingController();

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
                Icons.arrow_back_ios,
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
          padding: EdgeInsets.all(10),
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
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '昵称',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              TextField(
                controller: _userNameController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '账号',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              TextField(
                controller: _userNameController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '验证码',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              TextField(
                controller: _userNameController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '密码',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              TextField(
                controller: _userNameController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '确认密码',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              TextField(
                controller: _userNameController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                    hintText: '性别',
                    prefixIcon: Icon(
                      Icons.account_circle,
                      color: Colors.grey,
                    )),
              ),
              CommonButton(
                callback: () {

                },
                content: '注册',
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

}