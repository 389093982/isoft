import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/my_coupon_response.dart';
import 'package:linkknown/response/query_coupon_by_id_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

class SelectedCouponItemWidget extends StatefulWidget {
  SelectedCoupon selectedCoupon;

  SelectedCouponItemWidget(this.selectedCoupon);

  @override
  _SelectedCouponItemState createState() => _SelectedCouponItemState();
}

class _SelectedCouponItemState extends State<SelectedCouponItemWidget>
    with TickerProviderStateMixin {
  _SelectedCouponItemState();

  String startDate;
  String endDate;

  @override
  void initState() {
    super.initState();
    queryActivity();
  }

  //查询券对应的活动
  queryActivity(){
    String search_start_date = "";
    String search_end_date = "";
    String search_activity_id = widget.selectedCoupon.activityId;
    int currentPage = 1;
    int offset = 10;
    LinkKnownApi.QueryPagePayActivity(search_start_date, search_end_date,  search_activity_id,  currentPage,  offset).then((QueryPagePayActivityResponse) async {
      if (QueryPagePayActivityResponse?.status == "SUCCESS") {
        this.startDate = QueryPagePayActivityResponse.activityDatas.first.startDate;
        this.endDate = QueryPagePayActivityResponse.activityDatas.first.endDate;
        setState(() {
          //刷新优惠券-- 里的日期
        });
      } else {
        UIUtil.showToast(QueryPagePayActivityResponse.errorMsg);
      }
    }).catchError((err) {});
  }

  @override
  Widget build(BuildContext context) {
    bool isGeneralCoupon = widget.selectedCoupon.couponType == "general";
    bool isDisCount = widget.selectedCoupon.youhuiType == "discount";
    return Container(
      margin: EdgeInsets.all(5),
      color: Colors.white,
      child: Row(
        children: <Widget>[
          Expanded(
              child: Padding(
            padding: EdgeInsets.all(15),
            child: Column(
              children: <Widget>[
                Row(
                  children: <Widget>[
                    Text(
                      isDisCount ? (double.parse(widget.selectedCoupon.discountRate) * 10).toStringAsFixed(1) + "折" : Constants.RMB + widget.selectedCoupon.couponAmount,
                      style: TextStyle(color: Colors.red, fontSize: 20, fontWeight: FontWeight.bold),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    ),
                    Text(
                        isDisCount ? "" : "  满 ${widget.selectedCoupon.goodsMinAmount} 元减 ${widget.selectedCoupon.couponAmount} 元",
                        style: TextStyle(color: Colors.grey[600],fontSize: 12),
                        strutStyle: LinkKnownTextStyle.couponStrutStyle),
                  ],
                ),
                Row(
                  children: <Widget>[
                    Text(isGeneralCoupon ? "通用券" : "指定券",
                        style: TextStyle(color: Colors.grey[600],fontSize: 13),
                        strutStyle: LinkKnownTextStyle.couponStrutStyle
                    ),
                    Text(" 适用于",
                      style: TextStyle(color: Colors.grey[600],fontSize: 13),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    ),
                    Text(
                      isGeneralCoupon ? "所有付费课程" : "${widget.selectedCoupon.targetName}",
                      style: TextStyle(color: isGeneralCoupon ? Colors.red : Colors.blueAccent[700],fontSize: 13),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    )
                  ],
                ),
                Row(
                  children: <Widget>[
                    Text(
                      "活动日期:  ${startDate} - ${endDate}",
                      style: TextStyle(color: Colors.grey[600],fontSize: 13),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    )
                  ],
                )
              ],
            ),
          )),
          Stack(
            alignment: Alignment.center,
            children: <Widget>[
              Image.asset(
                getCouponBgPicture(widget.selectedCoupon),
                width: 78,
                height: 130,
              ),
              Column(
                mainAxisSize: MainAxisSize.min,
                children: <Widget>[
                  Text(
                    getFirstCharacter(widget.selectedCoupon),
                    style: TextStyle(color: Colors.white),
                  ),
                  Text(
                    getSecondCharacter(widget.selectedCoupon),
                    style: TextStyle(color: Colors.white),
                  ),
                  Text(
                    getThirdCharacter(widget.selectedCoupon),
                    style: TextStyle(color: Colors.white),
                  )
                ],
              ),
            ],
          ),
        ],
      ),
    );
  }


  //获取券的底色图片
  getCouponBgPicture(SelectedCoupon coupon){
    return "images/coupon_grey.png";
  }

  //获取第一个汉子
  getFirstCharacter(SelectedCoupon coupon){
    return "已";
  }

  //获取第二个汉子
  getSecondCharacter(SelectedCoupon coupon){
    return "使";
  }

  //获取第三个汉子
  String getThirdCharacter(SelectedCoupon coupon){
    return "用";
  }


}
