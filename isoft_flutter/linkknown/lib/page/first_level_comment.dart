import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_history_response.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/first_level_comment_response.dart';
import 'package:linkknown/model/user_favorite_list_response.dart';
import 'package:linkknown/provider/first_level_comment_refresh_notifer.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:linkknown/widgets/first_level_comment_item.dart';
import 'package:provider/provider.dart';

class FirstLevelCommentWidget extends StatefulWidget {
  String theme_pk;
  String theme_type;
  String comment_type;
  FirstLevelCommentWidget(this.theme_pk,this.theme_type,this.comment_type);

  @override
  _FirstLevelCommentState createState() => _FirstLevelCommentState();

}

class _FirstLevelCommentState extends State<FirstLevelCommentWidget> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<Comment> firstLevelComments = new List();
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

    LinkKnownApi.FilterCommentFirstLevel(widget.theme_pk,widget.theme_type,widget.comment_type,current_page, offset).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).then((value) {
      if (current_page == 1) {
        firstLevelComments.clear();
      }
      firstLevelComments.addAll(value.comments);
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
    return Consumer(
        builder: (BuildContext context, FirstLevelCommentRefreshNotifer firstLevelCommentRefreshNotifer, Widget child) {
          if (firstLevelCommentRefreshNotifer.hasChanged) {
            initData();
            firstLevelCommentRefreshNotifer.hasChanged = false;
          }
          return RefreshIndicator(
            child: Container(
              height: 400,
              padding: EdgeInsets.symmetric(horizontal: 5),
              child: Column(
                children: <Widget>[
                  Expanded(
                    child: ListView.builder(
                        controller: scrollController,
                        physics: AlwaysScrollableScrollPhysics(),
                        shrinkWrap: true,
                        itemCount: firstLevelComments.length,
                        itemBuilder: (BuildContext context, int index) {
                          return FirstLevelCommentItem(firstLevelComments[index]);
                        }),
                  )
                ],
              ),
            ),
            onRefresh: _onRefresh,
          );
        }
    );
  }


  @override
  void dispose() {
    super.dispose();
    scrollController.dispose();
  }

}