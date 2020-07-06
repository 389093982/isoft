import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/event/event_bus.dart';
import 'package:linkknown/model/element.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_search.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class ClassifyPage extends StatefulWidget {
  @override
  _ClassifyPageState createState() => _ClassifyPageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _ClassifyPageState extends State<ClassifyPage> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

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
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            Padding(
              padding: EdgeInsets.symmetric(vertical: 10, horizontal: 5),
              child: SearchInputWidget(
                handleSearch: (data){
                  UIUtils.showToast(data);
                },
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: Text("热门搜索", style: TextStyle(color: Colors.grey),),
                ),
                Container(
                  margin: EdgeInsets.only(right: 10),
                  child: Text("搜索历史", style: TextStyle(color: Colors.grey),),
                ),
              ],
            ),
            VEmptyView(10),
            ClassifyWidget(),
          ],
        ),
      ),
    );
  }

}


class ClassifyWidget extends StatefulWidget {

  @override
  _ClassifyState createState() => _ClassifyState();
}

class _ClassifyState extends State<ClassifyWidget> {

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
    ElementResponse elementResponse = await LinkKnownApi.filterElementByPlacement("placement_host_course_type_carousel");
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
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: <Widget>[
          Expanded(
            flex: 2,
            child: LeftClassifyWidget(levelOneClassifys),
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

  LeftClassifyWidget(this.levelOneClassifys);

  @override
  _LeftClassifyState createState() => _LeftClassifyState(levelOneClassifys);
}

class _LeftClassifyState extends State<LeftClassifyWidget> {
  // 默认选中第一项
  int _selectIndex = 0;

  List<ElementItem> levelOneClassifys = [];

  _LeftClassifyState(this.levelOneClassifys);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: ListView.builder(
          shrinkWrap: true,
          itemCount: levelOneClassifys.length,
          itemBuilder: (BuildContext context, int index) {
            return getLevelOneClassifyItem(levelOneClassifys[index], index);
          }),
    );
  }

  Widget getLevelOneClassifyItem (ElementItem item, int index) {
    return Container(
      width: 100,
      height: 50,
      alignment: Alignment.centerLeft,
      child: GestureDetector(
        onTap: (){
          eventBus.fire(ClassifyEvent(item.id));
          setState(() {
            // 更新状态
            _selectIndex = index;
          });
        },
        child: index == _selectIndex
        ? Text(item.elementLabel, style: TextStyle(color: Colors.red),)
        : Text(item.elementLabel),
      ),
    );
  }

}

class RightClassifyWidget extends StatefulWidget {

  List<ElementItem> allClassifys;

  RightClassifyWidget(this.allClassifys);

  @override
  _RightClassifyState createState() => _RightClassifyState(allClassifys);
}

class _RightClassifyState extends State<RightClassifyWidget> {
  // 全部分类
  List<ElementItem> allClassifys;
  // 二级分类
  List<ElementItem> levelTwoClassifys = [];

  _RightClassifyState(this.allClassifys);


  @override
  void initState() {
    super.initState();

    eventBus.on<ClassifyEvent>().listen((event) {
      _updateView(event.levelOneId);
    });
  }

  _updateView(int levelOneId) {
    setState(() {
      levelTwoClassifys.clear();
      allClassifys.forEach((element) {
        if (element.navigationParentId == levelOneId) {
          levelTwoClassifys.add(element);
        }
      });
    });
  }

  @override
  Widget build(BuildContext context) {

    UIUtils.showToast(this.allClassifys.length.toString());

    return ListView.builder(
        shrinkWrap: true,
        itemCount: levelTwoClassifys.length,
        itemBuilder: (BuildContext context, int index) {
          return Text(levelTwoClassifys[index].elementLabel);
        });
  }

}