import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/user_link_agent_response.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/coupon_item.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:linkknown/widgets/somebody_invite_me_item.dart';
import 'package:linkknown/widgets/user_link_agent_item.dart';

class InvitationWidget extends StatefulWidget {

  InvitationWidget();

  @override
  _InvitationState createState() => _InvitationState();

}

class _InvitationState extends State<InvitationWidget> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<UserLinkAgent> SomeBodyInviteMeList = new List();
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

  void loadPageData (int current_page, int offset) {
    if (isLoading) {
      return;
    }
    setState(() {
      isLoading = true;
      page = current_page;
    });
    LinkKnownApi.QueryTodayInviteMe(current_page, offset).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        SomeBodyInviteMeList.clear();
      }
      SomeBodyInviteMeList.addAll(value.userLinkAgentList);

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
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 5),
            child: ListView.builder(
                physics: AlwaysScrollableScrollPhysics(),
                itemExtent:95,
                itemCount: SomeBodyInviteMeList.length,
                controller: scrollController,
                itemBuilder: (BuildContext context, int index) {
                  return SomeBodyInviteMeItemWidget(SomeBodyInviteMeList[index]);
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