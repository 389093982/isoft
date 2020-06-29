
import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';

class TabRecommendWidget extends StatefulWidget {
  @override
  _TabRecommenState createState() => _TabRecommenState();
}

class _TabRecommenState extends State<TabRecommendWidget> with TickerProviderStateMixin {

  @override
  void initState() {
    super.initState();
  }

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
          Card(
            ///设置阴影的深度
            elevation: 3.0,
            ///增加圆角
            shape: new RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(10.0))),
            color: Colors.white,
            margin: const EdgeInsets.all(10),
            child: new Container(
              alignment: Alignment.centerLeft,
              padding: EdgeInsets.symmetric(horizontal: 10),
              height: 80,
              child: Swiper(
                itemBuilder: _swiperBuilder,
                itemCount: 3,
                pagination: new SwiperPagination(
                    builder: DotSwiperPaginationBuilder(
                      color: Colors.black54,
                      activeColor: Colors.white,
                    )),
                control: new SwiperControl(),
                scrollDirection: Axis.horizontal,
                autoplay: true,
                onTap: (index) => print('点击了第$index个'),
              ),
            ),
          ),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
          Text("好无聊奥~~"),
        ],
      ),
    );
  }

  Widget _swiperBuilder(BuildContext context, int index) {
    return Image.asset("images/banner_coupon.png");
  }

}