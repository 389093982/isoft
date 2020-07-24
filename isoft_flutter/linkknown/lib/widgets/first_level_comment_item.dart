import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/first_level_comment_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';

import 'divider_line.dart';

class FirstLevelCommentItem extends StatefulWidget {
  Comment comment;

  FirstLevelCommentItem(this.comment);

  @override
  _FirstLevelCommentItemState createState() => _FirstLevelCommentItemState();
}

class _FirstLevelCommentItemState extends State<FirstLevelCommentItem>
    with TickerProviderStateMixin {

  _FirstLevelCommentItemState();

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
                    Text("昵称.."),
                  ],),
                  Row(children: <Widget>[
                    Row(children: <Widget>[
                      Container(
                        width: 260,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            SizedBox(height: 5,),
                            Text(widget.comment.content,style: TextStyle(fontSize: 15),overflow: TextOverflow.clip,)
                          ],
                        ),
                      )
                    ],)
                  ],),
                  Row(children: <Widget>[
                    Text("日期+回复"),
                  ],),
                ],
              ),
            ),
          ],),
      );
  }


}
