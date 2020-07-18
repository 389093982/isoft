import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/page/pay_order.dart';
import 'package:linkknown/widgets/home_drawer.dart';

import 'invitation.dart';
import 'my_coupon.dart';
import 'my_customer.dart';

class TabViewModel {
  final String title;
  final Widget widget;

  const TabViewModel({
    this.title,
    this.widget,
  });
}

class PersonalCenterPage extends StatefulWidget {
  @override
  _PersonalCenterPage createState() => _PersonalCenterPage();
}

class _PersonalCenterPage extends State<PersonalCenterPage> with TickerProviderStateMixin {

  List<TabViewModel> viewModels = [
    TabViewModel(title: '发布的课程', widget: MyCustomerWidget()),
    TabViewModel(title: '收藏的课程', widget: InvitationWidget()),
    TabViewModel(title: '观看的课程', widget: InvitationWidget()),
  ].map((item) => TabViewModel(
    title: item.title,
    widget: item.widget,
  )).toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController = new TabController(length: 4, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HeaderWidget(),
          ),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: Container(
        height: 40,
        color: Colors.white,
        child: _tabBar(),
      ),
    );
  }


  _tabBar() {
    var titles = ['推荐', '商品', '情报', '美图'];
    var bars = new List<Widget>();
    for (int i = 0; i < titles.length; i++) {
      bars.add(
        new Tab(
          icon: Text(titles[i],),
        ),
      );
    }
    return Material(
        color: Colors.white,
        child: TabBar(
          indicatorColor: Colors.pinkAccent,
          unselectedLabelColor: Colors.grey[600],
          labelColor: Colors.pinkAccent,
          indicatorSize: TabBarIndicatorSize.label,
          tabs: bars,
          controller: tabController,
        )
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
            transform: Matrix4.translationValues(0, 0, 0),
            child: Text("个人中心"),
          ),
        ),
      ],
    );
  }
}