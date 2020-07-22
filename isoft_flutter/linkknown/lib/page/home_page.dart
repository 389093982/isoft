import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/home_drawer.dart';
import 'package:provider/provider.dart';

class TabViewModel {
  final String title;
  final Widget widget;

  const TabViewModel({
    this.title,
    this.widget,
  });
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _HomePageState extends State<HomePage>
    with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  List<TabViewModel> viewModels = [
    TabViewModel(title: '免费', widget: CourseFilterWidget("", "free")),
    TabViewModel(title: '付费', widget: CourseFilterWidget("", "charge")),
    TabViewModel(title: '全部', widget: CourseFilterWidget("", "")),
    TabViewModel(title: '推荐', widget: TabRecommendWidget()),
    TabViewModel(title: '会员', widget: Text("宠物卡片")),
  ]
      .map((item) => TabViewModel(
            title: item.title,
            widget: item.widget,
          ))
      .toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController =
        new TabController(length: viewModels.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: Drawer(child: HomeDrawerWidget()),
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HomeHeaderWidget(),
          ),
          bottom: TabBar(
            controller: this.tabController,
            isScrollable: true,
            indicatorColor: Colors.white,
            indicatorSize: TabBarIndicatorSize.label,
            tabs: this.viewModels.map((item) => Tab(text: item.title)).toList(),
          ),
        ),
        preferredSize: Size.fromHeight(80.0),
      ),
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
    );
  }
}

class _HomeHeaderWidget extends StatefulWidget {
  @override
  _HomeHeaderWidgetState createState() => _HomeHeaderWidgetState();
}

class _HomeHeaderWidgetState extends State<_HomeHeaderWidget>
    with TickerProviderStateMixin {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Expanded(
          child: Consumer(
            builder: (BuildContext context, LoginUserInfo loginUserInfo, Widget child) {
              return getLoginHeaderWidget(loginUserInfo);
            },
          ),
        ),
        Container(
          child: Text(
            "222222222",
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
        Container(
          child: Text(
            "3333",
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }

  Widget getLoginHeaderWidget (LoginUserInfo loginUserInfo) {
    if (loginUserInfo.loginUserResponse == null) {
      return Text("前去登录", style: TextStyle(color: Colors.white),);
    }
    return Row(
      children: <Widget>[
        ClipOval(
          child: loginUserInfo.loginUserResponse != null ?
          Image.network(UIUtils.replaceMediaUrl(loginUserInfo.loginUserResponse.headerIcon), width: 20, height: 20, fit: BoxFit.fill,) : null,
        ),
        SizedBox(width: 5,),
        Text(
          loginUserInfo.loginUserResponse != null
              ? loginUserInfo.loginUserResponse.nickName
              : "",
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
        ),
      ],
    );
  }
}
