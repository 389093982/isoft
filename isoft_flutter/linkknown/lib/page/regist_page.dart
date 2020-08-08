
import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/check_param_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class RegistPage extends StatefulWidget {

  @override
  _RegistPageState createState() => _RegistPageState();

}

class _RegistPageState extends State<RegistPage> with TickerProviderStateMixin {

  final TextEditingController _nickNameController = TextEditingController(text: "");
  final TextEditingController _userNameController = TextEditingController();
  final TextEditingController _verifyCodeController = TextEditingController();
  final TextEditingController _passwdController = TextEditingController();
  final TextEditingController _comfirmPasswdController = TextEditingController();
  String _gender = "";

  String verifyCodeTipText = "获取验证码";
  Color verifyCodeTipText_Color = Colors.black;

  bool passwordVisibility = false;

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
      body: ScrollConfiguration(
        behavior: NoShadowScrollBehavior(),
        child: SingleChildScrollView(
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
                    hintText: '昵称',
                    prefixIcon: Icon(
                      Icons.account_box,
                      color: Colors.grey,
                    ),
                  ),
                ),
                SizedBox(height: 10,),
                TextField(
                  controller: _userNameController,
                  decoration: InputDecoration(
                      hintText: '账号',
                      prefixIcon: Icon(
                        Icons.account_circle,
                        color: Colors.grey,
                      )
                  ),
                ),
                SizedBox(height: 10,),
                Stack(
                  children: <Widget>[
                    TextField(
                      controller: _verifyCodeController,
                      decoration: InputDecoration(
                        hintText: '验证码',
                        prefixIcon: Icon(
                          Icons.verified_user,
                          color: Colors.grey,
                        ),
                      ),
                    ),
                    Align(
                      alignment: Alignment.topRight,
                      child: Container(
                        alignment: Alignment.topRight,
                        width: 120,
                        height: 35,
                        margin: EdgeInsets.only(top: 20),
                        child: GestureDetector(
                          onTap: (){
                            createVerifyCode();
                          },
                          child: Text(verifyCodeTipText,style: TextStyle(fontSize: 16,color: verifyCodeTipText_Color),),
                        ),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 10,),
                TextField(
                  obscureText: !passwordVisibility,   // 是否是密码,和 visibility 相反
                  controller: _passwdController,
                  decoration: InputDecoration(
                    hintText: '密码',
                    prefixIcon: Icon(
                      Icons.lock,
                      color: Colors.grey,
                    ),
                    suffixIcon: GestureDetector(
                      onTap: () {
                        setState(() {
                          passwordVisibility = !passwordVisibility;
                        });
                      },
                      child: Icon(
                        passwordVisibility ? Icons.visibility : Icons.visibility_off,
                        color: Colors.grey,
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 10,),
                TextField(
                  obscureText: true,
                  controller: _comfirmPasswdController,
                  decoration: InputDecoration(
                    hintText: '确认密码',
                    prefixIcon: Icon(
                      Icons.lock,
                      color: Colors.grey,
                    ),
                  ),
                ),
                SizedBox(height: 10,),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Radio(
                      value: "male",
                      activeColor: Colors.red,
                      groupValue: this._gender,
                      onChanged: (value) {
                        setState(() {
                          this._gender = value;
                        });
                      },
                    ),
                    Text('男'),
                    SizedBox(width: 10,),
                    Radio(
                      value: "female",
                      activeColor: Colors.red,
                      groupValue: this._gender,
                      onChanged: (value) {
                        setState(() {
                          this._gender = value;
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
                      '已有账号,前去登录',
                      style: TextStyle(
                        color: Colors.black54,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }


  //发送验证码
  createVerifyCode(){
    String userName = _userNameController.text;

    //userName
    if(StringUtil.checkEmpty(userName)){
      UIUtil.showToast("请填写账号");
      return;
    }
    if (!CheckParamUtil.checkRegex(userName, CheckParamUtil.REGEX_EMAIL) && !CheckParamUtil.checkRegex(userName, CheckParamUtil.REGEX_PHONE)) {
      UIUtil.showToast("请使用手机号或邮箱注册");
      return;
    }

    //灰色tip文字，则不允许再次发送
    if(verifyCodeTipText_Color == Colors.black45){
      return;
    }
    //首次点击就该置灰
    verifyCodeTipText_Color = Colors.black45;
    setState(() {

    });

    LinkKnownApi.createVerifyCode(userName).then((value){
      if(value.status=="SUCCESS"){
        verifyCodeTime();
        UIUtil.showToast("发送成功");
      }else{
        UIUtil.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
  }


  Timer timer;

  //倒计时60秒
  verifyCodeTime(){
    int verifyCodeSecondCount = 60;
    timer = Timer.periodic(Duration(seconds: 1), (Timer t) {
      setState(() {
        if (verifyCodeSecondCount > 58) {
          verifyCodeTipText = "发送中...";
        } else {
          verifyCodeTipText = "${verifyCodeSecondCount}s后重新获取";
        }
        verifyCodeSecondCount --;
        if(verifyCodeSecondCount==0){
          verifyCodeTipText = "获取验证码";
          verifyCodeTipText_Color = Colors.black;
          timer?.cancel();
        }
      });
    });
  }

  @override
  void dispose() {
    timer?.cancel();
    _nickNameController?.dispose();
    _userNameController?.dispose();
    _verifyCodeController?.dispose();
    _passwdController?.dispose();
    _comfirmPasswdController?.dispose();
    super.dispose();
  }

  //注册
  regist(){
    String nickName = _nickNameController.text;
    String userName = _userNameController.text;
    String verifyCode = _verifyCodeController.text;
    String passwd = _passwdController.text;
    String comfirmPasswd = _comfirmPasswdController.text;
    String gender = _gender;

    //nickName
    if(StringUtil.checkEmpty(nickName)){
      UIUtil.showToast("昵称不能为空");
      return;
    }
    if(nickName.length>10){
      UIUtil.showToast("昵称长度不能超过10个字符");
      return;
    }

    //userName
    if(StringUtil.checkEmpty(userName)){
      UIUtil.showToast("请填写账号");
      return;
    }
    if (!CheckParamUtil.checkRegex(userName, CheckParamUtil.REGEX_EMAIL) && !CheckParamUtil.checkRegex(userName, CheckParamUtil.REGEX_PHONE)) {
      UIUtil.showToast("请使用手机号或邮箱注册");
      return;
    }

    //verifyCode
    if(StringUtil.checkEmpty(verifyCode) || verifyCode.length!=6){
      UIUtil.showToast("请填写6位验证码");
      return;
    }

    //passwd
    if(StringUtil.checkEmpty(passwd)){
      UIUtil.showToast("请填写密码");
      return;
    }
    if (!CheckParamUtil.checkRegex(passwd, CheckParamUtil.REGEX_PASSWD)) {
      UIUtil.showToast("密码必须由数字或字母组合，长度 6-20");
      return;
    }

    //comfirmPasswd
    if(StringUtil.checkEmpty(comfirmPasswd)){
      UIUtil.showToast("请填写确认密码");
      return;
    }
    if (comfirmPasswd!=passwd) {
      UIUtil.showToast("两次密码输入不一致");
      return;
    }

    //gender
    if(StringUtil.checkEmpty(gender)){
      UIUtil.showToast("请选择性别");
      return;
    }


    LinkKnownApi.regist(userName, passwd, nickName, gender, verifyCode, "linkknown").then((value){
        if(value.status=="SUCCESS"){
          UIUtil.showToast("注册成功");
          NavigatorUtil.goRouterPage(context, Routes.login);
        }else{
          UIUtil.showToast(value.errorMsg);
        }
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
  }

}