import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/model/favorite_count_response.dart';
import 'package:linkknown/model/favorite_is_response.dart';
import 'package:linkknown/page/course_video_widget.dart';
import 'package:linkknown/route/reoutes_handler.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

// 课程简介组件
class CourseIntroduceWidget extends StatefulWidget {
  Course course;
  List<CVideo> cVideos;

  CourseIntroduceWidget(this.course, this.cVideos);

  @override
  _CourseIntroduceState createState() => _CourseIntroduceState();
}

// State 构造函数传参的话，只会执行一次，所以不使用 State 传参
// 改用 widget.xxx 参数，widget 的构造器会重复执行
class _CourseIntroduceState extends State<CourseIntroduceWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            widget.course != null ? widget.course.courseName : '',
            style: LinkKnownTextStyle.commonTitle,
          ),
          VEmptyView(5),
          Row(
            children: <Widget>[
              // 课程集数和播放次数
              Image.asset(
                "images/ic_views.png",
                width: 15,
                height: 15,
              ),
              // 设置间距
              Container(
                width: 5,
              ),
              Text(widget.course != null
                  ? widget.course.courseNumber.toString()
                  : "0"),
              Container(
                width: 15,
              ),
              Image.asset(
                "images/ic_list_counts.png",
                width: 15,
                height: 15,
              ),
              Container(
                width: 5,
              ),
              Text(widget.course != null
                  ? widget.course.watchNumber.toString()
                  : "0"),
            ],
          ),
          VEmptyView(5),
          Text(
            widget.course != null ? widget.course.courseShortDesc : '',
            // 设置行间距 1.3
            strutStyle:
                StrutStyle(forceStrutHeight: true, height: 1.3, leading: 0.9),
          ),
          // 分享点赞收藏播放
          // 作者信息
          VEmptyView(5),
          // 课程操作组件
          Offstage(
            offstage: widget.course == null,
            child: widget.course != null ? CourseOperateWidget(widget.course) : null,
          ),
          // 课程标签语
          CourseLabelWidget(
              widget.course != null ? widget.course.courseLabel : ''),
          Expanded(
            // 分集视频
            child: CourseVideosWidget(widget.course, widget.cVideos,
                clickCallBack: _clickCallBack),
          ),
        ],
      ),
    );
  }

  _clickCallBack(index) {
    routerParamMap["videoplay_courseKey"] = widget.course;
    routerParamMap["videoplay_cVideosKey"] = widget.cVideos;
    NavigatorUtil.goRouterPage(context, "${Routes.videoPlay}?index=${index}");
  }
}

class CourseLabelWidget extends StatefulWidget {
  String label;

  CourseLabelWidget(this.label);

  @override
  _CourseLabelState createState() => _CourseLabelState();
}

class _CourseLabelState extends State<CourseLabelWidget> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    List<String> labels = StringUtil.splitLabel(widget.label);

    // Wrap是一个可以使子控件自动换行的控件，默认的方向是水平的
    return Padding(
      padding: EdgeInsets.symmetric(vertical: ScreenUtil().setHeight(20)),
      child: Wrap(
        spacing: 5, //主轴上子控件的间距
        runSpacing: 5, //交叉轴上子控件之间的间距
        children: List.generate(labels.length, (index) {
          return CommonLabel.getCommonLabel2(labels[index]);
        }),
      ),
    );
  }
}

class CourseOperateWidget extends StatefulWidget {
  Course course;

  CourseOperateWidget(this.course);

  int collectNumber = 0;
  int priaseNumber = 0;
  int watchNumber = 0;
  bool isCollect = false;
  bool isPriase = false;

  @override
  _CourseOperateState createState() => _CourseOperateState();
}

class _CourseOperateState extends State<CourseOperateWidget> {
  @override
  void initState() {
    super.initState();

    initCourseOperateData();
  }

  initCourseOperateData() async {
    FavoriteCountResponse collectFavoriteCountResponse =
        await LinkKnownApi.queryFavoriteCount(
            widget.course.id, Constants.FAVORITE_TYPE_COURSE_COLLECT);
    FavoriteCountResponse priaseFavoriteCountResponse =
        await LinkKnownApi.queryFavoriteCount(
            widget.course.id, Constants.FAVORITE_TYPE_COURSE_PRIASE);

    String userName = await LoginUtil.getUserName();

    IsFavoriteResponse collectIsFavoriteResponse = await LinkKnownApi.isFavorite(userName, widget.course.id, Constants.FAVORITE_TYPE_COURSE_COLLECT);
    IsFavoriteResponse priaseIsFavoriteResponse = await LinkKnownApi.isFavorite(userName, widget.course.id, Constants.FAVORITE_TYPE_COURSE_PRIASE);

    setState(() {
      if (collectFavoriteCountResponse.status == "SUCCESS") {
        this.widget.collectNumber = collectFavoriteCountResponse.counts;
      }
      if (priaseFavoriteCountResponse.status == "SUCCESS") {
        this.widget.priaseNumber = priaseFavoriteCountResponse.counts;
      }

      if (collectIsFavoriteResponse.status == "SUCCESS") {
        this.widget.isCollect = collectIsFavoriteResponse.isFavorite;
      }
      if (priaseIsFavoriteResponse.status == "SUCCESS") {
        this.widget.isPriase = priaseIsFavoriteResponse.isFavorite;
      }
    });
  }

  void handleFavoriteClick (String favorite_type) async {
    String userName = await LoginUtil.getUserName();
    BaseResponse baseResponse = await LinkKnownApi.toggleFavorite(widget.course.id, favorite_type);
    if (baseResponse.status == "SUCCESS") {
      // 重新刷新状态
      initCourseOperateData();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Expanded(
          child: CourseOperateItemWidget(
              label: "分享",
              imagepath: "images/ic_share.svg",
              enable: false,
              number: 0,
              clickCallback: () {}),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "点赞",
              imagepath: "images/ic_praise.svg",
              enable: widget.isPriase,
              number: widget.priaseNumber,
              clickCallback: () {
                // 处理点击事件
                handleFavoriteClick(Constants.FAVORITE_TYPE_COURSE_PRIASE);
              }),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "收藏",
              imagepath: "images/ic_collect.svg",
              enable: widget.isCollect,
              number: widget.collectNumber,
              clickCallback: () {
                // 处理点击事件
                handleFavoriteClick(Constants.FAVORITE_TYPE_COURSE_COLLECT);
              }),
        ),
        Expanded(
          child: CourseOperateItemWidget(
              label: "播放",
              imagepath: "images/ic_play.svg",
              enable: false,
              number: widget.course.watchNumber,
              clickCallback: () {}),
        ),
      ],
    );
  }
}

class CourseOperateItemWidget extends StatefulWidget {
  String label;
  String imagepath;
  bool enable;
  int number; // 显示收藏点赞观看数量
  VoidCallback clickCallback;

  CourseOperateItemWidget(
      {this.label,
      this.imagepath,
      this.enable,
      this.number,
      this.clickCallback});

  @override
  _CourseOperateItemState createState() => _CourseOperateItemState();
}

class _CourseOperateItemState extends State<CourseOperateItemWidget> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        children: <Widget>[
          Text(
            "${widget.number}",
            style: TextStyle(color: Colors.green),
          ),
          VEmptyView(3),
          GestureDetector(
            onTap: widget.clickCallback,
            child: Stack(
              alignment: Alignment.center,
              children: <Widget>[
                Image.asset(
                  "images/ic_operate_bg.png",
                  width: 50,
                  height: 50,
                ),
                Align(
                    alignment: Alignment.center,
                    child: SvgPicture.asset(
                      widget.imagepath,
                      width: 30,
                      height: 30,
                      color: widget.enable ? Colors.red[300] : Colors.grey,
                    )),
              ],
            ),
          ),
          VEmptyView(3),
          Text(widget.label),
        ],
      ),
    );
  }
}