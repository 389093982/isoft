import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

class CourseCardWidget extends StatefulWidget {
  Course course;

  CourseCardWidget(this.course);

  @override
  _CourseCardState createState() => _CourseCardState();
}

class _CourseCardState extends State<CourseCardWidget>
    with TickerProviderStateMixin {

  _CourseCardState();

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 3.0,
      //设置shape，这里设置成了R角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(4.0)),
      ),
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
                  NavigatorUtil.goRouterPage(
                      context, "${Routes.courseDetail}?course_id=${widget.course.id}");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child: AspectRatio(
//                  child: Image.network(UIUtils.replaceMediaUrl(course.smallImage), fit: BoxFit.fill, width: double.infinity,),
                  child: CachedImageWidget(UIUtils.replaceMediaUrl(widget.course.smallImage)),
                  aspectRatio: 1 / 0.6,
                ),
              ),
              Align(
                alignment: Alignment.topRight,
                child: Padding(
                  padding: EdgeInsets.all(5.0),
                  child:
                      CommonLabel(widget.course.isCharge == "charge" ? "付费课程" : "免费"),
                ),
              ),
            ],
          ),
          Container(
            padding: EdgeInsets.all(5.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text(widget.course.courseName),
                Container(
                  margin: EdgeInsets.only(top: 2),
                  child: Row(
                    children: <Widget>[
                      // 课程集数和播放次数
                      Image.asset(
                        "images/ic_play_circle_outline_black_24dp.png",
                        width: 15,
                        height: 15,
                      ),
                      Text(widget.course.courseNumber.toString()),
                      Image.asset(
                        "images/ic_subtitles_black_24dp.png",
                        width: 15,
                        height: 15,
                      ),
                      Text(widget.course.watchNumber.toString()),
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(top: 2),
                  child: Row(
                    children: <Widget>[
                      // offstage 组件控制组件是否隐藏
                      // 通过offsatge字段控制child是否显示,比较常用的控件
                      Offstage(
                        offstage: !UIUtils.isValidPrice(widget.course.price),
                        child: Padding(
                          padding: EdgeInsets.only(right: 5),
                          child: Text(
                            Constants.RMB + widget.course.price,
                            style: TextStyle(color: Colors.red),
                          ),
                        ),
                      ),
                      Offstage(
                        offstage: !UIUtils.isValidPrice(widget.course.oldPrice),
                        child: Text(
                          Constants.RMB + widget.course.oldPrice,
                          style: TextStyle(
                              color: Colors.grey,
                              decoration: TextDecoration.lineThrough),
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
