import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/base_response.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/query_coupon_center_list_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

class ReceiveCouponCenterItemWidget extends StatefulWidget {
  Coupon coupon;
  VoidCallback callback;

  ReceiveCouponCenterItemWidget(this.coupon,{this.callback});

  @override
  _ReceiveCouponCenterItemState createState() => _ReceiveCouponCenterItemState();
}

class _ReceiveCouponCenterItemState extends State<ReceiveCouponCenterItemWidget> with TickerProviderStateMixin {
  _ReceiveCouponCenterItemState();

  @override
  Widget build(BuildContext context) {
    bool isGeneralCoupon = widget.coupon.couponType == "general";
    bool isDisCount = widget.coupon.youhuiType == "discount";
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
                      isDisCount ? (double.parse(widget.coupon.discountRate) * 10).toStringAsFixed(1) + "折" : Constants.RMB + widget.coupon.couponAmount,
                      style: TextStyle(color: Colors.red, fontSize: 20, fontWeight: FontWeight.bold),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    ),
                    Text(
                        isDisCount ? "" : "  满 ${widget.coupon.goodsMinAmount} 元减 ${widget.coupon.couponAmount} 元",
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
                      isGeneralCoupon ? "所有付费课程" : "${widget.coupon.targetName}",
                      style: TextStyle(color: isGeneralCoupon ? Colors.red : Colors.blueAccent[700],fontSize: 13),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    )
                  ],
                ),
                Row(
                  children: <Widget>[
                    Text(
                      "活动日期:  ${widget.coupon.startDate} - ${widget.coupon.endDate}",
                      style: TextStyle(color: Colors.grey[600],fontSize: 13),
                      strutStyle: LinkKnownTextStyle.couponStrutStyle,
                    )
                  ],
                )
              ],
            ),
          )),
          GestureDetector(
            onTap: (){
              receiveCoupon(widget.coupon.activityId);
              widget.callback();
            },
            child: Stack(
              alignment: Alignment.center,
              children: <Widget>[
                Image.asset(
                  getCouponBgPicture(widget.coupon),
                  width: 78,
                  height: 130,
                ),
                Column(
                  mainAxisSize: MainAxisSize.min,
                  children: <Widget>[
                    Text(
                      getFirstCharacter(widget.coupon),
                      style: TextStyle(color: Colors.white),
                    ),
                    Text(
                      getSecondCharacter(widget.coupon),
                      style: TextStyle(color: Colors.white),
                    ),
                    Text(
                      getThirdCharacter(widget.coupon),
                      style: TextStyle(color: Colors.white),
                    )
                  ],
                ),
              ],
            ),
          )
        ],
      ),
    );
  }


  //获取券的底色图片
  getCouponBgPicture(Coupon coupon){
    return "images/coupon_red.png";
  }

  //获取第一个汉子
  getFirstCharacter(Coupon coupon){
    return "去";
  }

  //获取第二个汉子
  getSecondCharacter(Coupon coupon){
    return "领";
  }

  //获取第三个汉子
  String getThirdCharacter(Coupon coupon){
    return "取";
  }


  //领券
  receiveCoupon(String activity_id){
    LinkKnownApi.receiveCoupon(activity_id).then((BaseResponse) {
      if(BaseResponse.status=="SUCCESS"){
        UIUtils.showToast("领取成功");
      }else{
        UIUtils.showToast(BaseResponse.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast("领取失败..");
    });
  }


}
