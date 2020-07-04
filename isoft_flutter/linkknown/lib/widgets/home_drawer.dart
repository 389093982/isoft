
import 'dart:async';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class HomeDrawerWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        HomeDrawerHeaderWidget(),
        HomeContentHeaderWidget(),
      ],
    );
  }

}

class HomeDrawerHeaderWidget extends StatefulWidget {

  @override
  _HomeDrawerHeaderState createState() => _HomeDrawerHeaderState();

}

class _HomeDrawerHeaderState extends State<HomeDrawerHeaderWidget> {

  bool hasLogin = false;
  StreamSubscription _subscription;

  @override
  void initState() {
    super.initState();
    //订阅 eventbus
    _subscription = eventBus.on<LoginSuccessEvent>().listen((event) {
      setState(() {
        this.hasLogin = true;
      });
    });

    LoginUtil.checkHasLogin().then((value) => {
      if (value) {
        this.setState(() {
          this.hasLogin = true;
        })
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
    // 取消订阅
    if (_subscription != null) {
      _subscription.cancel();
      _subscription = null;
    }
  }

  @override
  Widget build(BuildContext context) {
    // flutter 中 container 如何撑满宽度
    // 1、child使用Row, 不光是container,其他的 card 等也可以内部使用 Row 来撑满
    // 2、给container设置alignment属性
    return Container(
      alignment: Alignment.center,
      height: 200,
      color: Colors.red,
      child: hasLogin ? getLoginWidget() : getUnLoginWidget(context),
    );
  }

  Widget getLoginWidget () {
    return Container(
      decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.red, Colors.redAccent, Colors.blue],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          )),
      child: Stack(
        children: <Widget>[
          // 已登录布局
          Align(
            alignment: Alignment.centerLeft,
            child: Text("1111111222222222fffffffff"),
          ),
          Align(
            alignment: Alignment.bottomCenter,
            child: Text("名人名言"),
          ),
        ],
      ),
    );
  }

  Widget getUnLoginWidget (BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.red, Colors.redAccent, Colors.blue],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          )),
      child: Stack(
        children: <Widget>[
          // 未登录布局
          Align(
            alignment: Alignment.center,
            // GestureDetector在Flutter中负责处理跟用户的简单手势交互
            child: GestureDetector(
              // 点击事件
              onTap: (){
                NavigatorUtil.goLoginPage(context);
              },
              child: Text(
                "未登录，请先登录！",
                style: TextStyle(fontSize: 24, color: Colors.white),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class HomeContentHeaderWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        ListTile(
          title: Text('我的课程'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            UIUtils.showToast("我的课程");
          },
        ),
        ListTile(
          title: Text('订单列表'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            UIUtils.showToast("订单列表");
          },
        ),
        ListTile(
          title: Text('购物车'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            UIUtils.showToast("购物车");
          },
        ),
        ListTile(
          title: Text('登陆'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            NavigatorUtil.goLoginPage(context);
          },
        ),
        ListTile(
          title: Text('注册'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            NavigatorUtil.goRegistPage(context);
          },
        ),
        ListTile(
          title: Text('退出'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            // 退出并重新跳往登录页面
            LoginUtil.logout();
            NavigatorUtil.goLoginPage(context);
         },
        ),
        ListTile(
          title: Text('分享'),
          leading: Image.asset("images/linkknown.jpg", width: 30, height: 30,),
          onTap: (){
            UIUtils.showToast("分享");
          },
        ),
      ],
    );
  }

}