import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:linkknown/response/paginator.dart' as common_paginator;

class CourseFilterWidget extends StatefulWidget {
  String search;
  String isCharge;

  CourseFilterWidget(this.search, this.isCharge);

  @override
  _CourseFilterState createState() => _CourseFilterState();
}

class _CourseFilterState extends State<CourseFilterWidget>
    with TickerProviderStateMixin {
  List<Course> courseList = new List();
  ScrollController scrollController = ScrollController();

  common_paginator.Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  String _old_search;
  String _old_isCharge;

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

  void loadPageData(int _current_page, int offset, {bool delayed = false, bool resetLoadingStatus = false}) {
    if (resetLoadingStatus) {
      loadingStatus = "";
    }
    if (loadingStatus == LoadingStatus.LOADING) {
      return;
    }
    setState(() {
      loadingStatus = LoadingStatus.LOADING;
    });
    current_page = _current_page;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.searchCourseList(
              widget.search, widget.isCharge, current_page, offset)
          .then((courseMetaResponse) {
        if (courseMetaResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            courseList.clear();
          }
          courseList.addAll(courseMetaResponse.courses);
          paginator = courseMetaResponse.paginator;

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
      }).catchError((e) {
        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  Widget build(BuildContext context) {
    // 当搜索条件发生修改时重新刷新数据
    checkCanRefreshData();

    return Stack(
      children: <Widget>[
        RefreshIndicator(
          //指示器颜色
          color: Theme.of(context).primaryColor,
          //指示器显示时距顶部位置
          displacement: 40,
//          child: ListView.builder(
//            controller: scrollController,
//            itemCount: courseList.length,//列表长度+底部加载中提示
//            itemBuilder: (BuildContext context, int position) {
//              return Text(courseList[position].courseName);
//            },
//            // 解决 item 太少不能下拉刷新的问题
//            physics: AlwaysScrollableScrollPhysics(),
//          ),
          child: CustomScrollView(
            controller: scrollController,
            slivers: <Widget>[
              // 如果不是Sliver家族的Widget，需要使用SliverToBoxAdapter做层包裹
              SliverToBoxAdapter(
                child: getHeaderWidget(),
              ),
              SliverGrid(
                delegate: SliverChildBuilderDelegate(
                  (BuildContext context, int position) {
                    return CourseCardWidget(courseList[position]);
                  },
                  childCount: courseList.length,
                ),
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                  //横轴元素个数
                  crossAxisCount: 2,
                  //纵轴间距
                  mainAxisSpacing: 10.0,
                  //横轴间距
                  crossAxisSpacing: 10.0,
                  //子组件宽高长度比例
                  childAspectRatio: 0.9,
                ),
              ),
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
        ),
      ],
    );
  }

  Widget getHeaderWidget() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 5, vertical: 8),
      child: Row(
        children: <Widget>[
          Image.asset(
            "images/ic_hot_logo.png",
            width: 25,
            height: 25,
            fit: BoxFit.fill,
          ),
          SizedBox(
            width: 5,
          ),
          Text(
            "热门推荐",
            style: TextStyle(fontWeight: FontWeight.w200),
          ),
          Expanded(
            child: Text(""),
          ),
          CommonLabel.getCommonLabel4("精品课程"),
        ],
      ),
    );
  }

  void checkCanRefreshData() {
    // _old_search 不为空表示不是初次搜索
    if (_old_search != null) {
      // 有一项不同则表示搜索条件变更
      if (_old_search != widget.search || _old_isCharge != widget.isCharge) {
        // 需要重新加载数据
        initData();
      }
    }
    _old_search = widget.search;
    _old_isCharge = widget.isCharge;
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }
}
