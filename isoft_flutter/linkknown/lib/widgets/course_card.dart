

import 'package:flutter/material.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_label.dart';

class CourseCardWidget extends StatefulWidget {

  Course course;

  CourseCardWidget(this.course);

  @override
  _CourseCardState createState() => _CourseCardState(course);

}

class _CourseCardState extends State<CourseCardWidget> with TickerProviderStateMixin {

  Course course;

  _CourseCardState(this.course);

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 3.0,
      //设置shape，这里设置成了R角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(4.0)),),
      // 对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
      clipBehavior: Clip.antiAlias,
      semanticContainer: false,
      child: Column(
        children: <Widget>[
          // Stack类似FrameLayout,子 widget可以通过父容器的四个角固定位置,子widget可以重叠
          Stack(
            children: <Widget>[
              InkWell(
                onTap: () {
                  NavigatorUtil.goRouterPage(context, "${Routes.courseDetail}?course_id=${course.id}");
                },
                child: Image.network(UIUtils.replaceMediaUrl(course.smallImage)),
              ),
              Align(
                alignment: Alignment.topRight,
                child: Padding(
                  padding: EdgeInsets.all(5.0),
                  child: CommonLabel(course.isCharge == "charge" ? "付费课程" : "免费"),
                ),
              ),
            ],
          ),
          Container(
            padding: EdgeInsets.all(8.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text(course.courseName),
                Container(
                  margin: EdgeInsets.only(top: 5),
                  child: Row(
                    children: <Widget>[
                      // 课程集数和播放次数
                      Image.asset("images/linkknown.jpg", width: 15, height: 15,),
                      Text(course.courseNumber.toString()),
                      Image.asset("images/linkknown.jpg", width: 15, height: 15,),
                      Text(course.watchNumber.toString()),
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(top: 5),
                  child: Row(
                    children: <Widget>[
                      // offstage 组件控制组件是否隐藏
                      // 通过offsatge字段控制child是否显示,比较常用的控件
                      Offstage(
                        offstage: !UIUtils.isValidPrice(course.price),
                        child: Padding(
                          padding: EdgeInsets.only(right: 5),
                          child: Text(
                            Constants.RMB + course.price,
                            style: TextStyle(color: Colors.red),
                          ),
                        ),
                      ),
                      Offstage(
                        offstage: !UIUtils.isValidPrice(course.oldPrice),
                        child: Text(
                          Constants.RMB + course.oldPrice,
                          style: TextStyle(color: Colors.grey, decoration: TextDecoration.lineThrough),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

}