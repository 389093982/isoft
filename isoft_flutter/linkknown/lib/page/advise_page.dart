import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_easyrefresh/material_footer.dart';
import 'package:flutter_easyrefresh/material_header.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/advise_list.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';

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
          header: MaterialHeader(),
          footer: MaterialFooter(),
          child: ListView.builder(
//            controller: scrollController,
            itemCount: adviseList.length, //列表长度+底部加载中提示
            itemBuilder: (BuildContext context, int position) {
              return Text(adviseList[position].advise);
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
