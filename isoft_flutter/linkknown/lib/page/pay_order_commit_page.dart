import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/base_response.dart';
import 'package:linkknown/response/pay_shopping_cart_response.dart';
import 'package:linkknown/response/query_coupon_by_id_response.dart';
import 'package:linkknown/response/search_coupon_for_pay_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/ui_util.dart';
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
  SelectedCoupon selectedCoupon;

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
        UIUtil.showToast(SearchCouponForPayResponse.errorMsg);
      }
    }).catchError((err) {
      //UIUtil.showToast("查询可用优惠券失败..");
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
        UIUtil.showToast(QueryCouponByIdResponse.errorMsg);
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
                    UIUtil.replaceMediaUrl(widget.goodsImg),
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
                      child: this.selectedCoupon==null?
                        Text(couponsForPay.length==0?"无可用":"有优惠券可以使用",style: TextStyle(fontSize: 14),)
                            :
                        Text(
                          selectedCoupon.youhuiType=="discount"?(double.parse(selectedCoupon.discountRate) * 10).toStringAsFixed(1)+"折" : "- "+Constants.RMB+selectedCoupon.couponAmount,
                          style: TextStyle(color: Colors.red,fontSize: 18),
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
                    child: selectedCoupon!=null?Text(
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
                  child: GestureDetector(
                    onTap: (){
                      nowToPay();
                    },
                    child: Container(
                      alignment: Alignment.topCenter,
                      child: FunctionButtonLabel(labelText: "                    立即付款                    ",
                        labelSize: 18,borderHeight: 28,bgColor: Colors.green,borderColor: Colors.green,),
                    ),
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
    if(couponsForPay.length==0){
      UIUtil.showToast("无可用优惠券");
      return;
    }
    String userName = await LoginUtil.getLoginUserName();
    String target_type = "course";
    String target_id = widget.goodsId;
    String paid_amount = widget.price;
    String today = DateUtil.getToday_YYYYMMDD();
    if(widget.goodsType=="course_theme_type"){
      Map<String,dynamic> map = await NavigatorUtil.goRouterPage(context,
          "${Routes.toSelectAvailableCoupon}?userName=${userName}"
              + "&target_type=${target_type}"
              + "&target_id=${target_id}"
              + "&paid_amount=${paid_amount}"
              + "&today=${today}"
      );
      queryCouponById(map["couponId"]);
    }else{
      UIUtil.showToast("非课程,待定..");
    }
  }


  //立即付款
  //1.先查看课程是否已经买过
  //2.如果存在未付款的订单，提示"您有一笔订单未付款，请前去付款，或取消订单"
  //3.查看券是否可使用
  //4.支付
  nowToPay() async {
    bool checkHasLogin = await LoginUtil.checkHasLogin();
    if(!checkHasLogin){
      UIUtil.showToast("会话过期,请重新登录..");
      return;
    }
    //1.先查看课程是否已经买过
    String user_name = await LoginUtil.getLoginUserName();
    String goods_type = widget.goodsType;
    String goods_id = widget.goodsId;
    String pay_result = "SUCCESS";
    int current_page = 1;
    int pageSize = 10;
    LinkKnownApi.queryPayOrderListIsPaid(current_page, pageSize, goods_type, goods_id, user_name, pay_result).then((PayOrderResponse) async {
      if (PayOrderResponse.status == "SUCCESS") {
        if(PayOrderResponse.orders.length>0){
          UIUtil.showToast("该课程您已购买过，无需再次购买^_^");
          return;
        }else{


          //2.如果存在未付款的订单，提示"您有一笔订单未付款，请前去付款，或取消订单"
          String scope = "WAIT_FOR_PAY";
          LinkKnownApi.queryPayOrderList(current_page,pageSize,user_name,scope).then((PayOrderResponse) async {
            if (PayOrderResponse?.status == "SUCCESS") {
              if(PayOrderResponse.orders.length>0){
                UIUtil.showToast("您有待付款的订单，请先去处理！");
                return;
              }else{


                //3.查看券是否可使用
                if(selectedCoupon!=null){
                  String couponId = selectedCoupon.couponId;
                  LinkKnownApi.queryCouponById(couponId).then((QueryCouponByIdResponse) async {
                    if (QueryCouponByIdResponse?.status == "SUCCESS") {
                      if("used"==QueryCouponByIdResponse.coupon.couponState){
                        UIUtil.showToast("当前券已被使用，请重新选择！");
                        return;
                      }else{
                        //4.微信支付
                        weChatPay();
                      }
                    } else {
                      UIUtil.showToast(QueryCouponByIdResponse.errorMsg);
                    }
                  }).catchError((err) {
                    UIUtil.showToast("3.查看券是否可使用--查询失败");
                  });
                }else{
                  //微信支付
                  weChatPay();
                }


              }
            } else {
              UIUtil.showToast(PayOrderResponse.errorMsg);
            }
          }).catchError((err) {
            UIUtil.showToast("2.查看是否有未付款订单--查询失败");
          });


        }
      } else {
        UIUtil.showToast(PayOrderResponse.errorMsg);
      }
    }).catchError((err) {
      UIUtil.showToast("1.查看课程是否已经买过--查询失败");
    });


  }


  //微信支付
  //1.订单入库
  //2.更新优惠券状态为已使用
  //3.支付成功后更新订单状态
  //4.再次更新优惠券状态为已使用
  weChatPay() async {
    //1.添加订单入pay_order
    String order_id = "支付系统生成订单号" + DateUtil.getNow_yyyyMMddHHmmss();
    String user_name = await LoginUtil.getLoginUserName();
    String goods_type = widget.goodsType;
    String goods_id = widget.goodsId;
    String goods_desc = widget.goodsDesc;
    String paid_amount = widget.price;
    if(selectedCoupon!=null){
      if(selectedCoupon.youhuiType=="discount"){
        paid_amount = (double.parse(widget.price) * double.parse(selectedCoupon.discountRate)).toStringAsFixed(2);
      }else{
        paid_amount = (double.parse(widget.price) - double.parse(selectedCoupon.couponAmount)).toStringAsFixed(2);
      }
    }
    String goods_original_price = widget.price;
    String activity_type = "";
    String activity_type_bind_id = "";
    if (selectedCoupon!=null){
      activity_type = "coupon";
      activity_type_bind_id = selectedCoupon.couponId;
    }
    String goods_img = widget.goodsImg;
    String pay_result = "Test_SUCCESS";
    String code_url = "no_code_url";
    LinkKnownApi.addPayOrder(order_id, user_name, goods_type, goods_id, goods_desc, paid_amount,
         goods_original_price, activity_type, activity_type_bind_id, goods_img, pay_result, code_url).then((BaseResponse) async {
      if (BaseResponse?.status == "SUCCESS") {
        UIUtil.showToast("订单添加成功");


        //2.更新优惠券,这里是下单的时候需要更新一次。
        if(selectedCoupon!=null){
          LinkKnownApi.UpdateCouponIsUsed(user_name,selectedCoupon.couponId).then((BaseResponse) async {
            if (BaseResponse?.status == "SUCCESS") {
              UIUtil.showToast("下单更新券为已使用！");
            } else {
              UIUtil.showToast(BaseResponse.errorMsg);
            }
          }).catchError((err) {});
        }


        //3.调微信支付接口
        UIUtil.showToast("微信支付成功..");


        //4.再次更新优惠券状态为已使用
        //更新优惠券,这里是下单的时候需要更新一次。
        if(selectedCoupon!=null){
          LinkKnownApi.UpdateCouponIsUsed(user_name,selectedCoupon.couponId).then((BaseResponse) async {
            if (BaseResponse?.status == "SUCCESS") {
              UIUtil.showToast("下单更新券为已使用！");
            } else {
              UIUtil.showToast(BaseResponse.errorMsg);
            }
          }).catchError((err) {});
        }


      } else {
        UIUtil.showToast("添加订单失败!");
      }
    }).catchError((err) {
      UIUtil.showToast("添加订单失败");
    });

  }





}
