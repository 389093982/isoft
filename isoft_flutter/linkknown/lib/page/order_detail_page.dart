import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/response/pay_order_response.dart';
import 'package:linkknown/response/query_coupon_by_id_response.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/coupon_item.dart';
import 'package:linkknown/widgets/selected_coupon_item.dart';

import '../constants.dart';

class OrderDetailPage extends StatefulWidget {
  String orderId;

  OrderDetailPage(this.orderId);

  @override
  _OrderDetailPageState createState() => _OrderDetailPageState();
}

class _OrderDetailPageState extends State<OrderDetailPage> {

  Order order;
  SelectedCoupon selectedCoupon;

  @override
  void initState() {
    super.initState();
    initData();
  }

  initData(){
    queryOrderByOrderId();
  }

  //根据订单号查询订单详情
  queryOrderByOrderId(){
    LinkKnownApi.queryOrderByOrderId(1,10,widget.orderId).then((PayOrderResponse) async {
      if (PayOrderResponse?.status == "SUCCESS" && PayOrderResponse.orders.length==1) {
        this.order = PayOrderResponse.orders.first??new Order();
        if(this.order.activityType=="coupon"){

          //如果订单含有优惠券，则做个查询
          LinkKnownApi.queryCouponById(this.order.activityTypeBindId).then((QueryCouponByIdResponse) async {
            if (QueryCouponByIdResponse?.status == "SUCCESS") {
              selectedCoupon = QueryCouponByIdResponse.coupon;
              setState(() {
                //刷新
              });
            } else {
              UIUtil.showToast(QueryCouponByIdResponse.errorMsg);
            }
          }).catchError((err) {});


        }else{
          setState(() {
            //刷新
          });
        }
      } else {
        UIUtil.showToast(PayOrderResponse.errorMsg);
      }
    }).catchError((err) {});
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("订单详情"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: this.order==null?Text(""):Container(
        padding: EdgeInsets.all(15),
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                Container(child: Text("订单编号:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(this.order.orderId),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("交易时间:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(DateUtil.format2StandardTime(this.order.transTime)),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品描述:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(this.order.goodsDesc),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("支付金额:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(Constants.RMB+this.order.paidAmount),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品原价:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(Constants.RMB+this.order.goodsOriginalPrice),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("参与活动:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(getActivityDesc()),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("支付结果:  "),),
                Container(
                  alignment: Alignment.topRight,
                  child: Text(getPayResultDesc()),
                ),
              ],
            ),
            SizedBox(height: 40,),
            this.selectedCoupon==null?Text(""):SelectedCouponItemWidget(this.selectedCoupon),
          ],
        ),
      ),
    );
  }


  //参与活动描述
  getActivityDesc(){
    if(this.order.activityType=="coupon"){
      return "优惠券";
    }else{
      return "未参与";
    }
  }

  //支付结果描述
  getPayResultDesc(){
    if(this.order.payResult=="SUCCESS"){
      return "支付成功";
    }else if(this.order.payResult=="FAIL"){
      return "交易失败";
    }
  }

}
