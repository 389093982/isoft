
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/utils/string_util.dart';

class CourseVideosWidget extends StatefulWidget {

  Course course;
  List<CVideo> cVideos;
  // 是否是列表模式,默认是列表模式
  bool isListPattern = true;
  // 定义接收父类回调函数的指针
  ValueChanged<int> clickCallBack;

  CourseVideosWidget(this.course, this.cVideos, {this.clickCallBack});

  @override
  _CourseVideosState createState() => _CourseVideosState();
}

class _CourseVideosState extends State<CourseVideosWidget> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Container(
          decoration: BoxDecoration(
            color: Colors.grey[200],
            border: Border(left: BorderSide(color: Colors.red, width: 3, style: BorderStyle.solid)),
          ),
          padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
          child: Row(
            children: <Widget>[
              Text("分集视频"),
              // 中间用Expanded控件
              Expanded(
                child: Text(''),
              ),
              InkWell(
                onTap: (){
                  setState(() {
                    widget.isListPattern = !widget.isListPattern;
                  });
                },
                child: Padding(
                  padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
                  child: SvgPicture.asset(
                    widget.isListPattern ? "images/icon_list.svg" : "images/icon_grid.svg",
                    width: ScreenUtil().setWidth(40),
                    height: ScreenUtil().setHeight(40),
                    color: Colors.red,
                  ),
                ),
              ),
            ],
          ),
        ),
        widget.isListPattern ? getListWidget() : getGridWidget(),
      ],
    );
  }
  
  Widget getGridWidget () {
    return GridView.count(
      shrinkWrap: true,
      //水平子 Widget 之间间距
      crossAxisSpacing: 10.0,
      //垂直子 Widget 之间间距
      mainAxisSpacing: 10.0,
      //GridView内边距
      padding: EdgeInsets.all(10.0),
      //一行的Widget数量
      crossAxisCount: 5,
      //子Widget宽高比例
      childAspectRatio: 1.0,
      //子Widget列表
      children: (widget.cVideos??[]).asMap().keys.map((index) {
        return InkWell(
          onTap: () {
            widget.clickCallBack(index);
          },
          child: Container(
            margin: EdgeInsets.all(5),
            padding: EdgeInsets.all(10),
            color: Colors.grey[200],
            child: Center(
              child: Text("${index + 1}"),
            ),
          ),
        );
      }).toList(),
    );
  }

  Widget getListWidget(){
    return ListView.builder(
        shrinkWrap: true,
        itemCount: (widget.cVideos ?? []).length,
        itemBuilder: (BuildContext context, int index) {
          return InkWell(
            onTap: () {
              widget.clickCallBack(index);
            },
            child: Container(
              decoration: BoxDecoration(
                border: Border(bottom: BorderSide(color: Colors.grey[300])),
              ),
              padding: EdgeInsets.symmetric(vertical: 10, horizontal: 5),
              child: Row(
                children: <Widget>[
                  Text("${index+1}"),
                  Container(
                    width: 20,
                  ),
                  Text(StringUtil.getFileName(widget.cVideos[index].videoName)),
                ],
              ),
            ),
          );
        });
  }
}