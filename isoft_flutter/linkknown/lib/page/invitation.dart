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
import 'package:linkknown/widgets/divider_line.dart';
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
  final searchInputController = TextEditingController();

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

    return Column(
      children: <Widget>[
        Container(
          height: 100,
          padding: EdgeInsets.all(10),
          child: Column(children: <Widget>[
            Row(children: <Widget>[
              Text("邀请他人:",),
            ],),
            SizedBox(height: 20,),
            Row(children: <Widget>[
              Expanded(
                child: Container(
                  alignment: Alignment.center,
                  child: ConstrainedBox(
                    constraints: BoxConstraints(
                        maxHeight: 35,
//                        maxWidth: 300
                    ),
                    child: TextField(
                      //最大行数
                      maxLines: 1,
                      //光标颜色
                      cursorColor: Colors.deepOrange,
                      //光标宽度
                      cursorWidth: 2.0,
                      //输入文本的样式
                      style: TextStyle(fontSize: 15.0),
                      controller: searchInputController,
                      decoration: new InputDecoration(
                        contentPadding: EdgeInsets.only(left: 12,top:0),
                        hintText: '请输入受邀者账号..',
                        enabledBorder: OutlineInputBorder(
                          borderSide: BorderSide(
                            color: Colors.deepOrange, //边框颜色
                            width: 2, //宽度为5
                          ),
                          borderRadius: BorderRadius.circular(30),//四个角弧度
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(
                            color: Colors.deepOrange, //边框颜色
                            width: 2, //宽度为5
                          ),
                          borderRadius: BorderRadius.circular(30),//四个角弧度
                        ),
                        suffixIcon: InkWell(
                          onTap: () {
//                            handleSearch(searchInputController.text);
                          },
                          child: Icon(
                            Icons.person_add,
                            color: Colors.blue,
                          ),
                        ),
//                        border: InputBorder.none
                      ),
                      // onChanged: onSearchTextChanged,
                    ),
                  )
                ),
              ),
            ],),
          ],),
        ),
        SizedBox(height: 20,),
        DividerLineView(),
        SizedBox(height: 20,),
        Container(
          margin: EdgeInsets.all(10),
          child: Row(children: <Widget>[
            Text("正在邀请我:")
          ],),
        ),
        SizedBox(height: 10,),
        Expanded(
          child: RefreshIndicator(
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