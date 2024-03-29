import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/clickable_textimage.dart';
import 'package:linkknown/widgets/function_button_label.dart';
import 'package:linkknown/widgets/header_icon.dart';
import 'package:provider/provider.dart';

class MinePage extends StatefulWidget {
  @override
  _MinePageState createState() => _MinePageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _MinePageState extends State<MinePage>
    with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Flutter去掉AppBar避免body溢出到状态栏
      // 没有AppBar的Flutter，如果不在Scaffold中使用AppBar会发现默认是沉浸式，预留出状态栏的高度方法
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(MediaQuery.of(context).size.height * 0),
        child: Container(
          width: double.infinity,
          height: double.infinity,
          decoration: BoxDecoration(
            color: Colors.red,
//              gradient: LinearGradient(colors: [Colors.yellow, Colors.pink])
          ),
        ),
      ),
      body: ListView(
        shrinkWrap: true,
        children: <Widget>[
          MineHeaderWidget(),
          MineCenterWidget(),
          MineFooterWidget(),
        ],
      ),
    );
  }
}

class MineHeaderWidget extends StatefulWidget {
  @override
  _MineHeaderState createState() => _MineHeaderState();
}

class _MineHeaderState extends State<MineHeaderWidget> with TickerProviderStateMixin {
  bool hasLogin = false;
  String loginUserName;

  queryLoginUserInfo() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }

  String nickName = "";
  String smallIcon = "";
  String userPoints = "";
  String userSignature = "";
  String attentionCounts = "";
  String fensiCounts = "";
  String vipLevel = "0";
  String vipExpiredTime = "";

  // 铃铛旋转动画控制器
  AnimationController rotationAnimationController;
  Animation rotationAnimation;

  StreamSubscription subscription;

  @override
  void initState() {
    super.initState();
    refreshLoginStatus();

    rotationAnimationController =
        AnimationController(duration: const Duration(seconds: 1), vsync: this);
    rotationAnimation =
        Tween(begin: -0.08, end: 0.08).animate(rotationAnimationController);
    rotationAnimationController.repeat(reverse: true);


    subscription = eventBus.on<LoginStateChangeEvent>().listen((event) {
      refreshLoginStatus();
    });
  }

  refreshLoginStatus() async {
    bool hasLogin = await LoginUtil.checkHasLogin();

    this.hasLogin = hasLogin;
    if (hasLogin) {
      this.nickName = await LoginUtil.getNickName();
      this.smallIcon = await LoginUtil.getSmallIcon();
      this.userPoints = await LoginUtil.getUserPoints();
      this.userSignature = await LoginUtil.getUserSignature();
      this.attentionCounts = await LoginUtil.getAttentionCounts();
      this.fensiCounts = await LoginUtil.getFensiCounts();
      this.vipLevel = await LoginUtil.getVipLevel();
      this.vipExpiredTime = await LoginUtil.getVipExpiredTime();
    }

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserInfo();
      return Text("");
    }
    return Container(
      decoration: BoxDecoration(
        // 添加背景图片
        image: new DecorationImage(
          image: new AssetImage("images/my_center_01.jpg"),
          fit: BoxFit.fill,
        ),
      ),
      child: Stack(
        children: <Widget>[
          Container(
            height: 180,
//            color: Colors.red,
            child: hasLogin ? getLoginWidget() : getUnLoginWidget(),
          ),
          Align(
            alignment: Alignment.topRight,
            child: getSettingMessageWidget(),
          ),
        ],
      ),
    );
  }

  Widget getSettingMessageWidget() {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        Container(
          margin: EdgeInsets.all(5),
          child: GestureDetector(
            onTap: () {
              NavigatorUtil.goRouterPage(context, Routes.setting);
            },
            child: SvgPicture.asset(
              "images/setting.svg",
              color: Colors.white,
              width: 23,
              height: 23,
            ),
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
              child: SvgPicture.asset(
                "images/lingdang.svg",
                color: Colors.white,
                width: 23,
                height: 23,
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget getLoginWidget() {
    return Row(
      children: <Widget>[
        Container(
          margin: EdgeInsets.only(left: 30),
          child: ClipOval(
            child: smallIcon==""?
            Image.asset(
              "images/linkknown.jpg",
              width: 80,
              height: 80,
              fit: BoxFit.cover,
            )
                :
            HeaderIconWidget(smallIcon, HeaderIconSize.SIZE_BIG_80),
          ),
        ),
        Container(
          width: 240,
          margin: EdgeInsets.only(left: 10),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text(
                nickName,
                style: TextStyle(color: Colors.white, fontSize: 20),
              ),
              Row(children: <Widget>[
                Text("积分: ${userPoints}", style: TextStyle(color: Colors.white),),
                SizedBox(width: 10,),
                GestureDetector(
                  onTap: () async {
                    await NavigatorUtil.goRouterPage(context, Routes.openVip);
                    setState(() {
                      //刷新一下
                    });
                  },
                  child: FunctionButtonLabel(
                    labelText: "vip",
                    labelTextColor: int.parse(this.vipLevel)>0 && DateUtil.compareStandardTime(DateUtil.format2StandardTime(this.vipExpiredTime), DateUtil.getNow_yyyyMMddHHmmss()) ? Colors.white:Colors.black45,
                    labelSize: 16,
                    borderRadius: 20,
                    borderHeight: 20,
                  ),
                ),
              ],),
              Row(children: <Widget>[
                GestureDetector(
                  onTap: (){
                    NavigatorUtil.goRouterPage(context, Routes.myAttention);
                  },
                  child: Text("关注: ${attentionCounts}     ", style: TextStyle(color: Colors.white),),
                ),
                GestureDetector(
                  onTap: (){
                    NavigatorUtil.goRouterPage(context, Routes.myFensi);
                  },
                  child: Text("粉丝: ${fensiCounts}", style: TextStyle(color: Colors.white),),
                ),
              ],),
              Text(
                StringUtil.checkEmpty(userSignature) == true
                    ? "这个家伙很懒，什么个性签名都没有留下"
                    : userSignature,
                style: TextStyle(color: Colors.white, fontSize: 12),
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget getUnLoginWidget() {
    return Row(
      children: <Widget>[
        Container(
          margin: EdgeInsets.only(left: 40),
          child: ClipOval(
            child: Image.asset(
              "images/unlogin_head_no_circle.png",
              width: 80,
              height: 80,
              fit: BoxFit.fill,
            ),
          ),
        ),
        Container(
          margin: EdgeInsets.only(left: 20),
          child: InkWell(
            onTap: () {
              NavigatorUtil.goLoginPage(context);
            },
            child: Text(
              "登录/注册",
              style: TextStyle(color: Colors.white, fontSize: 30),
            ),
          ),
        ),
      ],
    );
  }

  @override
  void dispose() {
    subscription?.cancel();
    // 释放铃铛旋转动画
    rotationAnimationController?.dispose();
    super.dispose();
  }
}

class MineCenterWidget extends StatefulWidget {

  @override
  _MineCenterState createState() => _MineCenterState();
}

class _MineCenterState extends State<MineCenterWidget> with TickerProviderStateMixin {

  String loginUserName;

  @override
  void initState() {
    super.initState();
  }

  queryLoginUserInfo() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }

  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserInfo();
      return Text("");
    }
    return Container(
      alignment: Alignment.center,
      margin: EdgeInsets.all(10),
      padding: EdgeInsets.all(10),
      decoration: BoxDecoration(
        //背景
        color: Colors.white,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(5.0)),
        //设置四周边框
        border: new Border.all(width: 1, color: Colors.grey[300]),
      ),
      child: Row(
        children: <Widget>[
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/ic_coupon.svg",
                icon_color: Colors.white,
                text: "优惠券",
                onTap: () {
                  if(StringUtil.checkEmpty(this.loginUserName)){
                    UIUtil.showToast("未登录..");
                  }else{
                    NavigatorUtil.goRouterPage(context, Routes.myCoupon);
                  }
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/ic_shopping_cart.svg",
                icon_color: Colors.white,
                text: "购物车",
                onTap: () {
                  if(StringUtil.checkEmpty(this.loginUserName)){
                    UIUtil.showToast("未登录..");
                  }else{
                    NavigatorUtil.goRouterPage(context, Routes.shoppingCart);
                  }
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/ic_order.svg",
                icon_color: Colors.white,
                text: "订单",
                onTap: () {
                  if(StringUtil.checkEmpty(this.loginUserName)){
                    UIUtil.showToast("未登录..");
                  }else{
                    NavigatorUtil.goRouterPage(context, Routes.payOrder);
                  }
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/ic_activity.svg",
                icon_color: Colors.white,
                text: "活动中心",
                onTap: () {
//                  if(StringUtil.checkEmpty(this.loginUserName)){
//                    UIUtil.showToast("未登录..");
//                  }else{
                    NavigatorUtil.goRouterPage(context, Routes.comingSoon);
//                  }
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/ic_kaoshi.svg",
                icon_color: Colors.white,
                text: "考试",
                onTap: () {
//                  if(StringUtil.checkEmpty(this.loginUserName)){
//                    UIUtil.showToast("未登录..");
//                  }else{
                    NavigatorUtil.goRouterPage(context, Routes.comingSoon);
//                  }
                },
              ),
            ),
          ),
        ],
      ),
    );
  }

}


class FooterItem {
  String icon;
  String text;
  String router; // 跳往的路由
  FooterItem(this.icon, this.text, this.router);
}

class MineFooterWidget extends StatefulWidget {
  @override
  _MineFooterState createState() => _MineFooterState();
}

class _MineFooterState extends State<MineFooterWidget> with TickerProviderStateMixin {

  String loginUserName;

  @override
  void initState() {
    super.initState();
  }

  queryLoginUserInfo() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }

  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserInfo();
      return Text("");
    }
    return Container(
      margin: EdgeInsets.only(top: 10),
      child: Column(
        children: <Widget>[
          getOperateWidget(context,
              FooterItem("images/ic_cloud_blog.png", '云博客', Routes.cloudBlog)),
          getOperateWidget(
              context,
              FooterItem("images/ic_personal_center.png", '个人中心', "${Routes.personalCenter}?userName=${loginUserName}")),
          getOperateWidget(context,
              FooterItem("images/ic_course.png", '已购课程', Routes.boughtCourse)),
          getOperateWidget(context,
              FooterItem("images/ic_advise.png", '我要吐槽', Routes.advise)),
          getOperateWidget(
              context, FooterItem("images/ic_about.png", '关于链知', Routes.about,), requireLogin: false),
          getOperateWidget(context,
              FooterItem("images/ic_link.png", '我与链知', Routes.linkknownWithMe)),
        ],
      ),
    );
  }


  Widget getOperateWidget(BuildContext context, FooterItem item, {bool requireLogin = true}) {
    return Container(
      decoration: BoxDecoration(
          color: Colors.white,
          border: Border(bottom: BorderSide(width: 1, color: Colors.black12))),
      child: InkWell(
        onTap: () {
          if(requireLogin && (this.loginUserName==null || this.loginUserName == "")){
            UIUtil.showToast("未登录..");
          }else{
            NavigatorUtil.goRouterPage(context, item.router);
          }
        },
        child: ListTile(
          leading: Image.asset(
            UIUtil.replaceMediaUrl(item.icon),
            width: 22,
            height: 22,
          ),
          title: Transform(
            transform: Matrix4.translationValues(-20, 0, 0),
            child: Text(
              item.text,
              style: TextStyle(fontSize: 14),
            ),
          ),
          trailing: Icon(Icons.keyboard_arrow_right),
        ),
      ),
    );
  }

}
