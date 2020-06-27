

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class CommonTabViewModel {
  final String title;
  final Widget widget;

  const CommonTabViewModel({
    this.title,
    this.widget,
  });
}

class CommonTabs extends StatelessWidget {
  final String title;
  final List<CommonTabViewModel> viewModels;
  final bool tabScrollable;
  final TabController tabController;

  CommonTabs({
    this.title,
    this.viewModels,
    this.tabScrollable = true,
    this.tabController,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(this.title),
        bottom: TabBar(
          controller: this.tabController,
          isScrollable: this.tabScrollable,
          tabs: this.viewModels.map((item) => Tab(text: item.title)).toList(),
        ),
      ),
      body: TabBarView(
        controller: this.tabController,
        children: this.viewModels.map((item) => item.widget).toList(),
      ),
    );
  }
}
