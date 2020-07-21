
import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/swiper_info.dart';

class TabRecommendWidget extends StatefulWidget {
  @override
  _TabRecommenState createState() => _TabRecommenState();
}


class _TabRecommenState extends State<TabRecommendWidget> with TickerProviderStateMixin {

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      //滑动的方向 Axis.vertical为垂直方向滑动，Axis.horizontal 为水平方向
      scrollDirection: Axis.vertical,
      //true 滑动到底部
      reverse: false,
      padding: EdgeInsets.all(0.0),
      ////滑动到底部回弹效果
      physics: BouncingScrollPhysics(),
      child: Column(
        children: <Widget>[
          SwiperDataWidget(),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
        ],
      ),
    );
  }



}