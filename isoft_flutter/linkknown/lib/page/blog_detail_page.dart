import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/blog_detail_response.dart';
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/page/course_filter.dart';
import 'package:linkknown/page/home_tab_recommend.dart';
import 'package:linkknown/page/shopping_cart_goods.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/comment_util.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/home_drawer.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

import 'my_coupon.dart';

class BlogDetailgPage extends StatefulWidget {
  int blog_id;
  BlogDetailgPage(this.blog_id);

  @override
  _BlogDetailPage createState() => _BlogDetailPage();
}

class _BlogDetailPage extends State<BlogDetailgPage> with TickerProviderStateMixin {

  Blog blog;
  User user;

  @override
  Future<void> initState() {
    initData();
    super.initState();
  }

  void initData() {
    //查询博客详情
    LinkKnownApi.ShowBlogArticleDetail(widget.blog_id.toString()).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    }).then((value) {
      if (value.status == "SUCCESS") {
        this.blog = value.blog;

        //查询用户详情
        LinkKnownApi.getUserDetail(this.blog.author).catchError((e) {
          UIUtils.showToast((e as LinkKnownError).errorMsg);
        }).then((value) {
          if (value.status == "SUCCESS") {
            this.user = value.user;
            setState(() {
              //执行build
            });
          } else {
            UIUtils.showToast(value.errorMsg);
          }
        });

      } else {
        UIUtils.showToast(value.errorMsg);
      }
    });
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Container(
            child: _HeaderWidget(),
          ),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: Container(
        child: Column(
          children: <Widget>[
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: <Widget>[
                    Row(children: <Widget>[
                      Expanded(
                        child: Container(
                          padding: EdgeInsets.all(20),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              SizedBox(height: 5,),
                              Row(
                                children: <Widget>[
                                  Container(
                                    width: 280,
                                    child: InkWell(
                                      onTap: (){

                                      },
                                      child: Text(this.blog!=null?this.blog.blogTitle:"加载中..",style: TextStyle(fontSize: 17),overflow: TextOverflow.clip,),
                                    ),
                                  )
                                ],
                              ),
                              SizedBox(height: 5,),
                              Row(
                                children: <Widget>[
                                  InkWell(
                                    onTap: (){
                                      NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}");
                                    },
                                    child: Text(this.user!=null?this.user.nickName:"加载中..",style: TextStyle(fontSize: 12,color: Colors.black54,decoration: TextDecoration.underline),),
                                  ),
                                  Text(" • ",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  Container(
                                    width: 120,
                                    child: Text(this.blog!=null?this.blog.catalogName:"加载中..",style: TextStyle(fontSize: 12,color: Colors.black54),overflow: TextOverflow.ellipsis,maxLines: 1,),
                                  ),
                                ],
                              ),
                              SizedBox(height: 5,),
                              Row(
                                children: <Widget>[
                                  Text(this.blog!=null?DateUtil.formatPublishTime(this.blog.createdTime):"加载中..",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  SizedBox(width: 20,),
//                      Icon(Icons.remove_red_eye, size: 13,color: Colors.black38,),
                                  Text((this.blog!=null?this.blog.views.toString():"0")+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  SizedBox(width: 20,),
//                      Icon(Icons.textsms, size: 13,color: Colors.black38,),
                                  Text((this.blog!=null?this.blog.comments.toString():"0")+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                ],),
                            ],
                          ),
                        ),
                      ),
                    ],
                    ),
                    Container(
                      padding: EdgeInsets.only(left: 20,right: 20),
                      alignment: Alignment.topLeft,
                      child: Text(this.blog!=null?this.blog.content:"加载中..",style: TextStyle(color: Colors.grey[700],fontSize: 15),),
                    ),
                    SizedBox(height: 50,),
                  ],
                ),
              ),
            ),
            Container(
              alignment: Alignment.center,
              padding: EdgeInsets.only(left: 20),
              height: 40,
              color: Colors.grey[200],
              child: Row(children: <Widget>[
                SizedBox(width: 70,),
                InkWell(
                  onTap: (){
                    //评论弹框
                    CommentUtil.showFirstLevelCommentDialog(context,blog.id.toString(),"blog_theme_type","comment",blog.author,blog.comments.toString());
                  },
                  child: Container(
                    width: 40,
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.textsms,color: Colors.black45,size: 20,),
                        Text(this.blog!=null?blog.comments.toString():"0",style: TextStyle(color: Colors.black45),),
                      ],
                    ),
                  ),
                ),
                SizedBox(width: 30,),
                InkWell(
                  onTap: (){},
                  child: Icon(Icons.remove_red_eye,color: Colors.black45,size: 20,),
                ),
                Text(this.blog!=null?blog.views.toString():"0",style: TextStyle(color: Colors.black45),),
                SizedBox(width: 30,),
                InkWell(
                  onTap: (){},
                  child: Icon(Icons.favorite_border,color: Colors.black45,size: 20,),
                ),
                Text("222",style: TextStyle(color: Colors.black45),),
              ],),
            ),
          ],
        ),
      )
    );
  }




}

class _HeaderWidget extends StatefulWidget {
  @override
  _HeaderWidgetState createState() => _HeaderWidgetState();
}

class _HeaderWidgetState extends State<_HeaderWidget> with TickerProviderStateMixin {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Container(
          child: Transform(
            transform: Matrix4.translationValues(0, 0, 0),
            child: Text("博客详情"),
          ),
        ),
      ],
    );
  }
}
