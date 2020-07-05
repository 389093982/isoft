

import 'package:flutter/material.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class CourseDetailPage extends StatefulWidget {

  int course_id;
  CourseDetailPage(this.course_id);

  @override
  _CourseDetailPageState createState() => _CourseDetailPageState(course_id);
}

class _CourseDetailPageState  extends State<CourseDetailPage> with TickerProviderStateMixin {

  int course_id;
  _CourseDetailPageState(this.course_id);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('课程详情页面'),
        centerTitle: true,
        backgroundColor: Colors.red,
        elevation: 0,
        brightness: Brightness.light,
        leading: Container( // 绘制返回键
            margin: EdgeInsets.all(10), // 设置边距
            child: IconButton(
              icon: Icon(
                Icons.arrow_back_ios,
                size: 20,
              ),
              onPressed: () {
                // 返回首页
                NavigatorUtil.goMainPage(context);
//                Navigator.pop(context); // 关闭当前页面--
              },
            )
        ),
      ),
      body: SingleChildScrollView(
        child: Text("课程详情页面${course_id}"),
      ),
    );
  }

}