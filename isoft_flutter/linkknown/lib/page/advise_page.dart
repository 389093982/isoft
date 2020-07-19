import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/advise_list.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/divider_line.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class AdvisePage extends StatefulWidget {
  @override
  _AdvisePageState createState() => _AdvisePageState();
}

class _AdvisePageState extends State<AdvisePage> {
  List<Advise> adviseList = new List();
  bool isLoading = false; //是否正在请求新数据
  bool isFirstLoading = false; // 是否第一次请求
  Paginator paginator;
  EasyRefreshController _easyRefreshController = EasyRefreshController();

  @override
  void initState() {
    super.initState();

    _onRefresh();
  }

  Future<void> _onLoadMore() async {
    if (paginator != null && paginator.currpage < paginator.totalpages) {
      await Future.delayed(Duration(seconds: 2), () async {
        await loadPageData(paginator.currpage + 1, 10);
      });
    }
  }

  Future<void> _onRefresh() async {
    // 第一次请求 paginator 为空
    isFirstLoading = paginator == null;
    await Future.delayed(Duration(seconds: 2), () {
      loadPageData(1, 10);
    });
  }

  void loadPageData(int current_page, int offset) async {
    if (isLoading) {
      return;
    }
    isLoading = true;
    AdviseListResponse adviseListResponse =
        await LinkKnownApi.queryPageAdvise(current_page, offset);
    if (adviseListResponse.status == "SUCCESS") {
      setState(() {
        if (current_page == 1) {
          adviseList.clear();
        }
        adviseList.addAll(adviseListResponse.advises);

        paginator = adviseListResponse.paginator;
        // 结束加载显示没有更多数据
        _easyRefreshController.finishLoad(
            noMore: paginator.currpage == paginator.totalpages);
      });
    }
    isLoading = false;
    isFirstLoading = false;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('我要吐槽'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
        actions: <Widget>[
          IconButton(
            icon: SvgPicture.asset(
              "images/add.svg",
              color: Colors.white,
              width: 20,
              height: 20,
            ),
            onPressed: () {
              NavigatorUtil.goRouterPage(context, Routes.adviseEdit);
            },
          ),
        ],
      ),
      body: getBodyWidget(),
    );
  }

  Widget getBodyWidget() {
    if (isFirstLoading) {
      return getFirstLoadingWidget(context);
    } else if (paginator != null && paginator.totalcount == 0) {
      return getEmptyDataWidget();
    } else {
      return getListDataWidget();
    }
  }

  Widget getEmptyDataWidget () {
    return Center(
      child: Container(
        height: double.infinity,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Expanded(
              child: SizedBox(),
              flex: 2,
            ),
            SizedBox(
              width: 100.0,
              height: 100.0,
              child: Image.asset('images/linkknown.jpg'),
            ),
            Text(
              "没有数据",
              style: TextStyle(fontSize: 16.0, color: Colors.grey[400]),
            ),
            Expanded(
              child: SizedBox(),
              flex: 3,
            ),
          ],
        ),
      ),
    );
  }

  Widget getListDataWidget() {
    var length = adviseList?.length ?? 0;

    return RefreshIndicator(
        child: ScrollConfiguration(
          behavior: NoShadowScrollBehavior(),
          child: ListView.builder(
            itemCount: adviseList.length,
            itemBuilder: (BuildContext context, int position) {
              // 显示最后一条的时候加载下一页数据
              if (position == length - 1) {
                _onLoadMore();
              }
              return Container(
                padding: EdgeInsets.all(15),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text(adviseList[position].advise),
                    VEmptyView(50),
                    DividerLineView(),
                  ],
                ),
              );
            },
          ),
        ),
        onRefresh: _onRefresh);
  }

  Widget getFirstLoadingWidget(BuildContext context) {
    return Container(
      width: double.infinity,
      height: double.infinity,
      child: Center(
          child: SizedBox(
        height: 200.0,
        width: 300.0,
        child: Card(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Container(
                width: 50.0,
                height: 50.0,
                child: SpinKitFadingCube(
                  color: Theme.of(context).primaryColor,
                  size: 25.0,
                ),
              ),
              Container(
                child: Text("正在加载..."),
              )
            ],
          ),
        ),
      )),
    );
  }

  @override
  void dispose() {
    super.dispose();
//    scrollController.dispose();
  }

}
