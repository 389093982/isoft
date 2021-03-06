import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/page/course_filter_widget.dart';
import 'package:linkknown/page/tab_recommend_widget.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/header_icon.dart';
import 'package:linkknown/widgets/home_drawer.dart';
import 'package:linkknown/widgets/ming_yan.dart';
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
    TabViewModel(title: '会员', widget: CourseFilterWidget("", "vip")),
    TabViewModel(title: '全部', widget: CourseFilterWidget("", "")),
    TabViewModel(title: '推荐', widget: TabRecommendWidget()),
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
            height: 80,
            padding: EdgeInsets.only(top: 10),
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
        preferredSize: Size.fromHeight(90.0),
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

class _HomeHeaderWidgetState extends State<_HomeHeaderWidget> with TickerProviderStateMixin {

  // 铃铛旋转动画控制器
  AnimationController rotationAnimationController;
  Animation rotationAnimation;

  bool hasLogin = false;
  String nickName = "";
  String headerIcon = "";

  StreamSubscription subscription;

  String loginUserName;

  queryLoginUserInfo() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }

  @override
  void initState() {
    super.initState();

    rotationAnimationController = AnimationController(duration: const Duration(seconds: 1), vsync: this);
    rotationAnimation = Tween(begin: -0.08, end: 0.08).animate(rotationAnimationController);
    rotationAnimationController.repeat(reverse: true);

    refreshLoginStatus();

    subscription = eventBus.on<LoginStateChangeEvent>().listen((event) {
      refreshLoginStatus();
    });
  }

  refreshLoginStatus() async {
    bool hasLogin = await LoginUtil.checkHasLogin();

    this.hasLogin = hasLogin;
    if (hasLogin) {
      this.nickName = await LoginUtil.getNickName();
      this.headerIcon = await LoginUtil.getSmallIcon();
    }

    setState(() {});
  }

  @override
  void dispose() {
    subscription?.cancel();
    // 释放铃铛旋转动画
    rotationAnimationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserInfo();
      return Text("");
    }
    return Row(
      children: <Widget>[
        Container(
          height: 50,
          width: 80,
          child: getLoginHeaderWidget(),
        ),
        Expanded(
          child: Container(
            transform: Matrix4.translationValues(0, -3, 0),
            child: Center(child: MingYanWidget(),),
          ),
        ),
        Container(
          margin: EdgeInsets.only(right: 15),
          child: GestureDetector(
            onTap: () {
              if(this.loginUserName==null || this.loginUserName==""){
                UIUtil.showToast("未登录..");
              }else{
                NavigatorUtil.goRouterPage(context, Routes.message);
              }
            },
            // 给铃铛添加旋转动画
            child: RotationTransition(
              turns: rotationAnimation,
              child: Container(
                transform: Matrix4.translationValues(0, -5, 0),
                child: SvgPicture.asset(
                  "images/lingdang.svg",
                  color: Colors.white,
                  width: 23,
                  height: 23,
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget getLoginHeaderWidget () {
    if (hasLogin) {
      return Row(
        children: <Widget>[
          InkWell(
            onTap: (){
              NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${this.loginUserName}");
            },
            child: Container(
              transform: Matrix4.translationValues(-10, -5, 0),
              child: HeaderIconWidget(headerIcon, HeaderIconSize.SIZE_SMALL_40),
            ),
          ),
          SizedBox(width: 5,),
//        Container(
//          color: Colors.blue[200],
//          child: Text(
//            nickName,
//            maxLines: 1,
//            overflow: TextOverflow.ellipsis,
//            style: TextStyle(fontSize: 14),
//          ),
//        ),
        ],
      );
    } else {
      return GestureDetector(
        onTap: () {
          NavigatorUtil.goRouterPage(context, Routes.login);
        },
        child: Container(
          transform: Matrix4.translationValues(-10, 4, 0),
          child: Text("未登录", style: TextStyle(color: Colors.white,fontSize: 20),),
        ),
      );
    }
  }
}
