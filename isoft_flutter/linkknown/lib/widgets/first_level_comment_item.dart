import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/first_level_comment_response.dart';
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
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:provider/provider.dart';

import 'common_button.dart';
import 'divider_line.dart';

class FirstLevelCommentItem extends StatefulWidget {
  Comment comment;

  FirstLevelCommentItem(this.comment);

  @override
  _FirstLevelCommentItemState createState() => _FirstLevelCommentItemState();
}

class _FirstLevelCommentItemState extends State<FirstLevelCommentItem> with TickerProviderStateMixin {
  _FirstLevelCommentItemState();

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
                    child: Image.network(
                      UIUtils.replaceMediaUrl(widget.comment.smallIcon),
                      width: 40,
                      height: 40,
                      fit: BoxFit.cover,
                    ),
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
                    Text(widget.comment.nickName,style: TextStyle(color: Colors.blue),),
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
                        showSecondLevelCommentDialog(widget.comment);
                      },
                      child: Text(widget.comment.subAmount>0?"${widget.comment.subAmount}回复":"回复",style: TextStyle(color: Colors.grey[700],fontSize: 13),),
                    ),
                    SizedBox(width: 20,),
                    widget.comment.userName==loginUserName?
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


  //删除一级评论
  deleteComment(Comment comment){
    int level = comment.parentId > 0 ? 2 : 1;    // 有父评论就是二级评论，否则就是一级评论
    int id = comment.id;                         // 评论 id
    int orgParentId = comment.orgParentId;       // 父评论 id
    int themePk = comment.themePk;               // 课程 id
    String themeType = comment.themeType;
    LinkKnownApi.deleteComment(level,id,themePk,themeType,orgParentId).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("删除成功");
        Provider.of<FirstLevelCommentRefreshNotifer>(context).update(true);
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    });
  }


  //二级评论弹框
  showSecondLevelCommentDialog(Comment firstLevelComment){
    showModalBottomSheet(
      isScrollControlled:true,
      context: context,
      builder: (BuildContext context) {
        return Container(
          padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
          child: Container(
            alignment: Alignment.topLeft,
            height: 600,
            padding: EdgeInsets.only(left: 10,top: 10,right: 10),
            child: Column(children: <Widget>[
              Row(children: <Widget>[
                Container(
                  alignment: Alignment.topLeft,
                  child: InkWell(
                    onTap: (){
                      Navigator.of(context).pop();
                    },
                    child: Container(
                      alignment: Alignment.topLeft,
                      width: 50,
                      child: Icon(Icons.arrow_back,size: 20,),
                    ),
                  ),
                ),
                SizedBox(width: 90,),
                Text("回复详情"),
              ],),
              SizedBox(height: 10,),
              Container(
                padding: EdgeInsets.only(left: 20),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Container(
                      child: InkWell(
                        onTap: () {
                          NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}");
                        },
                        // AspectRatio的作用是调整 child 到设置的宽高比
                        child:Container(
                          child: ClipOval(
                            child: Image.network(
                              UIUtils.replaceMediaUrl(firstLevelComment.smallIcon),
                              width: 40,
                              height: 40,
                              fit: BoxFit.cover,
                            ),
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
                            Text(firstLevelComment.nickName,style: TextStyle(color: Colors.blue),),
                          ],),
                          Row(children: <Widget>[
                            Row(children: <Widget>[
                              Container(
                                width: 260,
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: <Widget>[
                                    SizedBox(height: 5,),
                                    Text(firstLevelComment.content,style: TextStyle(fontSize: 15,color: Colors.grey[700]),overflow: TextOverflow.ellipsis,maxLines: 3,)
                                  ],
                                ),
                              )
                            ],)
                          ],),
                          Text(DateUtil.formatPublishTime(firstLevelComment.createdTime),style: TextStyle(color: Colors.black38,fontSize: 13),),
                        ],
                      ),
                    ),
                  ],),
              ),
              SizedBox(height: 10,),
              Row(children: <Widget>[
                InkWell(
                  onTap: (){
                    publisSecondLevelComment(context,
                        firstLevelComment.themePk,
                        firstLevelComment.themeType,
                        firstLevelComment.commentType,
                        firstLevelComment.id,
                        firstLevelComment.id,
                        firstLevelComment.createdBy
                    );
                  },
                  child: Text("+添加回复"),
                ),
                SizedBox(width: 170,),
                Container(child: Text("全部回复("+firstLevelComment.subAmount.toString()+")"),),
              ],),
              SizedBox(height: 10,),
              DividerLineView(
                margin: EdgeInsets.symmetric(horizontal: 10),
              ),
              SizedBox(height: 10,),
              Expanded(
                child: SecondLevelCommentWidget(firstLevelComment.themePk.toString(),firstLevelComment.themeType,firstLevelComment.id),
              ),
            ],),
          ),
        );
      },
    ).then((val) {
      print(val);
    });
  }


  //评论内容
  static String secondLevelCommentContent;

  //发布二级评论 -- 弹框
  publisSecondLevelComment(BuildContext context,int theme_pk,String theme_type,String comment_type,int orgParentId,int parentId,String referUserName) async {
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
                          secondLevelCommentContent = value;
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


  //添加二级评论
  static addComment(BuildContext context,int theme_pk,String theme_type,String comment_type,int orgParentId,int parentId,String referUserName) {
    String content = secondLevelCommentContent;
    int org_parent_id = orgParentId;
    int parent_id = parentId;                   // 一级评论
    String refer_user_name = referUserName;     // 被评论人

    LinkKnownApi.AddComment(theme_pk, theme_type, comment_type, content, org_parent_id, parent_id, refer_user_name).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if(value.status=="SUCCESS"){
        UIUtils.showToast("评论成功");
        Navigator.of(context).pop();
        Provider.of<SecondLevelCommentRefreshNotifer>(context).update(true);
      }else{
        UIUtils.showToast(value.errorMsg);
      }
    });
  }



}
