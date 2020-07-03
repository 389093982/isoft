import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:provider/provider.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> with TickerProviderStateMixin {

  Animation<double> _animation;
  AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller =
        AnimationController(vsync: this, duration: Duration(milliseconds: 300));
    _animation = CurvedAnimation(parent: _controller, curve: Curves.linear);
    Future.delayed(Duration(milliseconds: 500), () {
      _controller.forward();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('登录'),
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
                NavigatorUtil.goHomePage(context);
//                Navigator.pop(context); // 关闭当前页面--
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
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Hero(
                tag: 'logo',
                child: Image.asset(
                  'images/linkknown.jpg',
                  width: ScreenUtil().setWidth(90),
                  height: ScreenUtil().setWidth(90),
                ),
              ),
              _LoginAnimatedWidget(
                animation: _animation,
              ),
            ],
          ),
        ),
      ),
    );
  }

}


class _LoginWidget extends StatefulWidget {
  @override
  __LoginWidgetState createState() => __LoginWidgetState();
}

class __LoginWidgetState extends State<_LoginWidget> {
  final TextEditingController _userNameController = TextEditingController();
  final TextEditingController _passwdController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Theme(
      data: ThemeData(primaryColor: Colors.red),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Container(
            // 居中对齐
            alignment: Alignment.center,
            margin: EdgeInsets.only(top: ScreenUtil().setWidth(30)),
            // 设置图片
            child: Image.asset("images/linkknown.jpg", width: 80, height: 80,),
          ),
          VEmptyView(50),
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
          VEmptyView(40),
          TextField(
            obscureText: true,
            controller: _passwdController,
            decoration: InputDecoration(
                hintText: '密码',
                prefixIcon: Icon(
                  Icons.lock,
                  color: Colors.grey,
                )),
          ),
          VEmptyView(120),
          Consumer<UserModel>(
            builder: (BuildContext context, UserModel value, Widget child) {
              return CommonButton(
                callback: () {
                  String userName = _userNameController.text;
                  String passwd = _passwdController.text;
                  if (userName.isEmpty || passwd.isEmpty) {
                    UIUtils.showToast('请输入账号或者密码');
                    return;
                  }
                  value.postLogin(
                    userName,
                    passwd,
                    'http://www.linkknown.com'
                  ).catchError((e) {
                    UIUtils.showToast((e as LinkKnownError).errorMsg);
                  }).then((value){
                    if(value != null){
                      NavigatorUtil.goMainPage(context);
                    }
                  });
                },
                content: '登录',
                width: double.infinity,
              );
            },
          ),
          VEmptyView(10),
          Container(
            alignment: Alignment.center,
            margin: EdgeInsets.only(top: ScreenUtil().setWidth(30)),
            child: GestureDetector(
              onTap: (){
                NavigatorUtil.goRegistPage(context);
              },
              child: Text(
                '没有账号？注册',
                style: TextStyle(
                  color: Colors.grey,
                  fontSize: 18,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _LoginAnimatedWidget extends AnimatedWidget {
  // 透明度
  final Tween<double> _opacityTween = Tween(begin: 0, end: 1);
  // 偏移量
  final Tween<double> _offsetTween = Tween(begin: 40, end: 0);
  final Animation animation;

  _LoginAnimatedWidget({
    @required this.animation,
  }) : super(listenable: animation);

  @override
  Widget build(BuildContext context) {
    // Opacity 也是组件
    return Opacity(
      opacity: _opacityTween.evaluate(animation),
      child: Container(
        margin: EdgeInsets.only(top: _offsetTween.evaluate(animation)),
        child: _LoginWidget(),
      ),
    );
  }
}

