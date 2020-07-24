

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/course_card.dart';

class CourseFilterWidget extends StatefulWidget {

  String search;
  String isCharge;

  CourseFilterWidget(this.search, this.isCharge);

  @override
  _CourseFilterState createState() => _CourseFilterState();

}

class _CourseFilterState extends State<CourseFilterWidget> with TickerProviderStateMixin {

  List<Course> courseList = new List();
  ScrollController scrollController = ScrollController();

  int page = 0;
  bool isLoading = false;//是否正在请求新数据
  bool showMore = false;//是否显示底部加载中提示

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
        print('滑动到了最底部${scrollController.position.pixels}');
        setState(() {
          showMore = true;
        });
        loadPageData(page + 1, 10);
      }
    });
  }

  void loadPageData (int current_page, int offset) {
    if (isLoading) {
      return;
    }
    isLoading = true;
    page = current_page;

    LinkKnownApi.searchCourseList(widget.search, widget.isCharge, current_page, offset).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        courseList.clear();
      }
      courseList.addAll(value.courses);

      setState(() {
        isLoading = false;
        showMore = false;
      });
    });

  }

  void initData() {
    loadPageData(1, 10);
  }

  Future < void > _onRefresh() async {
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
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 5),
            child: GridView.builder(
                physics: AlwaysScrollableScrollPhysics(),
                shrinkWrap: true,
                itemCount: courseList.length,
                controller: scrollController,
                // SliverGridDelegateWithFixedCrossAxisCount 构建一个横轴固定数量Widget
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    //横轴元素个数
                    crossAxisCount: 2,
                    //纵轴间距
                    mainAxisSpacing: 10.0,
                    //横轴间距
                    crossAxisSpacing: 10.0,
                    //子组件宽高长度比例
                    childAspectRatio: 1.0),
                itemBuilder: (BuildContext context, int index) {
                return CourseCardWidget(courseList[index]);
              }),
          ),
          onRefresh: _onRefresh,
        ),
      ],
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
    super.dispose();
    scrollController.dispose();
  }

}