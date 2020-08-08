import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/course_history_response.dart';
import 'package:linkknown/response/course_meta_response.dart';
import 'package:linkknown/response/second_level_comment_response.dart';
import 'package:linkknown/response/user_favorite_list_response.dart';
import 'package:linkknown/provider/first_level_comment_refresh_notifer.dart';
import 'package:linkknown/provider/second_level_comment_refresh_notifer.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/course_card.dart';
import 'package:linkknown/widgets/first_level_comment_item.dart';
import 'package:linkknown/widgets/second_level_comment_item.dart';
import 'package:provider/provider.dart';

class SecondLevelCommentWidget extends StatefulWidget {
  String theme_pk;
  String theme_type;
  int org_parent_id;
  SecondLevelCommentWidget(this.theme_pk,this.theme_type,this.org_parent_id);

  @override
  _SecondLevelCommentState createState() => _SecondLevelCommentState();

}

class _SecondLevelCommentState extends State<SecondLevelCommentWidget> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive => true;

  List<Comment> secondLevelComments = new List();
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

    LinkKnownApi.FilterCommentSecondLevel(int.parse(widget.theme_pk),widget.theme_type,widget.org_parent_id,current_page, offset).then((value) {
      if (current_page == 1) {
        secondLevelComments.clear();
      }
      secondLevelComments.addAll(value.comments);
      setState(() {
        isLoading = false;
        showMore = false;
      });
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
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
        builder: (BuildContext context, SecondLevelCommentRefreshNotifer secondLevelCommentRefreshNotifer, Widget child) {
          if (secondLevelCommentRefreshNotifer.hasChanged) {
            initData();
            secondLevelCommentRefreshNotifer.hasChanged = false;
          }
          return RefreshIndicator(
            child: Container(
              padding: EdgeInsets.only(left: 5,right: 5,bottom: 10),
              child: Column(
                children: <Widget>[
                  Expanded(
                    child: ListView.builder(
                        controller: scrollController,
                        physics: AlwaysScrollableScrollPhysics(),
                        shrinkWrap: true,
                        itemCount: secondLevelComments.length,
                        itemBuilder: (BuildContext context, int index) {
                          return SecondLevelCommentItem(secondLevelComments[index]);
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