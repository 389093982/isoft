import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class CachedImageWidget extends StatelessWidget {
  String imageUrl;

  CachedImageWidget(this.imageUrl);

  @override
  Widget build(BuildContext context) {
    return CachedNetworkImage(
      fit: BoxFit.fill,
      width: double.infinity,
      imageUrl: imageUrl,
      placeholder: (context, url) => Container(
        width: double.infinity,
        color: Colors.grey[350],
        alignment: Alignment.center,
        child: Text(
          "正在加载中...",
          style: TextStyle(
              fontSize: ScreenUtil().setSp(26.0),
              color: Colors.white),
        ),
      ),
      errorWidget: (context, url, error) => Image.asset(
        "images/not_found.png",
        fit: BoxFit.fill,
        width: double.infinity,
      ),
    );
  }
}
