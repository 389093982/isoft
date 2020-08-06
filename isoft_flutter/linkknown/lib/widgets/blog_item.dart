import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/get_user_info_by_names_response.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/query_page_blog_response.dart';
import 'package:linkknown/model/user_link_agent_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/cached_image.dart';
import 'package:linkknown/widgets/common_label.dart';
import 'package:linkknown/widgets/header_icon.dart';

import 'accept_invite_button_label.dart';
import 'button_label.dart';
import 'clickable_textimage.dart';

class BlogItemWidget extends StatefulWidget {
  Blog blog;
  User user;

  VoidCallback refreshCallback;

  BlogItemWidget(this.blog,this.user, {this.refreshCallback});

  @override
  _BlogItemState createState() => _BlogItemState();
}

class _BlogItemState extends State<BlogItemWidget> with TickerProviderStateMixin {
  _BlogItemState();

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      //z轴的高度，设置card的阴影
      elevation: 0.1,
      //设置shape，这里设置成了R角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(4.0)),
      ),
      // 对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
      clipBehavior: Clip.antiAlias,
      semanticContainer: false,
      child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              // Stack类似FrameLayout,子 widget可以通过父容器的四个角固定位置,子widget可以重叠
              Stack(
                children: <Widget>[
                  InkWell(
                    onTap: () {
                      NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${widget.user.userName}");
                    },
                    // AspectRatio的作用是调整 child 到设置的宽高比
                    child:Container(
                      margin: EdgeInsets.only(left: 10,top: 10),
                      child: ClipOval(
                        child: HeaderIconWidget(widget.user.smallIcon,HeaderIconSize.SIZE_SMALL_40),
                      ),
                    ),
                  ),
                ],
              ),
              Expanded(
                child: Container(
                  padding: EdgeInsets.only(top: 2,bottom: 2,left: 5),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      SizedBox(height: 10,),
                      Row(
                        children: <Widget>[
                          Container(
                            width: 280,
                            child: InkWell(
                              onTap: () async {
                                await NavigatorUtil.goRouterPage(context, "${Routes.blogDetail}?blog_id=${widget.blog.id}");
                                widget.refreshCallback();
                              },
                              child: Text(widget.blog.blogTitle,style: TextStyle(fontSize: 15),overflow: TextOverflow.ellipsis,maxLines: 1,),
                            ),
                          )
                        ],
                      ),
                      SizedBox(height: 2,),
                      Row(
                        children: <Widget>[
                          InkWell(
                            onTap: (){
                              NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${widget.user.userName}");
                            },
                            child: Text(widget.user.nickName,style: TextStyle(fontSize: 12,color: Colors.black54,decoration: TextDecoration.underline),),
                          ),
                          Text(" • ",style: TextStyle(fontSize: 12,color: Colors.black54),),
                          Container(
                            width: 120,
                            child: Text(widget.blog.catalogName,style: TextStyle(fontSize: 12,color: Colors.black54),overflow: TextOverflow.ellipsis,maxLines: 1,),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
          widget.blog.createdTime==widget.blog.lastUpdatedTime?
          Container(
            margin: EdgeInsets.only(left: 55),
            child: Column(
              children: <Widget>[
                Row(
                  children: <Widget>[
                    Text((DateUtil.formatPublishTime(widget.blog.createdTime)),style: TextStyle(fontSize: 12,color: Colors.black54),),
                    SizedBox(width: 10,),
                    Text(widget.blog.views.toString()+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                    SizedBox(width: 10,),
                    Text(widget.blog.comments.toString()+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                  ],),
                Row(
                  children: <Widget>[
                    SizedBox(height: 10,),
                  ],
                )
              ],
            ),
          )
              :
          Container(
            margin: EdgeInsets.only(left: 55),
            child: Column(
              children: <Widget>[
                Row(
                  children: <Widget>[
                    widget.blog.createdTime==widget.blog.lastUpdatedTime?Text(""):
                    Text("更新于: "+(DateUtil.formatPublishTime(widget.blog.lastUpdatedTime)),style: TextStyle(fontSize: 12,color: Colors.black54),),
                  ],),
                SizedBox(height: 2,),
                Row(
                  children: <Widget>[
                    Text("发布于: "+(DateUtil.formatPublishTime(widget.blog.createdTime)),style: TextStyle(fontSize: 12,color: Colors.black54),),
                  ],),
                SizedBox(height: 2,),
                Row(
                  children: <Widget>[
                    Text(widget.blog.views.toString()+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                    SizedBox(width: 10,),
                    Text(widget.blog.comments.toString()+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                  ],
                ),
                SizedBox(height: 10,),
              ],
            ),
          )
        ],
      )
    );
  }


}
