import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/course_detail_response.dart';
import 'package:linkknown/page/course_introduce_widget.dart';
import 'package:linkknown/page/user_course_widget.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/accept_invite_button_label.dart';
import 'package:linkknown/widgets/attention_off_button_label.dart';
import 'package:linkknown/widgets/attention_on_button_label.dart';
import 'package:linkknown/widgets/header_icon.dart';

import 'my_customer_widget.dart';

class PersonalCenterPage extends StatefulWidget {
  String userName;
  PersonalCenterPage(this.userName);

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

  //是否展示关注按钮
  bool showAttentionButton = false;
  //是否已关注
  bool isAttention = false;

  int tabCounts = 3;
  TabController tabController;


  @override
  void initState() {
    super.initState();
    // 用来控制controller对应widget的各种各样交互行为以及状态变化的控制（类似于widget本身只是一个静态的物品，而通过对controller的操作控制让这个widget活了起来）
    this.tabController = TabController(length: tabCounts, vsync: this);

    initData();
    QueryIsAttention();
  }


  void initData() async {
    String userName = await LoginUtil.getLoginUserName();
    if(userName==widget.userName){
      headIcon = await LoginUtil.getSmallIcon();
      nickName = await LoginUtil.getNickName();
      gender = await LoginUtil.getGender();
      userPoints = await LoginUtil.getUserPoints();
      attentionCounts = await LoginUtil.getAttentionCounts();
      fensiCounts = await LoginUtil.getFensiCounts();
      userSignature = await LoginUtil.getUserSignature();
      //拿到数据后做个通知，重新执行build
      setState(() {});
    }else{
      LinkKnownApi.getUserDetail(widget.userName).then((GetUserDetailResponse) {
        if(GetUserDetailResponse.status=="SUCCESS"){
          headIcon = GetUserDetailResponse.user.smallIcon;
          nickName = GetUserDetailResponse.user.nickName;
          gender = GetUserDetailResponse.user.gender;
          userPoints = GetUserDetailResponse.user.userPoints.toString();
          attentionCounts = GetUserDetailResponse.user.attentionCounts.toString();
          fensiCounts = GetUserDetailResponse.user.fensiCounts.toString();
          userSignature = GetUserDetailResponse.user.userSignature;
          //拿到数据后做个通知，重新执行build
          setState(() {});
        }
      }).catchError((e) {});
    }
  }


  //是否显示关注按钮
  QueryIsAttention() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    if(StringUtil.checkNotEmpty(loginUserName)){
      //登录人和作者不是同一个人，才能看到按钮
      if(loginUserName!=widget.userName){
        LinkKnownApi.QueryIsAttention("user",widget.userName).then((value) {
          if(value.status=="SUCCESS"){
            //展示按钮
            showAttentionButton = true;
            value.attentionRecords>0 ? isAttention=true : isAttention=false;
          }
          setState(() {});
        }).catchError((e) {
          UIUtil.showToast((e as LinkKnownError).errorMsg);
        });
      }else{
        //自己，则不展示按钮
        showAttentionButton = false;
        setState(() {});
      }
    }else{
      //未登录，可以看到按钮
      showAttentionButton = true;
      //可以看到按钮,但是必须是未关注的
      isAttention = false;
      setState(() {});
    }

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
                            StringUtil.checkNotEmpty(headIcon)?HeaderIconWidget(headIcon, HeaderIconSize.SIZE_BIG_80):Image.asset(
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
                        Positioned(top: 290, left: 40, child: Text((userSignature??""),style: TextStyle(fontSize: 12,color: Colors.black54),overflow: TextOverflow.ellipsis,),),
                        Positioned(top: 230, left: 250, child: showAttentionButton ?
                        (isAttention?
                        GestureDetector(
                          onTap: (){doAttention("user", widget.userName, "off");},
                          child: AttentionOnButtonLabel("已关注"),
                        )
                            :
                        GestureDetector(
                          onTap: (){doAttention("user", widget.userName, "on");},
                          child: AttentionOffButtonLabel("+ 关注"),
                        )
                        )
                            :
                        Text(""),),

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
            UserCourseWidget(widget.userName,"DISPLAY_TYPE_NEW"),
            UserCourseWidget(widget.userName,"DISPLAY_TYPE_FAVORITE"),
            UserCourseWidget(widget.userName,"DISPLAY_TYPE_VIEWED"),
          ]),
        ),
      ),
    );
  }



  //关注和取消
  doAttention(String attention_object_type,String attention_object_id,String state) async {
    bool isLogin = await LoginUtil.checkHasLogin();
    if(!isLogin){
      UIUtil.showToast("未登录..");
      return;
    }
    LinkKnownApi.DoAttention(attention_object_type,attention_object_id,state).then((value) {
      if(value.status=="SUCCESS"){
        if(state=="on"){
          UIUtil.showToast("关注成功");
          isAttention = true;
        }else if(state=="off"){
          UIUtil.showToast("取消成功");
          isAttention = false;
        }
        showAttentionButton = true;
      }else{
        UIUtil.showToast(value.errorMsg);
      }
      setState(() {});
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
  }



}




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
