import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/model/message.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/widgets/common_loading.dart';

class MessagePage extends StatefulWidget {
  @override
  _MessageState createState() => _MessageState();
}

class _MessageState extends State<MessagePage> {
  List<Message> messageList = new List();
  String nickName;

  dynamic loadingStatus;

  Paginator paginator;
  int page = 0;

  ScrollController scrollController = ScrollController();

  @override
  void initState() {
    super.initState();

    initData();
  }

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
  }

  void loadPageData(int current_page, int offset,
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
    page = current_page;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.queryPageMessageList(current_page, offset)
          .then((messageListResponse) async {
        nickName = await LoginUtil.getNickName();

        if (messageListResponse.status == "SUCCESS") {
          if (current_page == 1) {
            messageList.clear();
          }
          messageList.addAll(messageListResponse.messages);
          paginator = messageListResponse.paginator;

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
        // 弹出登录对话框
        AutoLoginDialogHelper.checkCanShowUnLoginDialog(context, err);

        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: new Text('我的消息'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: RefreshIndicator(
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
                padding: EdgeInsets.all(15),
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border(bottom: BorderSide(color: Colors.grey[300])),
                ),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Image.asset(
                      "images/linkknown.jpg",
                      width: 35,
                      height: 35,
                      fit: BoxFit.fill,
                    ),
                    Container(
                      width: 10,
                    ),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          RichText(
                            text: TextSpan(
                                style: DefaultTextStyle.of(context).style,
                                children: <InlineSpan>[
                                  TextSpan(
                                      text: '链知课堂：',
                                      style: TextStyle(
                                          color: Colors.blue, fontSize: 16)),
                                  TextSpan(
                                      text: '推送消息',
                                      style: TextStyle(fontSize: 16)),
                                ]),
                          ),
                          Text(
                            messageList[position].lastUpdatedTime,
                            style: TextStyle(color: Colors.black54),
                          ),
                          SizedBox(
                            height: 5,
                          ),
                          Text(
                            "@${nickName}:${messageList[position].messageText}",
                            overflow: TextOverflow.clip,
                            style: TextStyle(color: Colors.black54),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              );
            }, childCount: messageList.length)),
            SliverToBoxAdapter(
              child: FooterLoadingWidget(
                loadingStatus: loadingStatus,
                refreshOnFailCallBack: (status) {
                  _onRefresh();
                },
              ),
            ),
          ],
        ),
        onRefresh: _onRefresh,
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    scrollController.dispose();
  }
}
