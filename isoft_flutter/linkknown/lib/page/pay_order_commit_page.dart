import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/model/query_coupon_by_id_response.dart';
import 'package:linkknown/model/search_coupon_for_pay_response.dart';
import 'package:linkknown/provider/select_available_coupon_notifer.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/function_button_label.dart';
import 'package:linkknown/widgets/goods_item.dart';
import 'package:provider/provider.dart';

class PayOrderCommitPage extends StatefulWidget {
  String goodsType;
  String goodsId;
  String goodsImg;
  String goodsDesc;
  String price;

  PayOrderCommitPage(this.goodsType,this.goodsId,this.goodsImg,this.goodsDesc,this.price);

  @override
  _PayOrderCommitPageState createState() => _PayOrderCommitPageState();
}

class _PayOrderCommitPageState extends State<PayOrderCommitPage> {

  List<Coupon> couponsForPay = new List();
  SelectedCoupon selectedCoupon = new SelectedCoupon();

  @override
  void initState() {
    super.initState();
    initData();
  }

  initData(){
    SearchCouponForPay();
  }

  //查询可用优惠券
  SearchCouponForPay() async {
    String userName = await LoginUtil.getLoginUserName();
    String target_type = "course";
    String target_id = widget.goodsId;
    String paid_amount = widget.price;
    String today = DateUtil.getToday_YYYYMMDD();
    LinkKnownApi.SearchCouponForPay(userName, target_type, target_id, paid_amount,today).then((SearchCouponForPayResponse) async {
      if (SearchCouponForPayResponse?.status == "SUCCESS") {
        couponsForPay.clear();
        couponsForPay.addAll(SearchCouponForPayResponse.coupons);
        setState(() {
          //刷新界面
        });
      } else {
        UIUtils.showToast(SearchCouponForPayResponse.errorMsg);
      }
    }).catchError((err) {
      //UIUtils.showToast("查询可用优惠券失败..");
    });
  }


  //查询已选择的优惠券
  queryCouponById(String couponId){
    LinkKnownApi.queryCouponById(couponId).then((QueryCouponByIdResponse) async {
      if (QueryCouponByIdResponse?.status == "SUCCESS") {
        selectedCoupon = QueryCouponByIdResponse.coupon;
        setState(() {
          //刷新
        });
      } else {
        UIUtils.showToast(QueryCouponByIdResponse.errorMsg);
      }
    }).catchError((err) {});
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("提交订单"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: Container(
//        alignment: Alignment.topRight,
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                Container(child: Text("支付方式:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text("微信支付"),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品描述:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text(widget.goodsDesc),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(
                  child: Image.network(
                    UIUtils.replaceMediaUrl(widget.goodsImg),
                    height: 160,
                    width: 280,
                    fit: BoxFit.fill,
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品价格:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text(Constants.RMB+widget.price,style: TextStyle(fontSize: 20,color: Colors.red),),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            GestureDetector(
              onTap: (){
                toSelectAvailableCouponPage();
              },
              child: Row(
                children: <Widget>[
                  Container(
                    child: Text("优惠券:"),
                  ),
                  Expanded(
                    child: Container(
                      alignment: Alignment.topRight,
                      child: Consumer(
                        builder: (BuildContext context, SelectAvailableCouponNotifer selectAvailableCouponNotifer, Widget child) {
                          if(selectAvailableCouponNotifer.hasChanged){
                            queryCouponById(selectAvailableCouponNotifer.couponId);
                            selectAvailableCouponNotifer.hasChanged = false;
                          }
                          return this.selectedCoupon.couponId==null?
                          Text(couponsForPay.length==0?"无可用":"有优惠券可以使用",style: TextStyle(fontSize: 14),)
                          :
                          Text(
                            selectedCoupon.youhuiType=="discount"?(double.parse(selectedCoupon.discountRate) * 10).toStringAsFixed(1)+"折" : "- "+Constants.RMB+selectedCoupon.couponAmount,
                            style: TextStyle(color: Colors.red,fontSize: 18),
                          );
                        },
                      ),
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 20,),
            Divider(color: Colors.black45, height: 2,),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("支付金额:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: selectedCoupon.couponId!=null?Text(
                      Constants.RMB+
                          (this.selectedCoupon.youhuiType=="discount"?
                          (double.parse(widget.price) * double.parse(selectedCoupon.discountRate)).toStringAsFixed(2)
                            :
                          (double.parse(widget.price) - double.parse(selectedCoupon.couponAmount)).toStringAsFixed(2)),
                      style: TextStyle(fontSize: 20,color: Colors.red),
                    )
                    :
                    Text(widget.price,style: TextStyle(fontSize: 20,color: Colors.red)),
                  ),
                ),
              ],
            ),
            SizedBox(height: 40,),
            Row(
              children: <Widget>[
                Expanded(
                  child: Container(
                    alignment: Alignment.topCenter,
                    child: FunctionButtonLabel(labelText: "                    确认支付                    ",
                      labelSize: 18,borderHeight: 24,bgColor: Colors.green,borderColor: Colors.green,),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }


  //选择可用优惠券
  toSelectAvailableCouponPage() async {
    String userName = await LoginUtil.getLoginUserName();
    String target_type = "course";
    String target_id = widget.goodsId;
    String paid_amount = widget.price;
    String today = DateUtil.getToday_YYYYMMDD();
    if(widget.goodsType=="course_theme_type"){
      NavigatorUtil.goRouterPage(context,
          "${Routes.toSelectAvailableCoupon}?userName=${userName}"
              + "&target_type=${target_type}"
              + "&target_id=${target_id}"
              + "&paid_amount=${paid_amount}"
              + "&today=${today}"
      );
    }else{
      UIUtils.showToast("非课程,待定..");
    }

  }

}
