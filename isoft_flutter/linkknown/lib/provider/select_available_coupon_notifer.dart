
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/login_user_response.dart';
import 'package:linkknown/model/query_page_blog_response.dart';
import 'package:linkknown/model/search_coupon_for_pay_response.dart';

class SelectAvailableCouponNotifer with ChangeNotifier {

  bool hasChanged = false;
  String couponId;

  update (bool hasChanged,String couponId) {
    this.hasChanged = hasChanged;
    this.couponId = couponId;
    notifyListeners();
  }

}