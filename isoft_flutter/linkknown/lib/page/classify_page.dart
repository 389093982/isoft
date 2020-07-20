import 'dart:async';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/model/element.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class ClassifyPage extends StatefulWidget {
  @override
  _ClassifyPageState createState() => _ClassifyPageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _ClassifyPageState extends State<ClassifyPage>
    with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // 禁用返回，也可以使用    leading: Text(''),
        automaticallyImplyLeading: false,
        title: Text("链知课堂"),
        centerTitle: true,
        actions: <Widget>[
          IconButton(
              icon: Icon(
                Icons.search,
                color: Colors.white,
              ),
              onPressed: () {
                NavigatorUtil.goRouterPage(
                    context, "${Routes.courseSearch}?search=&isCharge=");
              }),
        ],
      ),
      body: Container(
        margin: EdgeInsets.only(top: 10),
        child: ClassifyWidget(),
      ),
    );
  }
}

class ClassifyWidget extends StatefulWidget {
  @override
  _ClassifyState createState() => _ClassifyState();
}

class _ClassifyState extends State<ClassifyWidget> {
  // GlobalKey 可用于跨组件通信,此处用于父组件通知子组件
  final GlobalKey<_LeftClassifyState> leftClassifyStateKey = GlobalKey();

  ElementResponse elementResponse;

  // 全部分类
  List<ElementItem> allClassifys = [];

  // 一级分类
  List<ElementItem> levelOneClassifys = [];

  @override
  void initState() {
    super.initState();

    initData();
  }

  initData() async {
    String placementName = "placement_host_course_type_carousel";
    ElementResponse elementResponse =
        await LinkKnownApi.filterElementByPlacement(placementName);
    setState(() {
      // 全部元素
      this.elementResponse = elementResponse;
      this.allClassifys = elementResponse.elements;
      // 一级元素
      elementResponse.elements.forEach((element) {
        if (element.navigationLevel == 0) {
          levelOneClassifys.add(element);
        }
      });

      List<ElementItem> getLevelTwoClassifys(int levelOneId) {
        List<ElementItem> levelTwoClassifys = [];
        allClassifys.forEach((element) {
          if (element.navigationParentId == levelOneId) {
            levelTwoClassifys.add(element);
          }
        });
        return levelTwoClassifys;
      }

      levelOneClassifys.sort((e1, e2) {
        return getLevelTwoClassifys(e2.id).length -
            getLevelTwoClassifys(e1.id).length;
      });
    });

    // 延迟设置选中第一项,主要是为了等待右侧组件绘制完成
    Future.delayed(Duration(milliseconds: 500)).then((value) {
      // 网络请求成功后，调用子组件方法，设置默认选中第一项
      leftClassifyStateKey.currentState.setSelectIndex(0);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(right: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Expanded(
            flex: 2,
            child: LeftClassifyWidget(
              levelOneClassifys,
              key: leftClassifyStateKey,
            ),
          ),
          Expanded(
            flex: 8,
            child: RightClassifyWidget(allClassifys),
          ),
        ],
      ),
    );
  }
}

class LeftClassifyWidget extends StatefulWidget {
  List<ElementItem> levelOneClassifys = [];

  LeftClassifyWidget(this.levelOneClassifys, {Key key}) : super(key: key);

  @override
  _LeftClassifyState createState() => _LeftClassifyState();
}

class _LeftClassifyState extends State<LeftClassifyWidget> {
  // 默认选中第一项
  int _selectIndex = 0;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.builder(
          shrinkWrap: true,
          physics: BouncingScrollPhysics(),
          itemCount: widget.levelOneClassifys.length,
          itemBuilder: (BuildContext context, int index) {
            return getLevelOneClassifyItem(
                widget.levelOneClassifys[index], index);
          }),
    );
  }

  setSelectIndex(int index) {
    setState(() {
      eventBus.fire(ClassifyEvent(widget.levelOneClassifys[index].id));
      _selectIndex = index;
    });
  }

  Widget getLevelOneClassifyItem(ElementItem item, int index) {
    return Container(
      width: 100,
      height: 50,
      alignment: Alignment.centerLeft,
      child: GestureDetector(
        onTap: () {
          eventBus.fire(ClassifyEvent(item.id));
          setState(() {
            // 更新状态
            _selectIndex = index;
          });
        },
        child: index == _selectIndex
            ? Text(
                item.elementLabel,
                style: TextStyle(color: Colors.red),
              )
            : Text(item.elementLabel),
      ),
    );
  }
}

// 父widget用到子widget，第一次使用时，会执行子widget中声明的构造函数，然后执行其 State 构造函数
// 再次使用时，会执行子 widget中声明的构造函数，不会再执行 State 的构造函数
//  if (active) {
//    return ActiveGameTabs(title);
//  }
//  修改为可以解决子组件不刷新的问题
//  if (widget.active) {
//    return ActiveGameTabs(title);
//  }
// 目的是使得build子组件的时候，我们使用的是widget的active(每次重构的时候Widget构造方法会被调用)，而不是State 的构造函数(不会重复执行)
class RightClassifyWidget extends StatefulWidget {
  List<ElementItem> allClassifys;

  RightClassifyWidget(this.allClassifys);

  @override
  _RightClassifyState createState() => _RightClassifyState();
}

class _RightClassifyState extends State<RightClassifyWidget> {
  // 二级分类
  List<ElementItem> levelTwoClassifys = [];

  StreamSubscription subscription;

  @override
  void initState() {
    super.initState();

    subscription = eventBus.on<ClassifyEvent>().listen((event) {
      _updateView(event.levelOneId);
    });
  }

  _updateView(int levelOneId) {
    // 在使用 setState() 方法之前, 先判断一下 mounted 是否为真, 可解决异步函数 setState() 导致内存泄漏的错误
    if (mounted) {
      setState(() {
        levelTwoClassifys.clear();
        widget.allClassifys.forEach((element) {
          if (element.navigationParentId == levelOneId) {
            levelTwoClassifys.add(element);
          }
        });
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
        physics: BouncingScrollPhysics(),
        itemCount: levelTwoClassifys.length,
        // SliverGridDelegateWithFixedCrossAxisCount 构建一个横轴固定数量Widget
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            //横轴元素个数
            crossAxisCount: 3,
            //纵轴间距
            mainAxisSpacing: 10.0,
            //横轴间距
            crossAxisSpacing: 10.0,
            //子组件宽高长度比例
            childAspectRatio: 1.0),
        itemBuilder: (BuildContext context, int index) {
          return getWidget(levelTwoClassifys[index]);
        });
  }

  Widget getWidget(ElementItem item) {
    return GestureDetector(
      onTap: () {
        // 中文需要进行编码,使用时解码
        String searchText =
            FluroConvertUtil.fluroCnParamsEncode(item.elementLabel);
        NavigatorUtil.goRouterPage(
            context, '${Routes.courseSearch}?search=${searchText}&isCharge=');
      },
      child: Column(
        children: <Widget>[
          Image.network(UIUtils.replaceMediaUrl(item.imgPath)),
          Text(
            item.elementLabel,
            style: TextStyle(color: Colors.grey[800]),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();

    subscription?.cancel();
  }
}
