

import 'package:flutter/cupertino.dart';

class TabCourseFilterWidget extends StatefulWidget {

  String search;
  String isCharge;

  TabCourseFilterWidget(this.search, this.isCharge);

  @override
  _TabCourseFilterState createState() => _TabCourseFilterState();

}

class _TabCourseFilterState extends State<TabCourseFilterWidget> with TickerProviderStateMixin {

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();
  }

  void initData() {

  }

  @override
  Widget build(BuildContext context) {
    return Text("你好哇~~~");
  }



}