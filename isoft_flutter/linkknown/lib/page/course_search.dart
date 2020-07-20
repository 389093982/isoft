
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_search.dart';

class CourseSearchPage extends StatefulWidget {

  String search;
  String isCharge;

  CourseSearchPage(this.search, this.isCharge);

  @override
  _CourseSearchPageState createState() => _CourseSearchPageState();

}

class _CourseSearchPageState extends State<CourseSearchPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Flutter去掉AppBar避免body溢出到状态栏
      // 没有AppBar的Flutter，如果不在Scaffold中使用AppBar会发现默认是沉浸式，预留出状态栏的高度方法
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(MediaQuery.of(context).size.height * 0),
        child: Container(
          width: double.infinity,
          height: double.infinity,
          decoration: BoxDecoration(
            color: Colors.red,
          ),
        ),
      ),
      body: SafeArea(
        child: Column(
          children: <Widget>[
            Container(
              height: ScreenUtil().setHeight(130),
              color: Colors.red,
              child: SearchInputWidget(
                handleSearch: (data) {
                  this.setState(() {
                    widget.search = data;
                  });
                },
              ),
            ),
            SizedBox(height: 10,),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: Text("热门搜索", style: TextStyle(color: Colors.grey),),
                ),
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: Text("搜索历史", style: TextStyle(color: Colors.grey),),
                ),
              ],
            ),
            SizedBox(height: 10,),
            Expanded(
              child: CourseFilterWidget(widget.search, widget.isCharge),
            ),
          ],
        ),
      ),
    );
  }
}