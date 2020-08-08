import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/course_history_response.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/get_user_info_by_names_response.dart';
import 'package:linkknown/response/query_page_blog_response.dart';
import 'package:linkknown/response/user_favorite_list_response.dart';
import 'package:linkknown/provider/cloud_blog_refresh_notifer.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/blog_item.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:provider/provider.dart';

class CloudBlogWidget extends StatefulWidget {
  String searchScop;
  String searchData;

  CloudBlogWidget(this.searchScop, this.searchData, {Key key})
      : super(key: key);

  @override
  CloudBlogState createState() => CloudBlogState();
}

class CloudBlogState extends State<CloudBlogWidget>
    with AutomaticKeepAliveClientMixin {
  @override
  bool get wantKeepAlive => true;

  List<Blog> blogs = new List();
  Set<User> users = new Set();

  //博客查询的条件
  String search_type = "";
  String search_data = "";
  String search_user_name = "";

  dynamic paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();
  }

  Future<void> loadPageData(int _current_page, int offset,
      {bool delayed = false, bool resetLoadingStatus = false}) async {
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

    String userName = await LoginUtil.getUserName();

    //查询博客
    if ("SCOPE_MYSELF" == widget.searchScop) {
      search_user_name = userName;
    } else if ("SCOPE_ALL" == widget.searchScop) {
      search_type = "_all";
    }
    //如果searchData传过来了值，那就是根据内容来查了，这里给内容赋值
    if (StringUtil.checkNotEmpty(widget.searchData)) {
      search_data = widget.searchData;
    }else{
      search_data = "";
    }

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.queryPageBlog(search_type, search_data, search_user_name, current_page, offset).then((value) {
        List<Blog> blogList = value.blogs;
        if (blogList.length > 0) {
          //1.给blogs 添加内容
          if (current_page == 1) {
            blogs.clear();
          }
          blogs.addAll(value.blogs);
          paginator = value.paginator;
          //2.获取userName字段
          String usernames = "";
          for (var blog in blogList) {
            usernames += blog.author + ",";
          }
          //3.去掉最后一个逗号
          usernames = usernames.substring(0, usernames.length - 1);
          //4.根据usernames查询用户信息
          LinkKnownApi.GetUserInfoByNames(usernames).then((value) {
            //将用户信息放入users 集合
            users.addAll(value.users);
            setState(() {
              if (paginator.totalcount == 0) {
                loadingStatus = LoadingStatus.LOADED_EMPTY;
              } else {
                loadingStatus = paginator.currpage < paginator.totalpages
                    ? LoadingStatus.LOADED_COMPLETED
                    : LoadingStatus.LOADED_COMPLETED_ALL;
              }
            });
          }).catchError((e) {
            setState(() {
              loadingStatus = LoadingStatus.LOADED_FAILED;
            });
          });
        }else{
          setState(() {
            loadingStatus = LoadingStatus.LOADED_EMPTY;
          });
          UIUtil.showToast("未匹配到相应数据..");
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

  Future<void> onRefresh({String searchData}) async {
    widget.searchData = searchData;
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
      child: Consumer(
        builder: (BuildContext context, CloudBlogRefreshNotifer cloudBlogRefreshNotifer, Widget child) {
          if(cloudBlogRefreshNotifer.hasChanged){
            Future.delayed(Duration(milliseconds: 200)).then((e) {
              initData();
            });
            cloudBlogRefreshNotifer.hasChanged = false;
          }
          return RefreshIndicator(
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
                  ListView.builder(
                      shrinkWrap: true,
                      physics: NeverScrollableScrollPhysics(),
                      //itemExtent:130,
                      itemCount: blogs.length,
                      //controller: scrollController,
                      itemBuilder: (BuildContext context, int index) {
                        return BlogItemWidget(
                            blogs[index],
                            users.firstWhere((element) =>
                            element.userName == blogs[index].author),
                            refreshCallback: (){
                              initData();
                            },
                        );
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
            onRefresh: onRefresh,
          );
        },
      )
    );
  }

  @override
  void dispose() {
    super.dispose();
  }
}
