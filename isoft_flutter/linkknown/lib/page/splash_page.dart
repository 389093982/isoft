import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/common_label.dart';

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

  // Flutter中，使用AnimationController来控制动画暂停、调整进度、停止和倒退。AnimationController继承自Animation<double>。
  // 在vsync信号发出时，需要一个触发器来通知它，从而在每帧中产生一个0到1的线性差值。
  // 一个Controller可以与多个动画进行关联。
  // AnimationController 动画控制器类
  AnimationController _logoController;
  Tween _scaleTween;

  // 动画曲线用的是 CurvedAnimation 类，主要给 Controller 添加速度效果
  CurvedAnimation _logoAnimation;

  @override
  void initState() {
    super.initState();
    _scaleTween = Tween(begin: 0, end: 1);

    // 在AnimationController中提供了一个drive方法，这个是用来链接一个Tween到Animation并返回一个Animation的实例
    // Tween就是一个线性的插值器，可以实现一个完整的变化过程
    _logoController = AnimationController(vsync: this, duration: Duration(milliseconds: 1000))..drive(_scaleTween);
    // 延迟 500 ms 执行
    Future.delayed(Duration(milliseconds: 500), () {
      // 动画执行
      _logoController.forward();
    });

    // linear	匀速的
    // decelerate	匀减速
    // ease	开始加速，后面减速
    // easeIn	开始慢，后面快
    // easeOut	开始快，后面慢
    // easeInOut	开始慢，然后加速，最后再减速
    // ...
    // parent参数传入一个Animation对象，比如AnimationController, 主要给 Controller 添加速度效果
    _logoAnimation = CurvedAnimation(parent: _logoController, curve: Curves.easeOutQuart);

    _logoController.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        Future.delayed(Duration(milliseconds: 500), () {
          goPage();
        });
      }
    });
  }

  void goPage() async {
      NavigatorUtil.goMainPage(context);
  }

  @override
  Widget build(BuildContext context) {
    // 初始化 api 工厂
    LinkKnownApi.init();
    // 假如设计稿是按 iPhone6 的尺寸设计的 (iPhone6 750*1334)
    ScreenUtil.init(context, width: 750, height: 1334);

    return Scaffold(
      backgroundColor: Colors.white,
      body: Stack(
        alignment: Alignment.center,
        children: <Widget>[
          // 背景
          Container(
            color: Colors.red,
            // infinity 无穷大
            height: double.infinity,
            width: double.infinity,
          ),
          Positioned(
            top: 250,
            child: ScaleTransition(
              scale: _logoAnimation,
              child: Hero(
                tag: '_logo',
                child: ClipOval(
                  child: Image.asset("images/linkknown.jpg", width: 100, height: 100,),
                ),
              ),
            ),
          ),
          Positioned(
            top: 380,
            child: Column(
              children: <Widget>[
                Text("链知课堂", style: TextStyle(color: Colors.white, fontSize: 30),),
                Text("链接知识的桥梁", style: TextStyle(color: Colors.white, fontStyle: FontStyle.italic),),
              ],
            ),
          ),
          Positioned(
            top: 50,
            right: 30,
            child: CommonLabel.getCommonLabel("商业合作"),
          ),
          Positioned(
            bottom: 10,
            child: Column(
              children: <Widget>[
                Text(
                  "《用户协议》《隐私政策》和《商业合作》",
                  style: TextStyle(color: Colors.white),
                ),
                Text(
                  "Copyright © 2019-2020 链知网络科技版权所有",
                  style: TextStyle(color: Colors.white),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    _logoController.dispose();
  }
}
