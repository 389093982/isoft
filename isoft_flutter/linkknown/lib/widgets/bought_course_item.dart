import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/pay_order_response.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

import 'button_label.dart';

class BoughtCourseItemWidget extends StatefulWidget {
  Order order;

  BoughtCourseItemWidget(this.order);

  @override
  _BoughtCourseItemState createState() => _BoughtCourseItemState();
}

class _BoughtCourseItemState extends State<BoughtCourseItemWidget>
    with TickerProviderStateMixin {
  _BoughtCourseItemState();

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 1.2,
      //设置shape，这里设置成了R角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(4.0)),
      ),
      // 对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
      clipBehavior: Clip.antiAlias,
      semanticContainer: false,
      child: Row(
        children: <Widget>[
          // Stack类似FrameLayout,子 widget可以通过父容器的四个角固定位置,子widget可以重叠
          Stack(
            children: <Widget>[
              InkWell(
                onTap: () {
                  NavigatorUtil.goRouterPage(
                      context, "${Routes.courseDetail}?course_id=${widget.order.goodsId}");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  padding: EdgeInsets.all(10),
                  child: Image.network(
                    UIUtils.replaceMediaUrl(widget.order.goodsImg),
                    width: 130,
                    height: 100,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
            ],
          ),
          Container(
            padding: EdgeInsets.all(10),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                SizedBox(height: 5,),
                Text(widget.order.goodsDesc,style: TextStyle(fontSize: 15),),
                SizedBox(height: 5,),
                Text("course_theme_type"==widget.order.goodsType?"课程":"其他",style: TextStyle(fontSize: 12,color: Colors.black54),),
                SizedBox(height: 5,),
                Row(
                  children: <Widget>[
                    Text("支付金额: ", style: TextStyle(fontSize: 12,color: Colors.black54),),
                    Text(Constants.RMB + widget.order.paidAmount, style: TextStyle(color: Colors.red),),
                  ],
                ),

                SizedBox(height: 5,),
                Text("交易日期: "+formatTransTime(widget.order), style: TextStyle(fontSize: 12,color: Colors.black54),),
              ],
            ),
          ),
        ],
      ),
    );
  }


  //格式化交易日期
  String formatTransTime(Order order){
    return order.transTime.substring(0,4) + "-" + order.transTime.substring(4,6) + "-" + order.transTime.substring(6,8);
  }


}
