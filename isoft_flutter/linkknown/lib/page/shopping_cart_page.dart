import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/page/shopping_cart_goods.dart';
import 'package:linkknown/widgets/home_drawer.dart';

import 'my_coupon.dart';

class TabViewModel {
  final Widget widget;

  const TabViewModel({
    this.widget,
  });
}

class ShoppingCartPage extends StatefulWidget {
  @override
  _ShoppingCartPage createState() => _ShoppingCartPage();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _ShoppingCartPage extends State<ShoppingCartPage> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<TabViewModel> viewModels = [
    TabViewModel(widget: ShoppingCartGoodsWidget()),
  ].map((item) => TabViewModel(
    widget: item.widget,
  )).toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController = new TabController(length: viewModels.length, vsync: this);
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
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
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
            transform: Matrix4.translationValues(0, 0, 0),
            child: Text("购物车"),
          ),
        ),
      ],
    );
  }
}