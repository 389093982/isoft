import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/page/pay_order.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/home_drawer.dart';

import 'course_comment.dart';
import 'course_detail.dart';
import 'course_introduce.dart';
import 'invitation.dart';
import 'my_coupon.dart';
import 'my_customer.dart';

class PersonalCenterPage extends StatefulWidget {
  @override
  _PersonalCenterPage createState() => _PersonalCenterPage();
}

class _PersonalCenterPage extends State<PersonalCenterPage> with TickerProviderStateMixin {
  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController = new TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Listener(
      onPointerMove: (result) {
        print(result.position);
      },
      child: CustomScrollView(
        physics: ClampingScrollPhysics(),
        slivers: <Widget>[
          SliverAppBar(
            backgroundColor: Colors.white,
            floating: true,
            leading: IconButton(
              icon: Icon(Icons.arrow_back),
              onPressed: () {
                NavigatorUtil.goBack(context);
              },
            ),
            expandedHeight: 330,
            flexibleSpace: FlexibleSpaceBar(
              //title: Text("个人中心", style: TextStyle(color: Colors.black),),
              background: SliverTopBar(),
            ),
          ),
          SliverList(
            delegate: SliverChildBuilderDelegate((Context, index) {
              return Container(
                color: Colors.white,
                child: tabBar(),
              );
            }, childCount: 1),
          ),
          SliverFillRemaining(
            child: TabBarView(
              controller: this.tabController,
              children: <Widget>[
                MyCustomerWidget(),
                MyCustomerWidget(),
                MyCustomerWidget(),
              ],
            ),
          ),
        ],
      ),
    );
  }


  tabBar() {
    var titles = ['发布的课程', '收藏的课程', '观看的课程'];
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

class SliverTopBar extends StatelessWidget {
  const SliverTopBar({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Column(
          children: <Widget>[
            Image.asset(
              "images/personal_center_img02.jpg",
              fit: BoxFit.fill,
              width: 750,
              height: 200,
            ),
            Container(
              height: 100,
              color: Colors.white,
              child: Column(children: <Widget>[
                Text("OK"),
                Text("OK"),
                Text("OK"),
                Text("OK"),
              ],),
            ),
          ],
        ),
        Positioned(
          top: 160,
          left: 30,
          child: ClipOval(
            child: Image.asset(
              "images/linkknown.jpg",
              width: 80,
              height: 80,
            ),
          ),
        )
      ],
    );
  }
}