import 'dart:async';

import 'package:flutter/material.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/header_icon.dart';
import 'package:linkknown/widgets/ming_yan.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:share/share.dart';

class HomeDrawerWidget extends StatefulWidget {
  @override
  _HomeDrawerWidgetState createState() => _HomeDrawerWidgetState();
}

class _HomeDrawerWidgetState extends State<HomeDrawerWidget> with TickerProviderStateMixin {
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
  String headerIcon = "";
  String nickName = "";
  String userName = "";

  StreamSubscription subscription;

  @override
  void initState() {
    super.initState();

    refreshLoginStatus();

    subscription = eventBus.on<LoginStateChangeEvent>().listen((event) {
      refreshLoginStatus();
   });
  }

  refreshLoginStatus() async {
    bool hasLogin = await LoginUtil.checkHasLogin();
    if (hasLogin) {
      String nickName = await LoginUtil.getNickName();
      String headerIcon = await LoginUtil.getSmallIcon();
      String userName = await LoginUtil.getLoginUserName();
      setState(() {
        this.hasLogin = true;
        this.nickName = nickName;
        this.userName = userName;
        this.headerIcon = headerIcon;
      });
    } else {
      setState(() {
        this.hasLogin = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return // flutter 中 container 如何撑满宽度
      // 1、child使用Row, 不光是container,其他的 card 等也可以内部使用 Row 来撑满
      // 2、给coainer设置alignment属性
      Container(
        alignment: Alignment.center,
        height: 200,
        color: Colors.red,
        child: hasLogin ? getLoginWidget() : getUnLoginWidget(context),
      );
  }

  Widget getLoginWidget() {
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
              alignment: Alignment.center,
              child: Container(
                margin: EdgeInsets.only(top: 60),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    ClipOval(
                      child: StringUtil.checkNotEmpty(headerIcon)
                          ? HeaderIconWidget(headerIcon, HeaderIconSize.SIZE_BIG_80)
                          : Image.asset(
                              "images/linkknown.jpg",
                              width: 80.0,
                              height: 80.0,
                              fit: BoxFit.cover,
                            ),
                    ),
                    VEmptyView(10),
                    Text(
                      nickName + " / " + userName,
                      style: TextStyle(color: Colors.white),
                    ),
                  ],
                ),
              )),
          Align(
            child: Container(
              margin: EdgeInsets.only(top: 160),
              child: MingYanWidget(),
            ),
          ),
        ],
      ),
    );
  }

  Widget getUnLoginWidget(BuildContext context) {
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
              onTap: () {
                // 关闭抽屉
                if (Scaffold.of(context).isDrawerOpen) {
                  Navigator.pop(context); // close the drawer
                }
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

  @override
  void dispose() {
    subscription?.cancel();
    super.dispose();
  }
}

class HomeContentHeaderWidget extends StatefulWidget {
  @override
  _HomeContentHeaderWidgetState createState() => _HomeContentHeaderWidgetState();
}

class _HomeContentHeaderWidgetState extends State<HomeContentHeaderWidget> with TickerProviderStateMixin {

  String loginUserName;

  @override
  void initState() {
    super.initState();
  }

  queryLoginUserName() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }

  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserName();
      return Text("");
    }
    return Column(
      children: <Widget>[
        ListTile(
          title: Text('我的课程'),
          leading: Image.asset(
            "images/ic_video.png",
            width: 24,
            height: 24,
          ),
          onTap: () {
            if(StringUtil.checkEmpty(this.loginUserName)){
              UIUtil.showToast("未登录..");
            }else{
              NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${loginUserName}");
            }
          },
        ),
        ListTile(
          title: Text('订单列表'),
          leading: Image.asset(
            "images/ic_order.png",
            width: 24,
            height: 24,
          ),
          onTap: () {
            if(StringUtil.checkEmpty(this.loginUserName)){
              UIUtil.showToast("未登录..");
            }else{
              NavigatorUtil.goRouterPage(context, Routes.payOrder);
            }
          },
        ),
        ListTile(
          title: Text('购物车'),
          leading: Image.asset(
            "images/ic_shopping_cart.png",
            width: 25,
            height: 25,
          ),
          onTap: () {
            if(StringUtil.checkEmpty(this.loginUserName)){
              UIUtil.showToast("未登录..");
            }else{
              NavigatorUtil.goRouterPage(context, Routes.shoppingCart);
            }
          },
        ),
        ListTile(
          title: Text('登陆'),
          leading: Image.asset(
            "images/ic_login.png",
            width: 25,
            height: 25,
          ),
          onTap: () {
            // 关闭抽屉
            if (Scaffold.of(context).isDrawerOpen) {
              Navigator.pop(context); // close the drawer
            }
            NavigatorUtil.goLoginPage(context);
          },
        ),
        ListTile(
          title: Text('注册'),
          leading: Image.asset(
            "images/ic_regist.png",
            width: 25,
            height: 25,
          ),
          onTap: () {
            // 关闭抽屉
            if (Scaffold.of(context).isDrawerOpen) {
              Navigator.pop(context); // close the drawer
            }
            NavigatorUtil.goRegistPage(context);
          },
        ),
        ListTile(
          title: Text('退出'),
          leading: Image.asset(
            "images/ic_logout.png",
            width: 25,
            height: 25,
          ),
          onTap: () {
            // 退出并重新跳往登录页面
            LoginUtil.logout();

            eventBus.fire(LoginStateChangeEvent());

            NavigatorUtil.goLoginPage(context);
          },
        ),
        ListTile(
          title: Text('分享'),
          leading: Image.asset(
            "images/ic_share.png",
            width: 25,
            height: 25,
          ),
          onTap: () {
            Share.share('链知课堂：链知课堂APP', subject: "链知课堂");
          },
        ),
      ],
    );
  }
}

