import 'package:flutter/material.dart';
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

class MineHeaderWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text("111111");
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