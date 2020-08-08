import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/response/advise_list_response.dart';
import 'package:linkknown/response/get_user_info_by_names_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/divider_line.dart';
import 'package:linkknown/widgets/header_icon.dart';

class AdvisePage extends StatefulWidget {
  @override
  _AdvisePageState createState() => _AdvisePageState();
}

class _AdvisePageState extends State<AdvisePage> {
  List<Advise> adviseList = new List();
  Set<User> users = new Set();
  Paginator paginator;
  int current_page = 1;
  dynamic loadingStatus;

  ScrollController scrollController = ScrollController();

  @override
  void initState() {
    super.initState();

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

  void initData() {
    loadPageData(1, 10, resetLoadingStatus: true);
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
      LinkKnownApi.queryPageAdvise(current_page, offset)
          .then((adviseListResponse) async {
        if (adviseListResponse?.status == "SUCCESS") {
          if (current_page == 1) {
            adviseList.clear();
          }
          adviseList.addAll(adviseListResponse.advises);

          // 获取userName字段，并用逗号拼接
          await getUserInfoByNames(adviseListResponse);

          paginator = adviseListResponse.paginator;
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
//      UIUtils.showToast((e as LinkKnownError).errorMsg);

        setState(() {
          loadingStatus = LoadingStatus.LOADED_FAILED;
        });
      });
    });
  }

  void getUserInfoByNames(AdviseListResponse adviseListResponse) async {
    // 获取userName字段，并用逗号拼接
    String userNames = adviseListResponse.advises
        .map((advise) => advise.createdBy)
        .toSet()
        .join(",");
    //根据usernames查询用户信息
    GetUserInfoByNamesResponse response =
        await LinkKnownApi.GetUserInfoByNames(userNames);
    if (response.status == "SUCCESS") {
      users.addAll(response.users);
    }
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
      body: ScrollConfiguration(
        behavior: NoShadowScrollBehavior(),
        child: RefreshIndicator(
            child: CustomScrollView(
              controller: scrollController,
              slivers: <Widget>[
                SliverList(
                    delegate: SliverChildBuilderDelegate(
                        (BuildContext context, int position) {
                  return Column(
                    children: <Widget>[
                      Container(
                        padding: EdgeInsets.all(15),
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            HeaderIconWidget(
                              UIUtils.replaceMediaUrl(users
                                  .firstWhere((element) =>
                                      element.userName ==
                                      adviseList[position].createdBy)
                                  ?.smallIcon),
                              HeaderIconSize.SIZE_NORMAL_50
                            ),
                            SizedBox(
                              width: 10,
                            ),
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: <Widget>[
                                Row(
                                  children: <Widget>[
                                    Text(users.firstWhere((element) => element.userName == adviseList[position].createdBy)?.nickName,overflow: TextOverflow.ellipsis,style: TextStyle(color:Colors.orangeAccent)),
                                    Image.asset(
                                      "male"==users.firstWhere((element) => element.userName == adviseList[position].createdBy)?.gender?"images/ic_male.png":"images/ic_female.png",
                                      height: 20,
                                      fit: BoxFit.fill,
                                    ),
                                  ],
                                ),
                                Row(
                                  children: <Widget>[
                                    Container(
                                      width: 260,
                                      child: Text(adviseList[position].advise,overflow:TextOverflow.clip,style: TextStyle(color: Colors.black45,fontSize: 13),)
                                    )
                                  ],
                                ),
                                SizedBox(height: 5,),
                                Text(DateUtil.format2StandardTime(adviseList[position].createdTime),style: TextStyle(color:Colors.grey,fontSize: 12),),
                              ],
                            )
                          ],
                        ),
                      ),
                      DividerLineView(),
                    ],
                  );
                }, childCount: adviseList.length)),
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
            onRefresh: _onRefresh),
      ),
    );
  }

  @override
  void dispose() {
    scrollController?.dispose();
    super.dispose();
  }
}
