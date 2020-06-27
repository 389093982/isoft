import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:provider/provider.dart';

// State 的生命周期和 StatefulWidget 不同，当 StatefulWidget 状态改变后就会被重建，但是 State 不会改变，
// 但是当 StatefulWidget 在 View 树中移除再插入又会生成新的 State
class SplashPage extends StatefulWidget {
  @override
  _SplashPageState createState() => _SplashPageState();
}

// State 是管理 widget 状态的
// _ 开头表示私有类,不能被外部访问
// State对象必须 with SingleTickerProviderStateMixin 或 TickerProviderStateMixin；TickerProviderStateMixin 适用于多 AnimationController 的情况
class _SplashPageState extends State<SplashPage> with TickerProviderStateMixin {

  // AnimationController 动画控制器类
  AnimationController _logoController;
  Tween _scaleTween;
  CurvedAnimation _logoAnimation;

  @override
  void initState() {
    super.initState();
    _scaleTween = Tween(begin: 0, end: 1);
    _logoController =
        AnimationController(vsync: this, duration: Duration(milliseconds: 500))
          ..drive(_scaleTween);
    Future.delayed(Duration(milliseconds: 500), () {
      _logoController.forward();
    });
    _logoAnimation =
        CurvedAnimation(parent: _logoController, curve: Curves.easeOutQuart);

    _logoController.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        Future.delayed(Duration(milliseconds: 500), () {
          goPage();
        });
      }
    });
  }

  void goPage() async {
    UserModel userModel = Provider.of<UserModel>(context);
//    userModel.initUser();
//    if (userModel.user != null) {
//      await NetUtils.refreshLogin(context).then((value) {
//        if (value.data != -1) {
//          NavigatorUtil.goHomePage(context);
//        }
//      });
//    } else
//      NavigatorUtil.goLoginPage(context);
      NavigatorUtil.goMainPage(context);
  }

  @override
  Widget build(BuildContext context) {
    // 初始化 api 工厂
    LinkKnownApi.init();
    // 假如设计稿是按 iPhone6 的尺寸设计的 (iPhone6 750*1334)
    ScreenUtil.init(width: 750, height: 1334);

    return Scaffold(
      backgroundColor: Colors.white,
      body: Container(
        // infinity 无穷大
        height: double.infinity,
        width: double.infinity,
        child: ScaleTransition(
          scale: _logoAnimation,
          child: Hero(
            tag: 'logo',
            child: Image.asset("images/linkknown.jpg"),
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    _logoController.dispose();
  }
}
