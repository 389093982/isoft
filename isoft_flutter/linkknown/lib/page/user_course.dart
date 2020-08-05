import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_history_response.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/user_favorite_list_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/course_card.dart';

class UserCourseWidget extends StatefulWidget {
  String userName;
  String searchLable;

  UserCourseWidget(this.userName, this.searchLable);

  @override
  _UserCourseState createState() => _UserCourseState();
}

class _UserCourseState extends State<UserCourseWidget>
    with AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  List<Course> courses = new List();

//  ScrollController scrollController = ScrollController();

  dynamic paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

//    scrollController.addListener(() {
//      if (scrollController.position.pixels ==
//          scrollController.position.maxScrollExtent) {
//        if (paginator != null && paginator.currpage < paginator.totalpages) {
//          loadPageData(current_page + 1, 10, delayed: true);
//        }
//      }
//    });
  }

  void loadPageData(int _current_page, int offset,
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
    current_page = _current_page;

    String userName = widget.userName;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      //发布的课程
      if ("DISPLAY_TYPE_NEW" == widget.searchLable) {
        loadPageDataForNew(userName, current_page, offset);

        //收藏的课程
      } else if ("DISPLAY_TYPE_FAVORITE" == widget.searchLable) {
        loadPageForFavorite(userName, current_page, offset);

        //观看的课程
      } else if ("DISPLAY_TYPE_VIEWED" == widget.searchLable) {
        loadPageDataForViewed(current_page, offset);
      }
    });
  }

  void loadPageDataForViewed(int current_page, int offset) {
    LinkKnownApi.ShowCourseHistory(widget.userName, current_page, offset)
        .then((courseHistoryResponse) async {
      if (courseHistoryResponse?.status == "SUCCESS") {
        if (current_page == 1) {
          courses.clear();
        }

        List<History> histories = courseHistoryResponse.historys;
        if (histories.length > 0) {
          //开始拼ids
          String ids = "";
          for (int i = 0; i < histories.length; i++) {
            ids += (histories[i].historyValue).toString() + ",";
          }
          //去除最后一个逗号
          ids = ids.substring(0, ids.length - 1);

          //发送请求，根据ids批量查询课程
          CourseMetaResponse courseMetaResponse =
              await LinkKnownApi.GetCourseListByIds(ids);
          courses.addAll(courseMetaResponse.courses);
          paginator = courseHistoryResponse.paginator;

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
      }
    }).catchError((e) {
      setState(() {
        loadingStatus = LoadingStatus.LOADED_FAILED;
      });
    });
  }

  void loadPageForFavorite(String userName, int current_page, int offset) {
    LinkKnownApi.GetUserFavoriteList(
            userName, "course_collect", current_page, offset)
        .then((userFavoriteListResponse) async {
      if (userFavoriteListResponse?.status == "SUCCESS") {
        if (current_page == 1) {
          courses.clear();
        }

        List<Favorite> favorites = userFavoriteListResponse.favorites;
        if (favorites.length > 0) {
          //开始拼ids
          String ids = "";
          for (int i = 0; i < favorites.length; i++) {
            ids += (favorites[i].favoriteId).toString() + ",";
          }
          //去除最后一个逗号
          ids = ids.substring(0, ids.length - 1);

          //发送请求，根据ids批量查询课程
          CourseMetaResponse courseMetaResponse =
              await LinkKnownApi.GetCourseListByIds(ids);
          courses.addAll(courseMetaResponse.courses);
          paginator = userFavoriteListResponse.paginator;

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
      }
    }).catchError((e) {
      setState(() {
        loadingStatus = LoadingStatus.LOADED_FAILED;
      });
    });
  }

  void loadPageDataForNew(String userName, int current_page, int offset) {
    LinkKnownApi.GetCourseListByUserName(userName, current_page, offset)
        .then((courseMetaResponse) {
      if (courseMetaResponse?.status == "SUCCESS") {
        if (current_page == 1) {
          courses.clear();
        }
        courses.addAll(courseMetaResponse.courses);
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
  }

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  @override
  Widget build(BuildContext context) {
    return NotificationListener(
      onNotification: (notification) {
        if (notification is ScrollUpdateNotification &&
            notification.depth == 0) {
          if (notification.metrics.pixels ==
              notification.metrics.maxScrollExtent) {
            if (paginator != null &&
                paginator.currpage < paginator.totalpages) {
              loadPageData(current_page + 1, 10, delayed: true);
            }
          }
        }
        // 返回 true 取消冒泡
        return true;
      },
      child: RefreshIndicator(
        //指示器颜色
        color: Theme.of(context).primaryColor,
        //指示器显示时距顶部位置
        displacement: 40,
        child: Container(
          padding: EdgeInsets.symmetric(horizontal: 5),
          child: ListView(
            shrinkWrap: true,
            physics: NeverScrollableScrollPhysics(),
            children: <Widget>[
              GridView.builder(
                  physics: NeverScrollableScrollPhysics(),
                  shrinkWrap: true,
                  itemCount: courses.length,
                  //controller: scrollController,//注释掉就可以达到个人中心协调效果了
                  // SliverGridDelegateWithFixedCrossAxisCount 构建一个横轴固定数量Widget
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      //横轴元素个数
                      crossAxisCount: 2,
                      //纵轴间距
                      mainAxisSpacing: 10.0,
                      //横轴间距
                      crossAxisSpacing: 10.0,
                      //子组件宽高长度比例
                      childAspectRatio: 0.88),
                  itemBuilder: (BuildContext context, int index) {
                    return CourseCardWidget(courses[index]);
                  }),
              FooterLoadingWidget(
                loadingStatus: loadingStatus,
                refreshOnFailCallBack: (status) {
                  if (status == LoadingStatus.LOADED_EMPTY) {
                    initData();
                  }
                },
              )
            ],
          ),
        ),
        onRefresh: _onRefresh,
      ),
    );
  }

  @override
  void dispose() {
//    scrollController?.dispose();
    super.dispose();
  }
}
