import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/page/course_introduce.dart';
import 'package:linkknown/utils/comment_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class CourseDetailPage extends StatefulWidget {
  int course_id;

  CourseDetailPage(this.course_id);

  @override
  _CourseDetailPageState createState() => _CourseDetailPageState(course_id);
}

class _CourseDetailPageState extends State<CourseDetailPage> {
  ScrollController scrollController = ScrollController();
  double height = 0;
  bool showTitle = true;
  bool showTitleOld = false;

  int course_id;

  _CourseDetailPageState(this.course_id);

  Course course;
  List<CVideo> cVideos;


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
     setState(() {
       showTitleOld = showTitle;
     });
    }
  }

  void initData() async {
    CourseDetailResponse courseDetailResponse =
        await LinkKnownApi.showCourseDetailForApp(course_id, null);
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
                        title: showTitle
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
              SizedBox(width: 70,),
              InkWell(
                onTap: (){
                  //评论弹框
                  CommentUtil.showFirstLevelCommentDialog(context,course.id.toString(),"course_theme_type","comment",course.courseAuthor,course.comments.toString());
                },
                child: Container(
                  width: 40,
                  child: Row(
                    children: <Widget>[
                      Icon(Icons.textsms,color: Colors.black45,size: 20,),
                      Text(this.course!=null?course.comments.toString():"0",style: TextStyle(color: Colors.black45),),
                    ],
                  ),
                ),
              ),
              SizedBox(width: 30,),
              InkWell(
                onTap: (){},
                child: Icon(Icons.remove_red_eye,color: Colors.black45,size: 20,),
              ),
              Text(this.course!=null?course.watchNumber.toString():"0",style: TextStyle(color: Colors.black45),),
              SizedBox(width: 30,),
              InkWell(
                onTap: (){},
                child: Icon(Icons.favorite_border,color: Colors.black45,size: 20,),
              ),
              Text("222",style: TextStyle(color: Colors.black45),),
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
    super.dispose();
  }
}
