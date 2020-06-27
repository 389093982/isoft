import 'package:flutter/material.dart';
import 'package:linkknown/widgets/common_tab.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with TickerProviderStateMixin {


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
    return CommonTabs(
      title: '基础组件',
      viewModels: viewModels,
      tabScrollable: false,
      tabController: this.tabController,
    );
  }

}
