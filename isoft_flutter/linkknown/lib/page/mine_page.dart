import 'package:flutter/material.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/clickable_textimage.dart';

class MinePage extends StatefulWidget {
  @override
  _MinePageState createState() => _MinePageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _MinePageState extends State<MinePage> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Flutter去掉AppBar避免body溢出到状态栏
      // 没有AppBar的Flutter，如果不在Scaffold中使用AppBar会发现默认是沉浸式，预留出状态栏的高度方法
      appBar: PreferredSize(
        preferredSize:
            Size.fromHeight(MediaQuery.of(context).size.height * 0.07),
        child: SafeArea(
          top: true,
          child: Offstage(),
        ),
      ),
      backgroundColor: Colors.white,
      body: ListView(
        shrinkWrap: true,
        children: <Widget>[
          Column(
            children: <Widget>[
              MineHeaderWidget(),
              MineCenterWidget(),
              MineFooterWidget(),
            ],
          ),
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

  @override
  void initState() {
    super.initState();
    refreshLoginStatus();
  }

  refreshLoginStatus () async {
    bool hasLogin = await LoginUtil.checkHasLogin();
      this.setState(() {
        this.hasLogin = hasLogin;
      });
    }

  @override
  Widget build(BuildContext context) {
//    return hasLogin ? getLoginWidget() : getUnLoginWidget();
    return Stack(
      children: <Widget>[
        Container(
          height: 180,
          color: Colors.red,
          child: hasLogin ? getLoginWidget() : getUnLoginWidget(),
        ),
        Align(
          alignment: Alignment.topRight,
          child: getSettingMessageWidget(),
        ),
      ],
    );
  }

  Widget getSettingMessageWidget() {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        Container(
          margin: EdgeInsets.all(5),
          child: Image.asset("images/linkknown.jpg", width: 20, height: 20,),
        ),
        Container(
          margin: EdgeInsets.all(5),
          child: Image.asset("images/linkknown.jpg", width: 20, height: 20,),
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
            child: Image.asset("images/linkknown.jpg", width: 80, height: 80,),
          ),
        ),
        Container(
          margin: EdgeInsets.only(left: 10),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text("用户名", style: TextStyle(color: Colors.white, fontSize: 20),),
              Text("积分 10", style: TextStyle(color: Colors.white),),
              Text("关注 99 粉丝 99", style: TextStyle(color: Colors.white),),
              Text("这个家伙很懒，什么个性签名都没有留下", style: TextStyle(color: Colors.white),),
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
            child: Image.asset("images/linkknown.jpg", width: 80, height: 80,),
          ),
        ),
        Container(
          margin: EdgeInsets.only(left: 10),
          child: InkWell(
            onTap: () {
              NavigatorUtil.goLoginPage(context);
            },
            child: Text("登录/注册", style: TextStyle(color: Colors.white, fontSize: 30),),
          ),
        ),
      ],
    );
  }
}

class MineCenterWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
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
        border: new Border.all(width: 1, color: Colors.grey),
      ),
      child: Row(
        children: <Widget>[
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/linkknown.jpg",
                text: "优惠券",
                onTap: (){
                  UIUtils.showToast("优惠券");
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/linkknown.jpg",
                text: "购物车",
                onTap: (){
                  UIUtils.showToast("购物车");
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/linkknown.jpg",
                text: "订单",
                onTap: (){
                  UIUtils.showToast("订单");
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/linkknown.jpg",
                text: "活动中心",
                onTap: (){
                  UIUtils.showToast("活动中心");
                },
              ),
            ),
          ),
          Expanded(
            child: Center(
              child: ClickableTextImage(
                imgpath: "images/linkknown.jpg",
                text: "考试",
                onTap: (){
                  UIUtils.showToast("考试");
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
class MineFooterWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text("111111");
  }
}