import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

class CouponItemWidget extends StatefulWidget {
  Coupon coupon;

  CouponItemWidget(this.coupon);

  @override
  _CouponItemState createState() => _CouponItemState();
}

class _CouponItemState extends State<CouponItemWidget>
    with TickerProviderStateMixin {

  _CouponItemState();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(5.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(widget.coupon.activityDesc),
          Container(
            margin: EdgeInsets.only(top: 2),
            child: Row(
              children: <Widget>[
                // 课程集数和播放次数
                Image.asset(
                  "images/ic_views.png",
                  width: 15,
                  height: 15,
                ),
                Text(widget.coupon.couponId.toString()),
                Image.asset(
                  "images/ic_list_counts.png",
                  width: 15,
                  height: 15,
                ),
                Text(widget.coupon.couponId.toString()),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
