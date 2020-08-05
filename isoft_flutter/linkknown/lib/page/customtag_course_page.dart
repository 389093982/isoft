import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/customtag_course_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';

class CustomTagCoursePage extends StatefulWidget {
  String custom_tag;

  CustomTagCoursePage(this.custom_tag);

  @override
  _CustomTagCoursePageState createState() => _CustomTagCoursePageState();
}

class _CustomTagCoursePageState extends State<CustomTagCoursePage> {
  bool showGrid = true;

  List<CustomTagCourse> customTagCourses = new List();
  ScrollController scrollController = ScrollController();

  Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  @override
  void initState() {
    super.initState();

    // 发送网络请求加载数据
    initData();

    scrollController.addListener(() {
      if (scrollController.position.pixels ==
          scrollController.position.maxScrollExtent) {
        if (paginator != null && paginator.currpage < paginator.totalpages) {
          loadPageData(current_page + 1, 15, delayed: true);
        }
      }
    });
  }

  void initData() {
    loadPageData(1, 15, resetLoadingStatus: true);
  }

  Future<void> _onRefresh() async {
    initData();
  }

  void loadPageData(int _currentpage, int offset,
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
    current_page = _currentpage;

    // delayed 为 true 时延迟 2s 让底部动画显示
    Future.delayed(Duration(seconds: delayed ? 2 : 0), () {
      LinkKnownApi.queryCustomTagCourse(widget.custom_tag, current_page, offset)
          .then((customTagCourseResponse) async {
        if (customTagCourseResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            customTagCourses.clear();
          }
          customTagCourses.addAll(customTagCourseResponse.customTagCourses);

          paginator = customTagCourseResponse.paginator;
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
      }).catchError((err) {
        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('链知课堂'),
        centerTitle: true,
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
        actions: <Widget>[
          IconButton(
            icon: SvgPicture.asset(
              showGrid ? "images/icon_grid.svg" : "images/icon_list.svg",
              color: Colors.white,
              width: 20,
              height: 20,
            ),
            onPressed: () {
              setState(() {
                showGrid = !showGrid;
              });
            },
          ),
        ],
      ),
      body: Container(
        padding: EdgeInsets.all(10),
        child: ScrollConfiguration(
          behavior: NoShadowScrollBehavior(),
          child: RefreshIndicator(
            //指示器颜色
            color: Theme.of(context).primaryColor,
            //指示器显示时距顶部位置
            displacement: 40,
            child: CustomScrollView(
              controller: scrollController,
              slivers: <Widget>[
                getBodyWidget(),
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
        ),
      ),
    );
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }

  Widget getBodyWidget() {
    if (showGrid) {
      return getGridBodyWidget();
    } else {
      return getListBodyWidget();
    }
  }

  Widget getGridBodyWidget() {
    // 动态计算子元素宽高比
//    double cellWidth = ((MediaQuery.of(context).size.width - allHorizontalPadding) / columnCount);
    double cellWidth = ((MediaQuery.of(context).size.width - 40) / 3);
    double desiredCellHeight = 150;
    double _childAspectRatio = cellWidth / desiredCellHeight;

    return SliverGrid(
      delegate: SliverChildBuilderDelegate(
        (BuildContext context, int position) {
          return getCustomTagCourseWidget(customTagCourses[position]);
        },
        childCount: customTagCourses.length,
      ),
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        //横轴元素个数
        crossAxisCount: 3,
        //纵轴间距
        mainAxisSpacing: 10.0,
        //横轴间距
        crossAxisSpacing: 10.0,
        //子组件宽高长度比例
        childAspectRatio: _childAspectRatio,
      ),
    );
  }

  Widget getListBodyWidget() {
    return SliverList(
        delegate: SliverChildBuilderDelegate((BuildContext context, int position) {
            return Card(
              color: Colors.white,
              //z轴的高度，设置card的阴影
              elevation: 1.2,
              //设置shape，这里设置成了R角
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(4.0)),
              ),
              // 对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
              clipBehavior: Clip.antiAlias,
              semanticContainer: false,
              child: Row(
                children: <Widget>[
                  InkWell(
                    onTap: () {
                      NavigatorUtil.goRouterPage(
                          context, "${Routes.courseDetail}?course_id=${customTagCourses[position].id}");
                    },
                    // AspectRatio的作用是调整 child 到设置的宽高比
                    child:Container(
                      padding: EdgeInsets.all(10),
                      child: Image.network(
                        UIUtils.replaceMediaUrl(customTagCourses[position].smallImage),
                        width: 130,
                        height: 100,
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                  Expanded(
                    child: Container(
                      padding: EdgeInsets.only(right: 10),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          Text(customTagCourses[position].courseName,style: TextStyle(fontSize: 15),),
                          SizedBox(height: 5,),
                          Text(customTagCourses[position].courseShortDesc,
                              softWrap: true,
                              textAlign: TextAlign.left,
                              overflow: TextOverflow.ellipsis,
                              maxLines: 2,
                              style: TextStyle(fontSize: 12,color: Colors.black54)
                          ),
                          SizedBox(height: 5,),
                          Container(
                            margin: EdgeInsets.only(top: 2),
                            child: Row(
                              children: <Widget>[
                                // 课程集数和播放次数
                                Image.asset(
                                  "images/ic_views.png",
                                  width: 15,
                                  height: 15,
                                ),
                                SizedBox(
                                  width: 5,
                                ),
                                Text(customTagCourses[position].courseNumber.toString()),
                                SizedBox(
                                  width: 10,
                                ),
                                Image.asset(
                                  "images/ic_list_counts.png",
                                  width: 15,
                                  height: 15,
                                ),
                                SizedBox(
                                  width: 5,
                                ),
                                Text(customTagCourses[position].watchNumber.toString()),
                              ],
                            ),
                          ),
                          SizedBox(height: 5,),
                          Row(
                            children: <Widget>[
                              // offstage 组件控制组件是否隐藏
                              // 通过offsatge字段控制child是否显示,比较常用的控件
                              Offstage(
                                offstage: !UIUtils.isValidPrice(customTagCourses[position].price),
                                child: Padding(
                                  padding: EdgeInsets.only(right: 5),
                                  child: Text(
                                    Constants.RMB + customTagCourses[position].price,
                                    style: TextStyle(color: Colors.red),
                                  ),
                                ),
                              ),
                              Offstage(
                                offstage: customTagCourses[position] == null
                                    ? false
                                    : customTagCourses[position].isShowOldPrice == "N",
                                child: Text(
                                  Constants.RMB + customTagCourses[position].oldPrice,
                                  style: TextStyle(
                                      color: Colors.grey,
                                      decoration: TextDecoration.lineThrough),
                                ),
                              ),
                            ],
                          )

                        ],
                      ),
                    ),
                  ),
                ],
              ),
            );
        }, childCount: customTagCourses.length)
    );
  }

  Widget getCustomTagCourseWidget(CustomTagCourse customTagCourse) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        GestureDetector(
          onTap: () {
            NavigatorUtil.goRouterPage(context,
                "${Routes.courseDetail}?course_id=${customTagCourse.id}");
          },
          child: ClipRRect(
            borderRadius: BorderRadius.circular(5),
            child: Image.network(
              UIUtils.replaceMediaUrl(customTagCourse.smallImage),
              height: 100,
              width: double.infinity,
              fit: BoxFit.fill,
            ),
          ),
        ),
        Text(
          customTagCourse.courseName,
          style: TextStyle(color: Colors.black, fontWeight: FontWeight.w300,),overflow: TextOverflow.ellipsis,
        ),
        Text("${customTagCourse.courseType}/${customTagCourse.courseSubType}",
            overflow: TextOverflow.ellipsis,
            style: TextStyle(color: Colors.grey[700], fontSize: 12)),
      ],
    );
  }
}
