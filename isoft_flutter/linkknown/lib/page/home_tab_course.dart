

import 'package:flutter/cupertino.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/utils/utils.dart';

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
    LinkKnownApi.searchCourseList("", "", 1, 10).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      UIUtils.showToast(value.toString());
    });

  }

  @override
  Widget build(BuildContext context) {
    return Text("你好哇~~~");
  }



}