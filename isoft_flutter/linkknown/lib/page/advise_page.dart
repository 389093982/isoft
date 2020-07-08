import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/bezier_bounce_footer.dart';
import 'package:flutter_easyrefresh/bezier_circle_header.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_easyrefresh/material_footer.dart';
import 'package:flutter_easyrefresh/material_header.dart';
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
  int page = 0;
  bool isLoading = false; //是否正在请求新数据
  bool showMore = false; //是否显示底部加载中提示
  EasyRefreshController _easyRefreshController = EasyRefreshController();

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();
  }

  void initData() {
    loadPageData(1, 10);
  }

  void loadPageData(int current_page, int offset) {
    if (isLoading) {
      return;
    }
    setState(() {
      isLoading = true;
      page = current_page;
    });
    LinkKnownApi.queryPageAdvise(current_page, offset)
        .then((adviseListResponse) {
      if (adviseListResponse.status == "SUCCESS") {
        if (current_page == 1) {
          adviseList.clear();
        }
        adviseList.addAll(adviseListResponse.advises);
      }

      setState(() {
        isLoading = false;
        showMore = false;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('我要吐槽'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
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
          controller: _easyRefreshController,
          header: BezierCircleHeader(backgroundColor: Colors.deepOrange),
          footer: BezierBounceFooter(backgroundColor: Colors.deepOrange),
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
                    VEmptyView(20),
                    DividerLineView(),
                  ],
                ),
              );
            },
            // 解决 item 太少不能下拉刷新的问题
            physics: AlwaysScrollableScrollPhysics(),
          ),
          onRefresh: _onRefresh,
          onLoad: () async {
            page++;
            loadPageData(page, 10);
          },
        ),
      ),
    );
  }

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  void dispose() {
    super.dispose();
//    scrollController.dispose();
  }

}
