import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/pay_order_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/order_item.dart';

class TabViewModel {
  final String title;
  final Widget widget;

  const TabViewModel({
    this.title,
    this.widget,
  });
}

class PayOrderPage extends StatefulWidget {
  @override
  _PayOrderPage createState() => _PayOrderPage();
}

class _PayOrderPage extends State<PayOrderPage> with TickerProviderStateMixin {
  List<TabViewModel> viewModels = [
    TabViewModel(title: '全部', widget: PayOrderWidget("ALL")),
    TabViewModel(title: '待付款', widget: PayOrderWidget("WAIT_FOR_PAY")),
    TabViewModel(title: '已付款', widget: PayOrderWidget("PAID")),
    TabViewModel(title: '已取消', widget: PayOrderWidget("CANCELLED")),
    TabViewModel(title: '失败', widget: PayOrderWidget("FAIL")),
  ]
      .map((item) => TabViewModel(
            title: item.title,
            widget: item.widget,
          ))
      .toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController =
        new TabController(length: viewModels.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("我的订单"),
          bottom: TabBar(
            controller: this.tabController,
            isScrollable: true,
            indicatorColor: Colors.white,
            indicatorSize: TabBarIndicatorSize.label,
            tabs: this.viewModels.map((item) => Tab(text: item.title)).toList(),
          ),
        ),
        preferredSize: Size.fromHeight(80.0),
      ),
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
    );
  }
}

class PayOrderWidget extends StatefulWidget {
  String scope;

  PayOrderWidget(this.scope);

  @override
  _PayOrderState createState() => _PayOrderState();
}

class _PayOrderState extends State<PayOrderWidget>
    with AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  List<Order> orders = new List();
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
      {bool delayed = false, bool resetLoadingStatus = false}) async {
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

    String userName = await LoginUtil.getUserName();

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.queryPayOrderList(
              current_page, offset, userName, widget.scope)
          .then((payOrderResponse) async {
        if (payOrderResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            orders.clear();
          }
          orders.addAll(payOrderResponse.orders);

          paginator = payOrderResponse.paginator;
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
                  delegate: SliverChildBuilderDelegate(
                      (BuildContext context, int position) {
                return Container(
                  padding: EdgeInsets.symmetric(horizontal: 5, vertical: 3),
                  child: OrderItemWidget(
                      orders[position],
                      callback: (){
                        initData();
                      },
                  ),
                );
              }, childCount: orders.length)),
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
