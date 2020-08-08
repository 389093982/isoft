import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/my_coupon_response.dart';
import 'package:linkknown/response/user_link_agent_response.dart';
import 'package:linkknown/utils/login_util.dart';
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
    isLoading = true;
    page = current_page;

    LinkKnownApi.QueryTodayInviteMe(current_page, offset).then((value) {
      if (current_page == 1) {
        SomeBodyInviteMeList.clear();
      }
      SomeBodyInviteMeList.addAll(value.userLinkAgentList);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

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
                        //maxWidth: 300
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
                        contentPadding: EdgeInsets.only(left: 12,top:0),//光标距离左侧距离
                        hintText: '请输入受邀者账号..',
                        hintStyle: new TextStyle(fontSize: 14, color: Colors.grey[400]),
                        enabledBorder: OutlineInputBorder(//未点击输入框的效果
                          borderSide: BorderSide(
                            color: Colors.deepOrange, //边框颜色
                            width: 2, //宽度为2
                          ),
                          borderRadius: BorderRadius.circular(30),//四个角弧度
                        ),
                        focusedBorder: OutlineInputBorder(//点击输入框后的效果
                          borderSide: BorderSide(
                            color: Colors.deepOrange, //边框颜色
                            width: 2, //宽度为2
                          ),
                          borderRadius: BorderRadius.circular(30),//四个角弧度
                        ),
                        suffixIcon: InkWell(
                          onTap: () {
                            AddUserLinkAgent(searchInputController.text);
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
        DividerLineView(),//黑色的分割线
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


  AddUserLinkAgent(String userName) async {
    if(""==userName){
      UIUtils.showToast("请输入受邀者账号！");
      return;
    }
    String user_name = await LoginUtil.getUserName();
    if (userName == user_name){
      UIUtils.showToast("不能邀请自己^_^");
      return;
    }

    LinkKnownApi.AddUserLinkAgent(userName).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("邀请成功！请尽快通知对方接受邀请");
      }else{
        if(value.errorMsg.contains("今日已邀请")){
          UIUtils.showToast(value.errorMsg+"请尽快通知对方接受邀请");
        }else{
          UIUtils.showToast(value.errorMsg);

        }
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });

  }


  @override
  void dispose() {
    super.dispose();
    scrollController.dispose();
    searchInputController.dispose();
  }

}