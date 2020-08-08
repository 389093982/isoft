import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/response/query_general_coupon_targets_response.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/coupon_goods_item.dart';
import 'package:linkknown/widgets/goods_item.dart';

class CouponGoodsPage extends StatefulWidget {
  String youhui_type;
  String goods_min_amount;

  CouponGoodsPage(this.youhui_type,this.goods_min_amount);

  @override
  _CouponGoodsPageState createState() => _CouponGoodsPageState();
}

class _CouponGoodsPageState extends State<CouponGoodsPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("可以购买的课程"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: CouponGoodsWidget(widget.youhui_type,widget.goods_min_amount),
    );
  }
}

class CouponGoodsWidget extends StatefulWidget {
  String youhui_type;
  String goods_min_amount;

  CouponGoodsWidget(this.youhui_type,this.goods_min_amount);

  @override
  _CouponGoodsWidgetState createState() => _CouponGoodsWidgetState();
}

class _CouponGoodsWidgetState extends State<CouponGoodsWidget> {
  List<Course> courseList = new List();
  ScrollController scrollController = ScrollController();

  Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

    scrollController.addListener(() {
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

  void loadPageData(int _currentpage, int offset,
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
    current_page = _currentpage;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.QueryGeneralCouponTargets(widget.youhui_type,widget.goods_min_amount,current_page, offset)
          .then((QueryGeneralCouponTargetsResponse) async {
        if (QueryGeneralCouponTargetsResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            courseList.clear();
          }
          courseList.addAll(QueryGeneralCouponTargetsResponse.courseList);

          paginator = QueryGeneralCouponTargetsResponse.paginator;
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
      }).catchError((err) {
        AutoLoginDialogHelper.checkCanShowUnLoginDialog(context, err);

        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return ScrollConfiguration(
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
                  delegate: SliverChildBuilderDelegate((BuildContext context, int position) {
                        return Container(
                          padding: EdgeInsets.symmetric(horizontal: 5, vertical: 3),
                          child: CouponGoodsItemWidget(courseList[position],),
                        );
                      }, childCount: courseList.length)),
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
        ));
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }
}
