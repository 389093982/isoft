import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/pay_order_response.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/bought_course_item.dart';
import 'package:linkknown/widgets/coupon_item.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:linkknown/widgets/goods_item.dart';

class BoughtCourseWidget extends StatefulWidget {

  BoughtCourseWidget();

  @override
  _BoughtCourseState createState() => _BoughtCourseState();

}

class _BoughtCourseState extends State<BoughtCourseWidget> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin{

  @override
  bool get wantKeepAlive => true;

  List<Order> orders = new List();
  ScrollController scrollController = ScrollController();

  int page = 0;
  bool isLoading = false;//是否正在请求新数据
  bool showMore = false;//是否显示底部加载中提示

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

  Future<void> loadPageData (int current_page, int offset) async {
    if (isLoading) {
      return;
    }
    setState(() {
      isLoading = true;
      page = current_page;
    });
    String userName = await LoginUtil.getUserName();
    LinkKnownApi.queryPayOrderList(current_page, offset,userName,"PAID",goods_type:"course_theme_type").catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        orders.clear();
      }
      orders.addAll(value.orders);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    });

  }

  void initData() {
    loadPageData(1, 10);
  }

  Future < void > _onRefresh() async {
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
                itemExtent:130,
                itemCount: orders.length,
                controller: scrollController,
                itemBuilder: (BuildContext context, int index) {
                  return BoughtCourseItemWidget(orders[index]);
                }),
          ),
          onRefresh: _onRefresh,
        ),
      ],
    );
  }


  @override
  void dispose() {
    super.dispose();
    scrollController.dispose();
  }

}