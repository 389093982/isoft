import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/query_coupon_center_list_response.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/coupon_item.dart';
import 'package:linkknown/widgets/home_drawer.dart';
import 'package:linkknown/widgets/receive_coupon_center_item.dart';

import 'my_coupon.dart';

class ReceiveCouponCenterPage extends StatefulWidget {
  @override
  _ReceiveCouponCenterPageState createState() => _ReceiveCouponCenterPageState();
}

class _ReceiveCouponCenterPageState extends State<ReceiveCouponCenterPage> with TickerProviderStateMixin {

  List<Coupon> coupons = new List();
  ScrollController scrollController = ScrollController();

  int page = 0;
  bool isLoading = false;//是否正在请求新数据
  bool showMore = false;//是否显示底部加载中提示

  @override
  void initState() {
    super.initState();
    initData();
  }

  void initData() {
    loadPageData(1, 10);
  }

  void loadPageData (int current_page, int offset) {
    if (isLoading) {
      return;
    }
    isLoading = true;
    page = current_page;

    LinkKnownApi.QueryCouponCenterList(current_page, offset).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        coupons.clear();
      }
      coupons.addAll(value.coupons);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    });

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HeaderWidget(),
          ),
        ),
            preferredSize: Size.fromHeight(60.0),
      ),
      body:ListView.builder(
          physics: AlwaysScrollableScrollPhysics(),
          itemExtent:130,
          itemCount: coupons.length,
          controller: scrollController,
          itemBuilder: (BuildContext context, int index) {
            return ReceiveCouponCenterItemWidget(coupons[index]);
          }),
    );
  }

}



class _HeaderWidget extends StatefulWidget {
  @override
  _HeaderWidgetState createState() => _HeaderWidgetState();
}

class _HeaderWidgetState extends State<_HeaderWidget> with TickerProviderStateMixin {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Container(
          child: Transform(
            transform: Matrix4.translationValues(0, 3, 0),
            child: Text("领券中心"),
          ),
        ),
      ],
    );
  }
}