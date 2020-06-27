import 'package:flutter/material.dart';
import 'package:linkknown/page/classify_page.dart';
import 'package:linkknown/page/find_page.dart';
import 'package:linkknown/page/mine_page.dart';

import 'home_page.dart';

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> with TickerProviderStateMixin {
  @override
  Widget build(BuildContext context) {
    return new BottomNavigationWidget();
  }

}


class BottomNavigationWidget extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new BottomNavigationWidgetState();
  }
}

class BottomNavigationWidgetState extends State<BottomNavigationWidget> {
  int _currentIndex = 0;
  List<Widget> pages = new List();

  @override
  void initState() {
    pages
      ..add(HomePage())
      ..add(ClassifyPage())
      ..add(FindPage())
      ..add(MinePage());
  }

  @override
  Widget build(BuildContext context) {
    /*
    返回一个脚手架，里面包含两个属性，一个是底部导航栏，另一个就是主体内容
     */
    return new Scaffold(
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,  // 解决子项超过 3 个后，不显示颜色
        //底部导航栏的创建需要对应的功能标签作为子项，每个子项包含一个图标和一个title。
        items: [
          BottomNavigationBarItem(
              icon: Icon(
                Icons.home,
              ),
              title: new Text(
                '首页',
              )),
          BottomNavigationBarItem(
              icon: Icon(
                Icons.business,
              ),
              title: new Text(
                '分类',
              )),
          BottomNavigationBarItem(
              icon: Icon(
                Icons.find_in_page,
              ),
              title: new Text(
                '发现',
              )),
          BottomNavigationBarItem(
              icon: Icon(
                Icons.mood,
              ),
              title: new Text(
                '我的',
              )),
        ],
        //这是底部导航栏自带的位标属性，表示底部导航栏当前处于哪个导航标签。给他一个初始值0，也就是默认第一个标签页面。
        currentIndex: _currentIndex,
        //这是点击属性，会执行带有一个int值的回调函数，这个int值是系统自动返回的你点击的那个标签的位标
        onTap: (int i) {
          //进行状态更新，将系统返回的你点击的标签位标赋予当前位标属性，告诉系统当前要显示的导航标签被用户改变了。
          setState(() {
            _currentIndex = i;
          });
        },
      ),
      body: PageView.builder(
          onPageChanged: (index){
            setState(() {
              _currentIndex=index;
            });
          },
          itemCount: pages.length,
          itemBuilder: (BuildContext context,int index){
            return pages[index];
          }
      ),
    );
  }
}
