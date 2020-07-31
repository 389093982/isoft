import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_history_response.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/user_favorite_list_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/course_card.dart';

class UserCourseWidget extends StatefulWidget {
  String userName;
  String searchLable;
  UserCourseWidget(this.userName,this.searchLable);

  @override
  _UserCourseState createState() => _UserCourseState();

}

class _UserCourseState extends State<UserCourseWidget> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<Course> courses = new List();
  ScrollController scrollController = ScrollController();

  int page = 0;
  bool isLoading = false;//是否正在请求新数据
  bool showMore = false;//是否显示底部加载中提示

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

    scrollController.addListener(() {
      if (scrollController.position.pixels == scrollController.position.maxScrollExtent) {
        print('滑动到了最底部${scrollController.position.pixels}');
        setState(() {
          showMore = true;
        });
        loadPageData(page + 1, 10);
      }
    });
  }

  Future<void> loadPageData (int current_page, int offset) async {
    if (isLoading) {
      return;
    }
    isLoading = true;
    page = current_page;

    String userName = widget.userName;

    //发布的课程
    if("DISPLAY_TYPE_NEW"==widget.searchLable){
      LinkKnownApi.getCourseListByUserName(userName,current_page, offset).catchError((e) {
        UIUtils.showToast((e as LinkKnownError).errorMsg);
        setState(() {
          isLoading = false;
          showMore = false;
        });
      }).then((value) {
        if (current_page == 1) {
          courses.clear();
        }
        courses.addAll(value.courses);
        setState(() {
          isLoading = false;
          showMore = false;
        });
      });

    //收藏的课程
    }else if("DISPLAY_TYPE_FAVORITE"==widget.searchLable){
      LinkKnownApi.getUserFavoriteList(userName,"course_collect",current_page, offset).catchError((e) {
        UIUtils.showToast((e as LinkKnownError).errorMsg);
      }).then((value) {
        //查询到课程id集合
        if("SUCCESS"==value.status){
          List<Favorite> favorites = value.favorites;
          if(favorites.length>0){
            //开始拼ids
            String ids = "";
            for(int i = 0;i<favorites.length;i++){
              ids += (favorites[i].favoriteId).toString()+",";
            }
            //去除最后一个逗号
            ids = ids.substring(0,ids.length-1);

            //发送请求，根据ids批量查询课程
            LinkKnownApi.GetCourseListByIds(ids).catchError((e) {
              UIUtils.showToast((e as LinkKnownError).errorMsg);
              setState(() {
                isLoading = false;
                showMore = false;
              });
            }).then((value) {
              if (current_page == 1) {
                courses.clear();
              }
              courses.addAll(value.courses);
              setState(() {
                isLoading = false;
                showMore = false;
              });
            });

          }
        }else{
          UIUtils.showToast(value.errorMsg);
        }
      });

    //观看的课程
    }else if("DISPLAY_TYPE_VIEWED"==widget.searchLable){
      LinkKnownApi.showCourseHistory(widget.userName,current_page, offset).catchError((e) {
        UIUtils.showToast((e as LinkKnownError).errorMsg);
      }).then((value) {

        //查询到课程id集合
        if("SUCCESS"==value.status){
          List<History> historys = value.historys;
          if(historys.length>0){
            //开始拼ids
            String ids = "";
            for(int i = 0;i<historys.length;i++){
              ids += (historys[i].historyValue).toString()+",";
            }
            //去除最后一个逗号
            ids = ids.substring(0,ids.length-1);

            //发送请求，根据ids批量查询课程
            LinkKnownApi.GetCourseListByIds(ids).catchError((e) {
              UIUtils.showToast((e as LinkKnownError).errorMsg);
              setState(() {
                isLoading = false;
                showMore = false;
              });
            }).then((value) {
              if (current_page == 1) {
                courses.clear();
              }
              courses.addAll(value.courses);
              setState(() {
                isLoading = false;
                showMore = false;
              });
            });

          }
        }else{
          UIUtils.showToast(value.errorMsg);
        }
      });
    }

  }

  void initData() {
    loadPageData(1, 10);
  }

  Future < void > _onRefresh() async {
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
            child: GridView.builder(
                physics: AlwaysScrollableScrollPhysics(),
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
                    childAspectRatio: 1.0),
                itemBuilder: (BuildContext context, int index) {
                  return CourseCardWidget(courses[index]);
                }),
          ),
          onRefresh: _onRefresh,
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