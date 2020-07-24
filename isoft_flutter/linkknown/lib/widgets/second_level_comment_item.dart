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
import 'package:linkknown/provider/login_user_info_notifer.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:provider/provider.dart';

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
                  NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}");
                },
                // AspectRatio的作用是调整 child 到设置的宽高比
                child:Container(
                  child: ClipOval(
                    child: Image.network(
                      UIUtils.replaceMediaUrl(widget.comment.createdUserSmallIcon),
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
                    Text(widget.comment.createdUserNickName,style: TextStyle(color: Colors.blue),),
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
//                        showSecondLevelCommentDialog(widget.comment);
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



}
