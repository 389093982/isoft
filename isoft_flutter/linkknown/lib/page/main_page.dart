import 'package:flutter/material.dart';
import 'package:linkknown/page/classify_page.dart';
import 'package:linkknown/page/find_page.dart';
import 'package:linkknown/page/mine_page.dart';
import 'package:linkknown/widgets/exit_app.dart';

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
  PageController _controller;

  @override
  void initState() {
    pages
      ..add(HomePage())
      ..add(ClassifyPage())
      ..add(FindPage())
      ..add(MinePage());

    _controller = PageController(
      initialPage: 0,
      viewportFraction: 1,
      keepPage: true,
    );
  }

  @override
  void dispose() {
    super.dispose();
    _controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    /*
    返回一个脚手架，里面包含两个属性，一个是底部导航栏，另一个就是主体内容
     */
    return new Scaffold(
      bottomNavigationBar: _buildBottomNavigationBarWidget(),
      body: DoubleTapBackExitApp(child: _buildBodyWidget()),
    );
  }

  Widget _buildBottomNavigationBarWidget() {
    return BottomNavigationBar(
      // 解决子项超过 3 个后，不显示颜色
      type: BottomNavigationBarType.fixed,
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
              Icons.view_list,
            ),
            title: new Text(
              '分类',
            )),
        BottomNavigationBarItem(
            icon: Icon(
              Icons.explore,
            ),
            title: new Text(
              '发现',
            )),
        BottomNavigationBarItem(
            icon: Icon(
              Icons.person,
            ),
            title: new Text(
              '我的',
            )),
      ],

      backgroundColor: Colors.white,
      currentIndex: _currentIndex,
      // 点击事件
      onTap: (int index) {
        _controller.jumpToPage(index);
        setState(() {
          _currentIndex = index;
        });
      },
    );
  }

  Widget _buildBodyWidget() {
    return PageView.builder(
        controller: _controller,
        itemCount: pages.length,
        physics: BouncingScrollPhysics(),
        pageSnapping: true,
        onPageChanged: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
        itemBuilder: (BuildContext context, int index) {
          return pages[index];
        });
  }
}
