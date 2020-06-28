import 'package:flutter/material.dart';

class CommonTabViewModel {
  final String title;
  final Widget widget;

  const CommonTabViewModel({
    this.title,
    this.widget,
  });
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

// Flutter中为了节约内存不会保存widget的状态,widget都是临时变量.当我们使用TabBar,TabBarView是我们就会发现,切换tab，initState又会被调用一次
// 怎么为了让tab一直保存在内存中,不被销毁?
// 添加AutomaticKeepAliveClientMixin,并设置为true,这样就能一直保持当前不被initState了
class _HomePageState extends State<HomePage> with TickerProviderStateMixin, AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<CommonTabViewModel> viewModels = [
    CommonTabViewModel(title: '免费', widget: Text("宠物卡片")),
    CommonTabViewModel(title: '付费', widget: Text("宠物卡片")),
    CommonTabViewModel(title: '全部', widget: Text("宠物卡片")),
    CommonTabViewModel(title: '推荐', widget: Text("宠物卡片")),
    CommonTabViewModel(title: '会员', widget: Text("宠物卡片")),
  ].map((item) => CommonTabViewModel(
    title: item.title,
    widget: Column(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[item.widget],
    ),
  )).toList();

  TabController tabController;

  @override
  void initState() {
    super.initState();
    this.tabController = new TabController(length: viewModels.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HomeHeaderWidget(),
          ),
          bottom: TabBar(
            controller: this.tabController,
            isScrollable: true,
            indicatorColor: Colors.white,
            indicatorSize: TabBarIndicatorSize.label,
            tabs: this.viewModels.map((item) => Tab(text: item.title)).toList(),
          ),
        ),
            preferredSize: Size.fromHeight(80.0),
      ),
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
    );
  }

}



class _HomeHeaderWidget extends StatefulWidget {
  @override
  _HomeHeaderWidgetState createState() => _HomeHeaderWidgetState();
}

class _HomeHeaderWidgetState extends State<_HomeHeaderWidget> with TickerProviderStateMixin {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Container(
          child: Text(
              "1111111111",
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
          ),
        ),
        Container(
          child: Text(
            "222222222",
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
        Container(
          child: Text(
            "3333",
            maxLines: 1,
            overflow: TextOverflow.ellipsis,),
        ),
      ],
    );
  }
}