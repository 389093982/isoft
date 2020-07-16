import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
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
  ].map((item) => TabViewModel(
    title: item.title,
    widget: item.widget,
  )).toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController = new TabController(length: viewModels.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HeaderWidget(),
          ),
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
    );
  }

}



class _HeaderWidget extends StatefulWidget {
  @override
  _HeaderWidgetState createState() => _HeaderWidgetState();
}

class _HeaderWidgetState extends State<_HeaderWidget> with TickerProviderStateMixin {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Container(
          child: Transform(
            transform: Matrix4.translationValues(0, 3, 0),
            child: Text("优惠券"),
          ),
        ),
      ],
    );
  }
}