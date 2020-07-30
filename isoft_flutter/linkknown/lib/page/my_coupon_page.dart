import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/home_drawer.dart';

import 'my_coupon.dart';

class TabViewModel {
  final String title;
  final Widget widget;

  const TabViewModel({
    this.title,
    this.widget,
  });
}

class MyCouponPage extends StatefulWidget {
  @override
  _MyCouponPage createState() => _MyCouponPage();
}

class _MyCouponPage extends State<MyCouponPage> with TickerProviderStateMixin {
  List<TabViewModel> viewModels = [
    TabViewModel(title: '已领取', widget: MyCouponWidget("false", "false")),
    TabViewModel(title: '已使用', widget: MyCouponWidget("", "true")),
    TabViewModel(title: '已过期', widget: MyCouponWidget("true", "false")),
  ]
      .map((item) => TabViewModel(
            title: item.title,
            widget: item.widget,
          ))
      .toList();

  TabController tabController;

  // 抖动动画
  AnimationController rotationAnimationController;
  Animation rotationAnimation;

  @override
  void initState() {
    super.initState();
    this.tabController =
        new TabController(length: viewModels.length, vsync: this);

    rotationAnimationController =
        AnimationController(duration: const Duration(milliseconds: 300), vsync: this);
    rotationAnimation =
        Tween(begin: -0.05, end: 0.05).animate(rotationAnimationController);
    rotationAnimationController.repeat(reverse: true);
  }

  @override
  void dispose() {
    rotationAnimationController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("我的优惠券"),
          bottom: TabBar(
            controller: this.tabController,
            isScrollable: true,
            indicatorColor: Colors.white,
            indicatorSize: TabBarIndicatorSize.label,
            tabs: this.viewModels.map((item) => Tab(text: item.title)).toList(),
          ),
        ),
        preferredSize: Size.fromHeight(80.0),
      ),
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
      floatingActionButtonLocation: CustomFloatingActionButtonLocation(FloatingActionButtonLocation.endFloat, 0, - 20),
      floatingActionButton: FloatingActionButton.extended(
        icon: RotationTransition(
          turns: rotationAnimation,
          child: SvgPicture.asset("images/ic_coupon.svg", width: 20, height: 20, color: Colors.white,),
        ),
        backgroundColor: Colors.orange,
        label: Text("领券中心"),
        onPressed: () {
          NavigatorUtil.goRouterPage(context, Routes.receiveCouponCenter);
        },
      ),
    );
  }

}

class CustomFloatingActionButtonLocation extends FloatingActionButtonLocation {
  FloatingActionButtonLocation location;
  double offsetX;    // X方向的偏移量
  double offsetY;    // Y方向的偏移量
  CustomFloatingActionButtonLocation(this.location, this.offsetX, this.offsetY);

  @override
  Offset getOffset(ScaffoldPrelayoutGeometry scaffoldGeometry) {
    Offset offset = location.getOffset(scaffoldGeometry);
    return Offset(offset.dx + offsetX, offset.dy + offsetY);
  }
}

