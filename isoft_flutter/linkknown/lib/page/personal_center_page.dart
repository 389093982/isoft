import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/page/course_comment.dart';
import 'package:linkknown/page/course_introduce.dart';
import 'package:linkknown/page/user_course.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/accept_invite_button_label.dart';
import 'package:linkknown/widgets/attention_off_button_label.dart';
import 'package:linkknown/widgets/attention_on_button_label.dart';

import 'my_customer.dart';

class PersonalCenterPage extends StatefulWidget {
  PersonalCenterPage();

  @override
  _PersonalCenterPageState createState() => _PersonalCenterPageState();
}

class _PersonalCenterPageState extends State<PersonalCenterPage> with TickerProviderStateMixin {
  String headIcon;
  String nickName;
  String gender;
  String userPoints;
  String attentionCounts;
  String fensiCounts;
  String userSignature;
  _PersonalCenterPageState();

  int tabCounts = 3;
  TabController tabController;


  @override
  void initState() {
    super.initState();
    // 用来控制controller对应widget的各种各样交互行为以及状态变化的控制（类似于widget本身只是一个静态的物品，而通过对controller的操作控制让这个widget活了起来）
    this.tabController = TabController(length: tabCounts, vsync: this);

    initData();
  }

  void initData() async {
    headIcon = await LoginUtil.getSmallIcon();
    nickName = await LoginUtil.getNickName();
    gender = await LoginUtil.getGender();
    userPoints = await LoginUtil.getUserPoints();
    attentionCounts = await LoginUtil.getAttentionCounts();
    fensiCounts = await LoginUtil.getFensiCounts();
    userSignature = await LoginUtil.getUserSignature();

    //拿到数据后做个通知，重新执行build
   setState(() {

   });
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: tabCounts,
      child: Scaffold(
        body: NestedScrollView(
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return [
              SliverAppBar(
                leading: Container(
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
                expandedHeight: 300,
                // 一个显示在 AppBar 下方的控件，高度和 AppBar 高度一样，可以实现一些特殊的效果，该属性通常在 SliverAppBar 中使用
                flexibleSpace: FlexibleSpaceBar(
                  //centerTitle: true,//靠左显示
                  title: Text(""),
                  background: Container(
                    height: 100,
                    color: Colors.white,
                    child: Stack(
                      children: <Widget>[
                        Image.asset(
                          "images/personal_center_img02.jpg",
                          fit: BoxFit.fill,
                          width: 750,
                          height: 180,
                        ),
                        Positioned(
                          top: 140,
                          left: 30,
                          child: ClipOval(
                            child:
                            StringUtil.checkNotEmpty(headIcon)?Image.network(
                              UIUtils.replaceMediaUrl(headIcon??""),
                              width: 80,
                              height: 80,
                              fit: BoxFit.fill,
                            ):Image.asset(
                                "images/linkknown.jpg",
                                width: 80,
                                height: 80,
                                fit: BoxFit.fill,
                            )
                          ),
                        ),
                        Positioned(top: 230, left: 40, child: Row(children: <Widget>[
                          Text(nickName??"",style: TextStyle(fontSize: 17,color: Colors.black54),),
                          Image.asset(
                            gender=="male"?"images/ic_male.png":"images/ic_female.png",
                            height: 20,
                            fit: BoxFit.fill,
                          ),
                        ],),),
                        Positioned(top: 250, left: 40, child: Text("积分: "+(userPoints??""),style: TextStyle(fontSize: 13,color: Colors.black54)),),
                        Positioned(top: 270, left: 40, child: Text("关注: "+(attentionCounts??"") + "  粉丝: "+(fensiCounts??""),style: TextStyle(fontSize: 13,color: Colors.black54)),),
                        Positioned(top: 290, left: 40, child: Text((userSignature??""),style: TextStyle(fontSize: 13,color: Colors.black54)),),
                        Positioned(top: 230, left: 230, child: AttentionOffButtonLabel("+ 关注"),),
                        Positioned(top: 260, left: 230, child: AttentionOnButtonLabel("已关注"),),

                      ],
                    ))
                ),
              ),
              // SliverPersistentHeader最重要的一个属性是SliverPersistentHeaderDelegate，为此我们需要实现一个类继承自SliverPersistentHeaderDelegate
              SliverPersistentHeader(
                pinned: true,
                delegate: StickyTabBarDelegate(
                  child: TabBar(
                    indicatorColor: Colors.pinkAccent,
                    unselectedLabelColor: Colors.grey[600],
                    labelColor: Colors.pinkAccent,
                    indicatorSize: TabBarIndicatorSize.label,
                    controller: this.tabController,
                    tabs: <Widget>[
                      Tab(text: '发布的课程'),
                      Tab(text: '收藏的课程'),
                      Tab(text: '观看的课程'),
                    ],
                  ),
                ),
              ),
            ];
          },
          body: TabBarView(controller: this.tabController, children: [
            UserCourseWidget("DISPLAY_TYPE_NEW"),
            UserCourseWidget("DISPLAY_TYPE_FAVORITE"),
            UserCourseWidget("DISPLAY_TYPE_VIEWED"),
          ]),
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
