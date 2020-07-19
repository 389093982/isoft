import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/page/course_comment.dart';
import 'package:linkknown/page/course_introduce.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class CourseDetailPage extends StatefulWidget {
  int course_id;

  CourseDetailPage(this.course_id);

  @override
  _CourseDetailPageState createState() => _CourseDetailPageState(course_id);
}

class _CourseDetailPageState extends State<CourseDetailPage>
    with TickerProviderStateMixin {
  int course_id;

  _CourseDetailPageState(this.course_id);

  Course course;
  List<CVideo> cVideos;

  TabController tabController;

  @override
  void initState() {
    super.initState();
    // 用来控制controller对应widget的各种各样交互行为以及状态变化的控制（类似于widget本身只是一个静态的物品，而通过对controller的操作控制让这个widget活了起来）
    this.tabController = TabController(length: 2, vsync: this);

    initData();
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
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        body: NestedScrollView(
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
                  title: Text(course != null ? course.courseName : ""),
                  background: Image.network(
                    UIUtils.replaceMediaUrl(
                        course != null ? course.smallImage : ""),
                    fit: BoxFit.fill,
                  ),
                ),
              ),
              // SliverPersistentHeader最重要的一个属性是SliverPersistentHeaderDelegate，为此我们需要实现一个类继承自SliverPersistentHeaderDelegate
              SliverPersistentHeader(
                pinned: true,
                delegate: StickyTabBarDelegate(
                  child: TabBar(
                    unselectedLabelColor: Colors.black,
                    labelColor: Colors.red[400],
                    indicatorColor: Colors.red[400],
                    indicatorSize: TabBarIndicatorSize.label,
                    controller: this.tabController,
                    tabs: <Widget>[
                      Tab(text: '简介'),
                      Tab(text: '评论'),
                    ],
                  ),
                ),
              ),
            ];
          },
          body: ScrollConfiguration(
            behavior: NoShadowScrollBehavior(),
            child: TabBarView(controller: this.tabController, children: [
              CourseIntroduceWidget(course, cVideos),
              CourseCommentWidget(),
            ]),
          ),
        ),
      ),
    );
  }
}

// SliverPersistentHeaderDelegate的实现类必须实现其4个方法
// minExtent：收起状态下组件的高度；
// maxExtent：展开状态下组件的高度；
// shouldRebuild：类似于react中的shouldComponentUpdate；
// build：构建渲染的内容。
class StickyTabBarDelegate extends SliverPersistentHeaderDelegate {
  final TabBar child;

  StickyTabBarDelegate({@required this.child});

  @override
  Widget build(
      BuildContext context, double shrinkOffset, bool overlapsContent) {
    return Material(
      child: this.child,
    );
  }

  @override
  double get maxExtent => this.child.preferredSize.height;

  @override
  double get minExtent => this.child.preferredSize.height;

  @override
  bool shouldRebuild(SliverPersistentHeaderDelegate oldDelegate) {
    return true;
  }
}
