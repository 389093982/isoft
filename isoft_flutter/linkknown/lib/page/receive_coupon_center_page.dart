import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/paginator.dart' as common_paginator;
import 'package:linkknown/model/query_coupon_center_list_response.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/receive_coupon_center_item.dart';

class ReceiveCouponCenterPage extends StatefulWidget {
  @override
  _ReceiveCouponCenterPageState createState() =>
      _ReceiveCouponCenterPageState();
}

class _ReceiveCouponCenterPageState extends State<ReceiveCouponCenterPage>
    with TickerProviderStateMixin {
  List<Coupon> coupons = new List();
  ScrollController scrollController = ScrollController();

  common_paginator.Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    initData();

    scrollController.addListener(() {
      // 预留底部 loading 的高度 30
      if (scrollController.position.pixels ==
          scrollController.position.maxScrollExtent) {
        if (paginator != null && paginator.currpage < paginator.totalpages) {
          loadPageData(current_page + 1, 10, delayed: true);
        }
      }
    });
  }

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  void loadPageData(int _current_page, int offset,
      {bool delayed = false, bool resetLoadingStatus = false}) {
    if (resetLoadingStatus) {
      loadingStatus = "";
    }
    if (loadingStatus == LoadingStatus.LOADING) {
      return;
    }
    setState(() {
      loadingStatus = LoadingStatus.LOADING;
    });
    current_page = _current_page;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.QueryCouponCenterList(current_page, offset)
          .then((QueryCouponCenterListResponse) {
        if (QueryCouponCenterListResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            coupons.clear();
          }
          coupons.addAll(QueryCouponCenterListResponse.coupons);
          paginator = QueryCouponCenterListResponse.paginator;

          setState(() {
            if (paginator.totalcount == 0) {
              loadingStatus = LoadingStatus.LOADED_EMPTY;
            } else {
              loadingStatus = paginator.currpage < paginator.totalpages
                  ? LoadingStatus.LOADED_COMPLETED
                  : LoadingStatus.LOADED_COMPLETED_ALL;
            }
          });
        } else {
          setState(() {
            loadingStatus = LoadingStatus.LOADED_FAILED;
          });
        }
      }).catchError((e) {
        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("领券中心"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: ScrollConfiguration(
        behavior: NoShadowScrollBehavior(),
        child: RefreshIndicator(
          //指示器颜色
          color: Theme.of(context).primaryColor,
          //指示器显示时距顶部位置
          displacement: 40,
          child: CustomScrollView(
            controller: scrollController,
            slivers: <Widget>[
              SliverList(
                  delegate: SliverChildBuilderDelegate(
                      (BuildContext context, int position) {
                return ReceiveCouponCenterItemWidget(coupons[position],
                    callback: () {
                  initData();
                });
              }, childCount: coupons.length)),
              SliverToBoxAdapter(
                child: FooterLoadingWidget(
                  loadingStatus: loadingStatus,
                  refreshOnFailCallBack: (status) {
                    if (status == LoadingStatus.LOADED_EMPTY) {
                      initData();
                    }
                  },
                ),
              ),
            ],
          ),
          onRefresh: _onRefresh,
        ),
      ),
    );
  }
}
