import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/swiper_info.dart';

class TabRecommendWidget extends StatefulWidget {
  @override
  _TabRecommenState createState() => _TabRecommenState();
}

class _TabRecommenState extends State<TabRecommendWidget>
    with TickerProviderStateMixin {
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
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          SwiperDataWidget(),
          SpecialContentWidget(),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
        ],
      ),
    );
  }
}

class SpecialContentWidget extends StatefulWidget {
  @override
  _SpecialContentState createState() => _SpecialContentState();
}

class _SpecialContentState extends State<SpecialContentWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              Text(
                "特色功能",
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
              ),
              Expanded(child: Text("")),
              Text(
                "查看更多",
                style: TextStyle(
                    fontSize: 14,
                    color: Colors.white,
                    backgroundColor: Colors.grey),
              ),
            ],
          ),
          SizedBox(height: 5,),
          GridView.count(
            shrinkWrap: true,
            //水平子 Widget 之间间距
            crossAxisSpacing: 10.0,
            //垂直子 Widget 之间间距
            mainAxisSpacing: 10.0,
            //GridView内边距
//            padding: EdgeInsets.all(10.0),
            //一行的Widget数量
            crossAxisCount: 2,
            //子Widget宽高比例
            childAspectRatio: 2.0,
            //子Widget列表
            children: <Widget>[
              ClipRRect(
                borderRadius: BorderRadius.circular(5),
                child: Image.asset(
                  "images/image_wenjuan.png",
                  fit: BoxFit.fill,
                ),
              ),
              ClipRRect(
                borderRadius: BorderRadius.circular(5),
                child: Image.asset(
                  "images/image_kaoshi.png",
                  fit: BoxFit.fill,
                ),
              ),
            ],
          )
        ],
      ),
    );
  }
}
