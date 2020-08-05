import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/query_attention_or_fensi_response.dart';
import 'package:linkknown/widgets/attention_or_fensi_item.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/goods_item.dart';

class MyFensiPage extends StatefulWidget {
  @override
  _MyFensiPageState createState() => _MyFensiPageState();
}

class _MyFensiPageState extends State<MyFensiPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("我的粉丝"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: MyFensiWidget(),
    );
  }
}

class MyFensiWidget extends StatefulWidget {
  MyFensiWidget();

  @override
  _MyFensiState createState() => _MyFensiState();
}

class _MyFensiState extends State<MyFensiWidget> {
  List<AttentionOrFensi> fensiList = new List();
  ScrollController scrollController = ScrollController();

  Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

    scrollController.addListener(() {
      if (scrollController.position.pixels ==
          scrollController.position.maxScrollExtent) {
        if (paginator != null && paginator.currpage < paginator.totalpages) {
          loadPageData(current_page + 1, 10, delayed: true);
        }
      }
    });
  }

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  void loadPageData(int _currentpage, int offset,
      {bool delayed = false, bool resetLoadingStatus = false}) {
    if (resetLoadingStatus) {
      loadingStatus = "";
    }
    if (loadingStatus == LoadingStatus.LOADING) {
      return;
    }
    setState(() {
      loadingStatus = LoadingStatus.LOADING;
    });
    current_page = _currentpage;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.QueryAttentionOrFensi("user","Fensi",current_page, offset)
          .then((value) async {
        if (value?.status == "SUCCESS") {
          if (current_page == 1) {
            fensiList.clear();
          }
          fensiList.addAll(value.queryDatas);

          paginator = value.paginator;
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
        AutoLoginDialogHelper.checkCanShowUnLoginDialog(context, err);

        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return ScrollConfiguration(
        behavior: NoShadowScrollBehavior(),
        child: RefreshIndicator(
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
                          padding: EdgeInsets.symmetric(horizontal: 5, vertical: 3),
                          child: AttentionOrFensiItemWidget(fensiList[position],),
                        );
                      }, childCount: fensiList.length)),
              SliverToBoxAdapter(
                child: FooterLoadingWidget(
                  loadingStatus: loadingStatus,
                  refreshOnFailCallBack: (status) {
                    if (status == LoadingStatus.LOADED_EMPTY) {
                      initData();
                    }
                  },
                ),
              ),
            ],
          ),
          onRefresh: _onRefresh,
        ));
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }
}
