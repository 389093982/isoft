import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/response/course_detail_response.dart';
import 'package:linkknown/page/course_introduce_widget.dart';
import 'package:linkknown/utils/comment_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:share/share.dart';

class CourseDetailPage extends StatefulWidget {

  int course_id;

  CourseDetailPage(this.course_id);

  @override
  _CourseDetailPageState createState() => _CourseDetailPageState();
}

class _CourseDetailPageState extends State<CourseDetailPage> {
  ScrollController scrollController = ScrollController();
  double height = 0;
  bool showTitle = true;
  bool showTitleOld = false;

  Course course;
  List<CVideo> cVideos;

  // StreamBuilder 实现局部刷新
  // 使用StreamBuilder来局部刷新，通过sink.add方法向streamController.sink中添加一个事件流，这个流会被StreamBuilder中stream接收，然后触发builder方法。
  // 最后在页面销毁的时候释放资源
  final StreamController _streamController = StreamController<bool>();

  @override
  void initState() {
    super.initState();

    scrollController.addListener(offsetListener);

    initData();
  }

  offsetListener() {
    height = scrollController.offset;
    showTitle = !(scrollController.position.maxScrollExtent - height < 70);
    if (showTitle != showTitleOld) {
      showTitleOld = showTitle;
     _streamController.sink.add(showTitle);
    }
  }

  void initData() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    CourseDetailResponse courseDetailResponse = await LinkKnownApi.showCourseDetailForApp(widget.course_id, loginUserName);
    setState(() {
      course = courseDetailResponse.course;
      cVideos = courseDetailResponse.cVideos;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: <Widget>[
          Expanded(
              child: NestedScrollView(
                controller: scrollController,
                headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
                  return [
                    // AppBar 和 SliverAppBar 是Material Design中的 App Bar，也就是 Android 中的 Toolbar
                    // SliverAppBar是一个与 CustomScrollView 结合使用的material design风格的标题栏 .
                    // 不同于AppBar, 它可以展开或收缩.
                    SliverAppBar(
                      leading: Container(
                        // 绘制返回键
                          margin: EdgeInsets.all(10), // 设置边距
                          child: IconButton(
                            icon: Icon(
                              Icons.arrow_back,
                              size: 20,
                            ),
                            onPressed: () {
                              // 返回上一页
                              NavigatorUtil.goBack(context);
                            },
                          )),
                      // appBar是否置顶
                      pinned: true,
                      elevation: 0,
                      expandedHeight: 200,
                      // 一个显示在 AppBar 下方的控件，高度和 AppBar 高度一样，可以实现一些特殊的效果，该属性通常在 SliverAppBar 中使用
                      flexibleSpace: FlexibleSpaceBar(
                        centerTitle: true,
                        title: StreamBuilder<bool>(
                          stream: _streamController.stream,
                          initialData: true,    // showTitle
                          builder: (BuildContext context, AsyncSnapshot<bool> snapshot){
                            return snapshot.data
                                ? Text(course != null ? course.courseName : "")
                                : Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: <Widget>[
                                Image.asset(
                                  "images/ic_fab_play.png",
                                  width: 30,
                                  height: 30,
                                  fit: BoxFit.fill,
                                ),
                                SizedBox(
                                  width: 10,
                                ),
                                Text("立即播放")
                              ],
                            );
                          },
                        ),
                        background: Image.network(
                          UIUtils.replaceMediaUrl(
                              course != null ? course.smallImage : ""),
                          fit: BoxFit.fill,
                        ),
                      ),
                    ),
                    // SliverPersistentHeader最重要的一个属性是SliverPersistentHeaderDelegate，为此我们需要实现一个类继承自SliverPersistentHeaderDelegate
                  ];
                },
                body: ScrollConfiguration(
                  behavior: NoShadowScrollBehavior(),
                  child: CourseIntroduceWidget(course, cVideos),
                ),
              ),
          ),
          Container(
            alignment: Alignment.center,
            padding: EdgeInsets.only(left: 20),
            height: 40,
            color: Colors.grey[200],
            child: Row(children: <Widget>[
              SizedBox(width: 50,),
              InkWell(
                onTap: (){
                  //评论弹框
                  CommentUtil.showFirstLevelCommentDialog(context, widget.course_id.toString(),"course_theme_type","comment",course.courseAuthor,course.comments.toString());
                },
                child: Container(
                  width: 90,
                  child: Row(
                    children: <Widget>[
                      Icon(Icons.textsms,color: Colors.black45,size: 20,),
                      SizedBox(width: 2,),
                      Text(this.course!=null?course.comments.toString():"0",style: TextStyle(color: Colors.black45),),
                    ],
                  ),
                ),
              ),
              InkWell(
                onTap: (){},
                child: Container(
                  width: 90,
                  child: Row(children: <Widget>[
                    Icon(Icons.remove_red_eye,color: Colors.black45,size: 20,),
                    SizedBox(width: 2,),
                    Text(this.course!=null?course.watchNumber.toString():"0",style: TextStyle(color: Colors.black45),),
                  ],),
                ),
              ),
              InkWell(
                onTap: (){
                  Share.share('链知课程：${this.course.courseName}', subject: "链知课堂");
                },
                child: Container(
                  width: 90,
                  child: Row(children: <Widget>[
                    Icon(Icons.share,color: Colors.black45,size: 20,),
                    SizedBox(width: 2,),
                    Text("分享",style: TextStyle(color: Colors.black45),),
                  ],),
                ),
              ),

            ],),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    scrollController?.removeListener(offsetListener);
    scrollController?.dispose();
    _streamController?.close();
    super.dispose();
  }
}
