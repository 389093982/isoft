
import 'package:flutter/material.dart';
import 'package:linkknown/utils/navigator_util.dart';

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

class _HomeDrawerHeaderState extends State<HomeDrawerHeaderWidget> with TickerProviderStateMixin {

  bool hasLogin = false;

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
    return Stack(
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
    );
  }

  Widget getUnLoginWidget (BuildContext context) {
    return Stack(
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
    );
  }
}

class HomeContentHeaderWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text("22222222222222222222");
  }

}