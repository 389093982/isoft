import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_history_response.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/get_user_info_by_names_response.dart';
import 'package:linkknown/model/query_page_blog_response.dart';
import 'package:linkknown/model/user_favorite_list_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/blog_item.dart';
import 'package:linkknown/widgets/course_card.dart';

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
  ScrollController scrollController = ScrollController();

  //博客查询的条件
  String search_type = "";
  String search_data = "";
  String search_user_name = "";

  int page = 0;
  bool isLoading = false; //是否正在请求新数据
  bool showMore = false; //是否显示底部加载中提示

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

  Future<void> loadPageData(int current_page, int offset) async {
    if (isLoading) {
      return;
    }
    isLoading = true;
    page = current_page;
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
    }

    LinkKnownApi.queryPageBlog(search_type, search_data, search_user_name, current_page, offset).catchError((e) {
      setState(() {
        isLoading = false;
        showMore = false;
      });
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      List<Blog> blogList = value.blogs;
      if (blogList.length > 0) {
        //1.给blogs 添加内容
        if (current_page == 1) {
          blogs.clear();
        }
        blogs.addAll(value.blogs);
        //2.获取userName字段
        String usernames = "";
        for (var blog in blogList) {
          usernames += blog.author + ",";
        }
        //3.去掉最后一个逗号
        usernames = usernames.substring(0, usernames.length - 1);
        //4.根据usernames查询用户信息
        LinkKnownApi.GetUserInfoByNames(usernames).catchError((e) {
          UIUtils.showToast((e as LinkKnownError).errorMsg);
          setState(() {
            isLoading = false;
            showMore = false;
          });
        }).then((value) {
          //将用户信息放入users 集合
          users.addAll(value.users);
          setState(() {
            isLoading = false;
            showMore = false;
          });
        });
      }else{
        setState(() {
          isLoading = false;
          showMore = false;
        });
        UIUtils.showToast("未匹配到相应数据..");
      }
    });
  }

  void initData() {
    loadPageData(1, 10);
  }

  Future<void> onRefresh({String searchData}) async {
    if(StringUtil.checkNotEmpty(searchData)){
      widget.searchData = searchData;
    }
    initData();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        RefreshIndicator(
          //指示器颜色
          color: Theme.of(context).primaryColor,
          //指示器显示时距顶部位置
          displacement: 40,
          child: Container(
            padding: EdgeInsets.symmetric(horizontal: 5),
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 5),
              child: ListView.builder(
                  physics: AlwaysScrollableScrollPhysics(),
                  //itemExtent:130,
                  itemCount: blogs.length,
                  //controller: scrollController,
                  itemBuilder: (BuildContext context, int index) {
                    return BlogItemWidget(
                        blogs[index],
                        users.firstWhere((element) =>
                            element.userName == blogs[index].author));
                  }),
            ),
          ),
          onRefresh: onRefresh,
        ),
      ],
    );
  }

  @override
  void dispose() {
    super.dispose();
    scrollController.dispose();
  }
}
