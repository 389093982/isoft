import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/pay_shopping_cart_response.dart';
import 'package:linkknown/response/search_coupon_for_pay_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/available_coupon_item.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/coupon_item.dart';
import 'package:linkknown/widgets/function_button_label.dart';
import 'package:linkknown/widgets/goods_item.dart';
import 'package:provider/provider.dart';

class ToSelectAvailableCouponPage extends StatefulWidget {
  String userName;
  String target_type;
  String target_id;
  String paid_amount;
  String today;

  ToSelectAvailableCouponPage(this.userName,this.target_type,this.target_id,this.paid_amount,this.today);

  @override
  _ToSelectAvailableCouponPageState createState() => _ToSelectAvailableCouponPageState();
}

class _ToSelectAvailableCouponPageState extends State<ToSelectAvailableCouponPage> {

  List<Coupon> availableCoupons = new List();
  ScrollController scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    initData();
  }

  initData(){
    SearchCouponForPay();
  }

  //查询可用优惠券
  SearchCouponForPay() {
    String userName = widget.userName;
    String target_type = widget.target_type;
    String target_id = widget.target_id;
    String paid_amount = widget.paid_amount;
    String today = widget.today;
    LinkKnownApi.SearchCouponForPay(userName, target_type, target_id, paid_amount,today).then((SearchCouponForPayResponse) async {
      if (SearchCouponForPayResponse?.status == "SUCCESS") {
        availableCoupons.clear();
        availableCoupons.addAll(SearchCouponForPayResponse.coupons);
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

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("可用优惠券"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: ListView.builder(
        physics: AlwaysScrollableScrollPhysics(),
        itemExtent:132,
        itemCount: availableCoupons.length,
        controller: scrollController,
        itemBuilder: (BuildContext context, int index) {
          return AvailableCouponsItemWidget(
            availableCoupons[index],
            changed: (couponId){
              NavigatorUtil.goBack(context,map:{"couponId":couponId});
            },
          );
        }
      )
    );
  }

}
