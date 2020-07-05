

import 'package:flutter/material.dart';

class CourseDetailPage extends StatefulWidget {

  int course_id;
  CourseDetailPage(this.course_id);

  @override
  _CourseDetailPageState createState() => _CourseDetailPageState(course_id);
}

class _CourseDetailPageState  extends State<CourseDetailPage> with TickerProviderStateMixin {

  int course_id;
  _CourseDetailPageState(this.course_id);

  TabController tabController;

  @override
  void initState() {
    super.initState();
    // 用来控制controller对应widget的各种各样交互行为以及状态变化的控制（类似于widget本身只是一个静态的物品，而通过对controller的操作控制让这个widget活了起来）
    this.tabController = TabController(length: 2, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: CustomScrollView(
        slivers: <Widget>[
          // AppBar 和 SliverAppBar 是Material Design中的 App Bar，也就是 Android 中的 Toolbar
          // SliverAppBar是一个与 CustomScrollView 结合使用的material design风格的标题栏 .
          // 不同于AppBar, 它可以展开或收缩.
          SliverAppBar(
            leading: Container( // 绘制返回键
                margin: EdgeInsets.all(10), // 设置边距
                child: IconButton(
                  icon: Icon(
                    Icons.arrow_back_ios,
                    size: 20,
                  ),
                  onPressed: () {
                    // 返回首页
//                    NavigatorUtil.goMainPage(context);
//                Navigator.pop(context); // 关闭当前页面--
                  },
                )
            ),
            // appBar是否置顶
            pinned: true,
            elevation: 0,
            expandedHeight: 250,
            // 一个显示在 AppBar 下方的控件，高度和 AppBar 高度一样，可以实现一些特殊的效果，该属性通常在 SliverAppBar 中使用
            flexibleSpace: FlexibleSpaceBar(
              title: Text("课程详情页"),
              background: Image.network(
                'http://img1.mukewang.com/5c18cf540001ac8206000338.jpg',
                fit: BoxFit.cover,
              ),
            ),
          ),
          // SliverPersistentHeader最重要的一个属性是SliverPersistentHeaderDelegate，为此我们需要实现一个类继承自SliverPersistentHeaderDelegate
          SliverPersistentHeader(
            pinned: true,
            delegate: StickyTabBarDelegate(
              child: TabBar(
                labelColor: Colors.black,
                controller: this.tabController,
                tabs: <Widget>[
                  Tab(text: '简介'),
                  Tab(text: '评论'),
                ],
              ),
            ),
          ),
          // SliverFillRemaining 创建填充视口中剩余空间的小条
          SliverFillRemaining(
            child: TabBarView(
              controller: this.tabController,
              children: <Widget>[
                Center(child: Text('Content of Home1111111111111111')),
                Center(child: Text('Content of Profil1111111111111111111e')),
              ],
            ),
          ),
        ],
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
  Widget build(BuildContext context, double shrinkOffset, bool overlapsContent) {
    return this.child;
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