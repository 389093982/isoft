import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/blog_detail_response.dart';
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/comment_util.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:share/share.dart';

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
    LinkKnownApi.ShowBlogArticleDetail(widget.blog_id.toString()).then((value) {
      if (value.status == "SUCCESS") {
        this.blog = value.blog;

        //查询用户详情
        LinkKnownApi.getUserDetail(this.blog.author).then((value) {
          if (value.status == "SUCCESS") {
            this.user = value.user;
            setState(() {
              //执行build
            });
          } else {
            UIUtils.showToast(value.errorMsg);
          }
        }).catchError((e) {
          UIUtils.showToast((e as LinkKnownError).errorMsg);
        });

      } else {
        UIUtils.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
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
                child: this.blog==null?Text(""):
                Column(
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
                                      child: Text(this.blog.blogTitle,style: TextStyle(fontSize: 17),overflow: TextOverflow.clip,),
                                    ),
                                  )
                                ],
                              ),
                              SizedBox(height: 5,),
                              Row(
                                children: <Widget>[
                                  InkWell(
                                    onTap: (){
                                      NavigatorUtil.goRouterPage(context, "${Routes.personalCenter}?userName=${this.user.userName}");
                                    },
                                    child: Text(this.user.nickName,style: TextStyle(fontSize: 12,color: Colors.black54,decoration: TextDecoration.underline),),
                                  ),
                                  Text(" • ",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  Container(
                                    width: 120,
                                    child: Text(this.blog.catalogName,style: TextStyle(fontSize: 12,color: Colors.black54),overflow: TextOverflow.ellipsis,maxLines: 1,),
                                  ),
                                ],
                              ),
                              SizedBox(height: 5,),
                              this.blog.createdTime==this.blog.lastUpdatedTime?
                              Row(
                                children: <Widget>[
                                  Text(DateUtil.formatPublishTime(this.blog.createdTime),style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  SizedBox(width: 20,),
                                  Text((this.blog!=null?this.blog.views.toString():"0")+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  SizedBox(width: 20,),
                                  Text((this.blog!=null?this.blog.comments.toString():"0")+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                ],)
                                  :
                              Column(children: <Widget>[
                                Row(children: <Widget>[
                                  Text("更新于: "+DateUtil.formatPublishTime(this.blog.lastUpdatedTime),style: TextStyle(fontSize: 12,color: Colors.black54),),
                                ],),
                                Row(children: <Widget>[
                                  Text("发布于: "+DateUtil.formatPublishTime(this.blog.createdTime),style: TextStyle(fontSize: 12,color: Colors.black54),),
                                ],),
                                Row(children: <Widget>[
                                    Text((this.blog!=null?this.blog.views.toString():"0")+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                    SizedBox(width: 10,),
                                    Text((this.blog!=null?this.blog.comments.toString():"0")+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                  ],)
                              ],)
                            ],
                          ),
                        ),
                      ),
                    ],
                    ),
                    Container(
                      padding: EdgeInsets.only(left: 20,right: 20),
                      alignment: Alignment.topLeft,
                      child: Text(this.blog.content,style: TextStyle(color: Colors.grey[700],fontSize: 15),),
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
                SizedBox(width: 50,),
                InkWell(
                  onTap: (){
                    //评论弹框
                    CommentUtil.showFirstLevelCommentDialog(context,blog.id.toString(),"blog_theme_type","comment",blog.author,blog.comments.toString());
                  },
                  child: Container(
                    width: 90,
                    child: Row(
                      children: <Widget>[
                        Icon(Icons.textsms,color: Colors.black45,size: 20,),
                        SizedBox(width: 2,),
                        Text(this.blog!=null?blog.comments.toString():"0",style: TextStyle(color: Colors.black45),),
                      ],
                    ),
                  ),
                ),
                InkWell(
                  onTap: (){},
                  child: Container(
                    width: 90,
                    child: Row(children: <Widget>[
                      Icon(Icons.remove_red_eye,color: Colors.black45,size: 20,),
                      SizedBox(width: 2,),
                      Text(this.blog!=null?blog.views.toString():"0",style: TextStyle(color: Colors.black45),),
                    ],),
                  ),
                ),
                InkWell(
                  onTap: (){
                    Share.share(this.blog!=null?'云博客：${blog.blogTitle}':"", subject: "链知课堂");
                  },
                  child: Container(
                    width: 90,
                    child: Row(children: <Widget>[
                      Icon(Icons.share,color: Colors.black45,size: 20,),
                      SizedBox(width: 2,),
                      Text("分享",style: TextStyle(color: Colors.black45),),
                    ],),
                  ),
                ),
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
