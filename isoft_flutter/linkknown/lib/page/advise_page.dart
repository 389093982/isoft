import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/bezier_bounce_footer.dart';
import 'package:flutter_easyrefresh/bezier_circle_header.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
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
  bool canShowEmptyFlag = false; // 是否可以显示空布局,只有等第一次网络请求结束后才可以显示
  Paginator paginator;
  EasyRefreshController _easyRefreshController = EasyRefreshController();

  @override
  void initState() {
    super.initState();
  }

  void loadPageData(int current_page, int offset) async {
    if (isLoading){
      return;
    }
    isLoading = true;
    AdviseListResponse adviseListResponse = await LinkKnownApi.queryPageAdvise(current_page, offset);
    if (adviseListResponse.status == "SUCCESS") {
      setState(() {
        if (current_page == 1) {
          adviseList.clear();
        }
        adviseList.addAll(adviseListResponse.advises);

        paginator = adviseListResponse.paginator;
        // 结束加载显示没有更多数据
        _easyRefreshController.finishLoad(noMore: paginator.currpage == paginator.totalpages);
      });
    }
    isLoading = false;
    canShowEmptyFlag = true;
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
      body: Center(
        child: EasyRefresh(
          firstRefresh: true,
          firstRefreshWidget: Container(
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
          ),
          controller: _easyRefreshController,
          header: BezierCircleHeader(),
          footer: BezierBounceFooter(),
          emptyWidget: canShowEmptyFlag && adviseList.length == 0
              ? Container(
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
          )
              : null,
          child: ListView.builder(
//            controller: scrollController,
            itemCount: adviseList.length, //列表长度+底部加载中提示
            itemBuilder: (BuildContext context, int position) {
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
            // 解决 item 太少不能下拉刷新的问题
            physics: AlwaysScrollableScrollPhysics(),
          ),
          onRefresh: _onRefresh,
          onLoad: _onLoadMore,
        ),
      ),
    );
  }

  Future<void> _onLoadMore() async {
    if (paginator != null && paginator.currpage < paginator.totalpages){
      await Future.delayed(Duration(seconds: 2), () async {
        await loadPageData(paginator.currpage + 1, 10);
      });
    }
  }

  Future<void> _onRefresh() async {
    await Future.delayed(Duration(seconds: 2), () {
      loadPageData(1, 10);
    });
  }

  @override
  void dispose() {
    super.dispose();
//    scrollController.dispose();
  }
}
