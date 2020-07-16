import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

import 'button_label.dart';

class GoodsItemWidget extends StatefulWidget {
  GoodsData goods;

  GoodsItemWidget(this.goods);

  @override
  _GoodsItemState createState() => _GoodsItemState();
}

class _GoodsItemState extends State<GoodsItemWidget>
    with TickerProviderStateMixin {
  _GoodsItemState();

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 3.0,
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
                      context, "${Routes.courseDetail}?course_id=11");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  padding: EdgeInsets.all(10),
                  child: Image.network(
                    UIUtils.replaceMediaUrl(widget.goods.smallImage),
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
                Text(widget.goods.courseName,style: TextStyle(fontSize: 15),),
                SizedBox(height: 5,),
                Row(
                  children: <Widget>[
                    Text(Constants.RMB + widget.goods.price, style: TextStyle(color: Colors.red),),
                    Container(
                      child: Offstage(
                        offstage: !(double.parse(widget.goods.goodsPriceOnAdd)-double.parse(widget.goods.price)>0),
                        child: Text(
                          "  比加入时降"+((double.parse(widget.goods.goodsPriceOnAdd)-double.parse(widget.goods.price)).toStringAsFixed(2))+"元",
                          style: TextStyle(color: Colors.red,fontSize: 11),
                        ),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 5,),
                Text("加入日期:"+widget.goods.addTime.substring(0,10),style: TextStyle(fontSize: 12,color: Colors.black54),),
                SizedBox(height: 5,),
                SizedBox(height: 5,),
                Row(children: <Widget>[
                  ButtonLabel("前去支付"),
                  SizedBox(width: 10,),
                  ButtonLabel("删除"),
                ],)
              ],
            ),
          ),
        ],
      ),
    );
  }


}
