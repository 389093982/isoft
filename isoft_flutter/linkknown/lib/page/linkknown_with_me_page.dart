import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter_widget.dart';
import 'package:linkknown/page/tab_recommend_widget.dart';
import 'package:linkknown/widgets/home_drawer.dart';

import 'invitation_widget.dart';
import 'my_coupon_widget.dart';
import 'my_customer_widget.dart';

class TabViewModel {
  final String title;
  final Widget widget;

  const TabViewModel({
    this.title,
    this.widget,
  });
}

class LinkknownWithMePage extends StatefulWidget {
  @override
  _LinkknownWithMePage createState() => _LinkknownWithMePage();
}

  class _LinkknownWithMePage extends State<LinkknownWithMePage> with TickerProviderStateMixin {

  List<TabViewModel> viewModels = [
    TabViewModel(title: '          我的客户          ', widget: MyCustomerWidget()),
    TabViewModel(title: '          邀请          ', widget: InvitationWidget()),
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
            child: Text("我与链知"),
          ),
        ),
      ],
    );
  }
}