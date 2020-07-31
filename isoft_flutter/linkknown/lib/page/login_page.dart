import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/utils/login_util.dart';
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
        leading: Container(
            // 绘制返回键
            margin: EdgeInsets.all(10), // 设置边距
            child: IconButton(
              icon: Icon(
                Icons.arrow_back,
                size: 20,
              ),
              onPressed: () {
                // 返回首页
                NavigatorUtil.goMainPage(context);
//                Navigator.pop(context); // 关闭当前页面--
              },
            )),
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
  var _userName = "";
  var _password = "";

  @override
  void initState() {
    super.initState();
    fillAccountFromMemory();
  }

  // 异步填充表单(自动填充登录信息)
  void fillAccountFromMemory() async {
    _userName = await LoginUtil.getUserName();
    _password = await LoginUtil.getPasswd();
    // 表单填充新值
    _userNameController.value = new TextEditingValue(text: _userName ?? "");
    _passwdController.value = new TextEditingValue(text: _password ?? "");
  }

  _usernameChange() {
    _userName = _userNameController.text;
  }

  _passwordChange() {
    _password = _passwdController.text;
  }

  @override
  void dispose() {
    super.dispose();
    _userNameController.removeListener(_usernameChange);
    _passwdController.removeListener(_passwordChange);
  }

  postLogin() {
    if (_userName == null || _userName.isEmpty) {
      UIUtils.showToast('请输入账号');
      return;
    }
    if (_password.isEmpty || _password.isEmpty) {
      UIUtils.showToast('请输入密码');
      return;
    }

    LinkKnownApi.postLogin(_userName, _password, 'http://www.linkknown.com')
        .then((value) {
      if (value != null) {
        if (value.status == "SUCCESS") {
          LoginUtil.memoryAccount(_userName, _password, value);

          LinkKnownApi.updateTokenString();

          UIUtils.showToast("登录成功！");
          NavigatorUtil.goBack(context);

          eventBus.fire(LoginStateChangeEvent());

          //查询用户基本信息
          getUserDetail(_userName);
        } else {
          UIUtils.showToast("登录失败！" + value.errorMsg);
        }
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }

  //查询用户基本信息
  getUserDetail(String userName) {
    LinkKnownApi.getUserDetail(userName).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if (value != null) {
        if (value.status == "SUCCESS") {
          LoginUtil.memoryUserDetail(value);
        }
      }
    });
  }

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
            child: Image.asset(
              "images/linkknown.jpg",
              width: 80,
              height: 80,
            ),
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
            onChanged: (String value) {
              _userName = value;
            },
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
            onChanged: (String value) {
              _password = value;
            },
          ),
          VEmptyView(120),
          CommonButton(
            callback: postLogin,
            content: '登录',
            width: double.infinity,
          ),
          VEmptyView(10),
          Container(
            alignment: Alignment.center,
            margin: EdgeInsets.only(top: 10),
            child: GestureDetector(
              onTap: () {
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
