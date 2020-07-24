import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/utils.dart';

class SwiperData {
  String imagePath;
  String redirectUrl;

  SwiperData(this.imagePath, this.redirectUrl);
}

class SwiperDataWidget extends StatefulWidget {

  EdgeInsets margin;
  double height;

  SwiperDataWidget({this.margin, this.height});

  @override
  _SwiperDataState createState() => _SwiperDataState();
}

class _SwiperDataState extends State<SwiperDataWidget> {

  List<SwiperData> swperList = [];

  @override
  void initState() {
    super.initState();
    swperList
      ..add(SwiperData("images/banner_coupon.png", Routes.login))
      ..add(SwiperData("images/banner_biancheng.png", Routes.login))
      ..add(SwiperData("images/banner_shaoer.png", Routes.login))
      ..add(SwiperData("images/banner_huodong.png", Routes.login));
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: widget.margin ?? const EdgeInsets.all(10),
      width: MediaQuery.of(context).size.width,
      height: widget.height ?? 160,
      alignment: Alignment(0, 0),
      // 设置圆角属性
      child: ClipRRect(
        borderRadius: BorderRadius.circular(5),
        child: Swiper(
          itemBuilder: _swiperBuilder,
          itemCount: swperList.length,
          scale: 0.99, // 两张图片之间的间隔
          pagination: new SwiperPagination(
              builder: DotSwiperPaginationBuilder(
                color: Colors.black54,
                activeColor: Colors.white,
                size: 5,
                activeSize: 5,
              )),
          control: new SwiperControl(
            iconNext: null,
            iconPrevious: null,
          ),
          scrollDirection: Axis.horizontal,
          autoplay: true,
          onTap: (index) => UIUtils.showToast('点击了第$index个,需要调往xxx页面'),
        ),
      ),
    );
  }

  Widget _swiperBuilder(BuildContext context, int index) {
    return Image.asset(
      swperList[index].imagePath,
      fit: BoxFit.cover,
    );
  }

}