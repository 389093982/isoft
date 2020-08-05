import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/second_level_comment_response.dart';
import 'package:linkknown/page/second_level_comment.dart';
import 'package:linkknown/provider/first_level_comment_refresh_notifer.dart';
import 'package:linkknown/provider/second_level_comment_refresh_notifer.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/header_icon.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:provider/provider.dart';

import 'common_button.dart';
import 'divider_line.dart';

class SecondLevelCommentItem extends StatefulWidget {
  Comment comment;

  SecondLevelCommentItem(this.comment);

  @override
  _SecondLevelCommentItemState createState() => _SecondLevelCommentItemState();
}

class _SecondLevelCommentItemState extends State<SecondLevelCommentItem> with TickerProviderStateMixin {
  _SecondLevelCommentItemState();

  String loginUserName = "";

  @override
  Future<void> initState() {
    super.initState();
    initData();
  }

  initData() async {
      loginUserName = await LoginUtil.getLoginUserName();
      setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return
      Container(
        padding: EdgeInsets.only(top: 20),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
              child: InkWell(
                onTap: () {
                  NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${widget.comment.createdBy}");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  child: ClipOval(
                    child: HeaderIconWidget(widget.comment.createdUserSmallIcon, HeaderIconSize.SIZE_SMALL_40),
                  ),
                ),
              ),
            ),
            SizedBox(width: 5,),
            Container(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Row(children: <Widget>[
                    Text(widget.comment.createdUserNickName,style: TextStyle(color: Colors.blue),),
                    Text(widget.comment.depth==2?" 回复 ":""),
                    Text(widget.comment.depth==2?"${widget.comment.referNickName}: ":"",style: TextStyle(color: Colors.blue),),
                  ],),
                  Row(children: <Widget>[
                    Row(children: <Widget>[
                      Container(
                        width: 260,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            SizedBox(height: 5,),
                            Text(widget.comment.content,style: TextStyle(fontSize: 15,color: Colors.grey[700]),overflow: TextOverflow.clip,)
                          ],
                        ),
                      )
                    ],)
                  ],),
                  Row(children: <Widget>[
                    Text(DateUtil.formatPublishTime(widget.comment.createdTime),style: TextStyle(color: Colors.black38,fontSize: 13),),
                    Text("  •  ",style: TextStyle(color: Colors.black45),),
                    InkWell(
                      onTap: (){
                        publisSecondLevelComment_reply(context,
                            widget.comment.themePk,
                            widget.comment.themeType,
                            widget.comment.commentType,
                            widget.comment.orgParentId,
                            widget.comment.id,
                            widget.comment.createdBy
                        );
                      },
                      child: Text(widget.comment.subAmount>0?"${widget.comment.subAmount}回复":"回复",style: TextStyle(color: Colors.grey[700],fontSize: 13),),
                    ),
                    SizedBox(width: 20,),
                    widget.comment.createdBy==loginUserName?
                    InkWell(
                      onTap: (){
                        deleteComment(widget.comment);
                      },
                      child: Text("删除",style: TextStyle(fontSize: 13,color: Colors.grey[700]),),
                    )
                    :
                    Text("")
                  ],),
                ],
              ),
            ),
          ],),
      );
  }


  //删除二级评论
  deleteComment(Comment comment){
    int level = comment.parentId > 0 ? 2 : 1;    // 有父评论就是二级评论，否则就是一级评论
    int id = comment.id;                         // 评论 id
    int orgParentId = comment.orgParentId;       // 父评论 id
    int themePk = comment.themePk;               // 课程 id
    String themeType = comment.themeType;
    LinkKnownApi.deleteComment(level,id,themePk,themeType,orgParentId).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("删除成功");
        Provider.of<SecondLevelCommentRefreshNotifer>(context).update(true);
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }


  //回复内容
  static String replyContent;

  //发布评论回复的回复 -- 弹框
  publisSecondLevelComment_reply(BuildContext context,int theme_pk,String theme_type,String comment_type,int orgParentId,int parentId,String referUserName) async {
    bool isLogin = await LoginUtil.checkHasLogin();
    if(isLogin){
      showModalBottomSheet(
        isScrollControlled:true,
        context: context,
        builder: (BuildContext context) {
          return SingleChildScrollView(
            child: Container(
              padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.min,
                children: <Widget>[
                  Container(
                    padding: EdgeInsets.all(20),
                    child: Column(children: <Widget>[
                      TextField(
                        maxLines: 3,
                        decoration: InputDecoration(
                          labelText: '回复内容..',
                        ),
                        onChanged: (String value) {
                          replyContent = value;
                        },
                      ),
                      VEmptyView(40),
                      VEmptyView(40),
                      CommonButton(
                        callback: () {
                          addComment(context,theme_pk,theme_type,comment_type,orgParentId,parentId,referUserName);
                        },
                        content: '回 复',
                        //width: double.infinity,
                      ),
                      VEmptyView(40),
                    ],),
                  )
                ],
              ),
            ),
          );
        },
      ).then((val) {
        print(val);
      });
    }else{
      UIUtils.showToast("未登录..");
    }
  }


  //添加回复的回复
  static addComment(BuildContext context,int theme_pk,String theme_type,String comment_type,int orgParentId,int parentId,String referUserName) {
    String content = replyContent;
    int org_parent_id = orgParentId;
    int parent_id = parentId;                   // 一级评论
    String refer_user_name = referUserName;     // 被评论人

    LinkKnownApi.AddComment(theme_pk, theme_type, comment_type, content, org_parent_id, parent_id, refer_user_name).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("评论成功");
        Navigator.of(context).pop();
        Provider.of<SecondLevelCommentRefreshNotifer>(context).update(true);
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }


}
