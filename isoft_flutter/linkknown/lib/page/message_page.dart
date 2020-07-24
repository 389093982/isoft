import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/message.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/widgets/divider_line.dart';

class MessagePage extends StatefulWidget {
  @override
  _MessageState createState() => _MessageState();
}

class _MessageState extends State<MessagePage> {
  List<Message> messageList = new List();
  String nickName;

  @override
  void initState() {
    super.initState();

    initMessageData();
  }

  initMessageData() async {
    MessageListResponse messageListResponse =
        await LinkKnownApi.queryPageMessageList(1, 10);
    nickName = await LoginUtil.getNickName();
    if (messageListResponse.status == "SUCCESS") {
      setState(() {
        messageList.addAll(messageListResponse.messages);
      });
    }
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
      body: ListView.builder(
//            controller: scrollController,
        itemCount: messageList.length, //列表长度+底部加载中提示
        itemBuilder: (BuildContext context, int position) {
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
                                  text: '推送消息', style: TextStyle(fontSize: 16)),
                            ]),
                      ),
                      Text(messageList[position].lastUpdatedTime, style: TextStyle(color: Colors.black54),),
                      SizedBox(height: 5,),
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
        },
        // 解决 item 太少不能下拉刷新的问题
        physics: AlwaysScrollableScrollPhysics(),
      ),
    );
  }
}
