import 'package:flutter/material.dart';
import 'package:linkknown/utils/utils.dart';

class FindPage extends StatefulWidget {
  @override
  _FindPageState createState() => _FindPageState();
}

class _FindPageState extends State<FindPage> with TickerProviderStateMixin {
  List<String> getDataList() {
    List<String> list = [];
    for (int i = 0; i < 15; i++) {
      list.add(i.toString());
    }
    return list;
  }

  List<Widget> getWidgetList() {
    return getDataList().map((item) => getItemContainer(item)).toList();
  }

  Widget getItemContainer(String item) {
    return new InkWell(
      //点击事件回调
      onTap: () {
        UIUtils.showToast("点击了文字");
      },
      child: Container(
        alignment: Alignment.center,
        child: Text(
          item,
          style: TextStyle(color: Colors.white, fontSize: 20),
        ),
        color: Colors.blue,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Flutter去掉AppBar避免body溢出到状态栏
      // 没有AppBar的Flutter，如果不在Scaffold中使用AppBar会发现默认是沉浸式，预留出状态栏的高度方法
      appBar: PreferredSize(
        preferredSize:
            Size.fromHeight(MediaQuery.of(context).size.height * 0.07),
        child: SafeArea(
          top: true,
          child: Offstage(),
        ),
      ),
      backgroundColor: Colors.white,
      body: Container(
        // infinity 无穷大
        height: double.infinity,
        width: double.infinity,
        child: GridView.count(
          //水平子 Widget 之间间距
          crossAxisSpacing: 20.0,
          //垂直子 Widget 之间间距
          mainAxisSpacing: 20.0,
          //GridView内边距
          padding: EdgeInsets.all(10.0),
          //一行的Widget数量
          crossAxisCount: 4,
          //子Widget宽高比例
          childAspectRatio: 1.0,
          //子Widget列表
          children: getWidgetList(),
        ),
      ),
    );
    // GridView.count 创建网格布局
  }
}
