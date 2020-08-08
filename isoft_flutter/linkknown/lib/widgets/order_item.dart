import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/base_response.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/my_coupon_response.dart';
import 'package:linkknown/response/pay_order_response.dart';
import 'package:linkknown/response/pay_shopping_cart_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

import 'button_label.dart';

class OrderItemWidget extends StatefulWidget {
  Order order;
  VoidCallback callback;

  OrderItemWidget(this.order,{this.callback});

  @override
  _OrderItemState createState() => _OrderItemState();
}

class _OrderItemState extends State<OrderItemWidget>
    with TickerProviderStateMixin {
  _OrderItemState();

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Card(
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
                        UIUtil.replaceMediaUrl(widget.order.goodsImg),
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
                    Text(Constants.RMB+widget.order.paidAmount,style: TextStyle(color: Colors.red),),
                    SizedBox(height: 5,),
                    Text(getOrderDateCharacterTip(widget.order)+": "+widget.order.orderTime.substring(0,10),style: TextStyle(fontSize: 12,color: Colors.black54),),
                    SizedBox(height: 5,),
                    SizedBox(height: 5,),
                    Row(children: <Widget>[
                      Offstage(
                        offstage: !(""==widget.order.payResult),
                        child: GestureDetector(
                          onTap: (){
                            toPay();
                          },
                          child: ButtonLabel("前去支付"),
                        ),
                      ),
                      Offstage(
                        offstage: !(""==widget.order.payResult),
                        child: SizedBox(width: 10,),
                      ),
                      Offstage(
                        offstage: !(""==widget.order.payResult),
                        child: GestureDetector(
                          onTap: (){
                            toCancelOrder();
                          },
                          child: ButtonLabel("取消订单"),
                        ),
                      ),
                      Offstage(
                        offstage: !("SUCCESS"==widget.order.payResult || "FAIL"==widget.order.payResult),
                        child: GestureDetector(
                          onTap: (){
                            toOrderDetail();
                          },
                          child: ButtonLabel("订单详情"),
                        ),
                      ),
                    ],)
                  ],
                ),
              ),
            ],
          ),
        ),
        Align(
          alignment: Alignment.topRight,
          child: Padding(
            padding: EdgeInsets.all(12),
            child:Image.asset(
              getOrderPayResultStateIcon(widget.order),
              width: 40,
              height: 40,
            ),
          ),
        ),
      ],
    );
  }

  //根据订单状态获取日期提示
  String getOrderDateCharacterTip(Order order){
    if("SUCCESS"==order.payResult || "FAIL"==order.payResult){
      return "付款日期";
    }else if("CANCELLED"==order.payResult || ""==order.payResult){
      return "下单日期";
    }else{
      return "订单日期";
    }
  }

  String getOrderPayResultStateIcon(Order order) {
    if("SUCCESS"==order.payResult){
      return "images/ic_pay_result_success.png";
    }else if("FAIL"==order.payResult){
      return "images/ic_pay_result_fail.png";
    }else if(""==order.payResult){
      return "images/ic_pay_result_wait_for_pay.png";
    }else if("CANCELLED"==order.payResult){
      return "images/ic_pay_result_cancel.png";
    }else {
      return "images/linkknown.jpg";
    }

  }

  //前往支付页面
  toPay(){
    String _smallImage = FluroConvertUtil.fluroCnParamsEncode(widget.order.goodsImg);
    String _courseName = FluroConvertUtil.fluroCnParamsEncode(widget.order.goodsDesc);
    NavigatorUtil.goRouterPage(context,
        "${Routes.payOrderCommit}?goodsType=course_theme_type"
            + "&goodsId=${widget.order.goodsId}"
            + "&goodsImg=${_smallImage}"
            + "&goodsDesc=${_courseName}"
            + "&price=${widget.order.goodsOriginalPrice}"
    );
  }


  //取消订单
  toCancelOrder(){
    LinkKnownApi.OrderCancelledById(widget.order.orderId).then((BaseResponse) {
      if(BaseResponse.status=="SUCCESS"){
        UIUtil.showToast("取消成功");
        widget.callback();
      }else{
        UIUtil.showToast("取消失败");
      }
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
  }


  //订单详情
  toOrderDetail(){
    String _orderId = FluroConvertUtil.fluroCnParamsEncode(widget.order.orderId);
    NavigatorUtil.goRouterPage(context, "${Routes.orderDetail}?orderId=${_orderId}");
  }


}
