import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/goods_item.dart';

class ShoppingCartPage extends StatefulWidget {
  @override
  _ShoppingCartPageState createState() => _ShoppingCartPageState();
}

class _ShoppingCartPageState extends State<ShoppingCartPage> {
  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("购物车"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: ShoppingCartGoodsWidget(),
    );
  }
}

class ShoppingCartGoodsWidget extends StatefulWidget {
  ShoppingCartGoodsWidget();

  @override
  _ShoppingCartGoodsState createState() => _ShoppingCartGoodsState();
}

class _ShoppingCartGoodsState extends State<ShoppingCartGoodsWidget>
    with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  List<GoodsData> goodsList = new List();
  ScrollController scrollController = ScrollController();

  int page = 0;
  bool isLoading = false; //是否正在请求新数据
  bool showMore = false; //是否显示底部加载中提示

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

    scrollController.addListener(() {
      if (scrollController.position.pixels ==
          scrollController.position.maxScrollExtent) {
        print('滑动到了最底部${scrollController.position.pixels}');
        setState(() {
          showMore = true;
        });
        loadPageData(page + 1, 10);
      }
    });
  }

  void loadPageData(int current_page, int offset) {
    if (isLoading) {
      return;
    }
    isLoading = true;
    page = current_page;

    LinkKnownApi.queryPayShoppingCartList(current_page, offset).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        goodsList.clear();
      }
      goodsList.addAll(value.goodsData);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    });
  }

  void initData() {
    loadPageData(1, 10);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        RefreshIndicator(
          //指示器颜色
          color: Theme.of(context).primaryColor,
          //指示器显示时距顶部位置
          displacement: 40,
//          child: ListView.builder(
//            controller: scrollController,
//            itemCount: courseList.length,//列表长度+底部加载中提示
//            itemBuilder: (BuildContext context, int position) {
//              return Text(courseList[position].courseName);
//            },
//            // 解决 item 太少不能下拉刷新的问题
//            physics: AlwaysScrollableScrollPhysics(),
//          ),
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 5),
            child: ListView.builder(
                shrinkWrap: true,
                physics: AlwaysScrollableScrollPhysics(),
                itemCount: goodsList.length,
                controller: scrollController,
                itemBuilder: (BuildContext context, int index) {
                  return GoodsItemWidget(
                    goodsList[index],
                    callback: () {
                      initData();
                    },
                  );
                }),
          ),
          onRefresh: _onRefresh,
        ),
      ],
    );
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }
}
