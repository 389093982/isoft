import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/response/blog_detail_response.dart';
import 'package:linkknown/response/get_user_detail_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/comment_util.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/ui_util.dart';
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
  bool isShowEditIcon = false;

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
            //做个判断是否展示编辑按钮
            showEditIcon();
          } else {
            UIUtil.showToast(value.errorMsg);
          }
        }).catchError((e) {
          UIUtil.showToast((e as LinkKnownError).errorMsg);
        });

      } else {
        UIUtil.showToast(value.errorMsg);
      }
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
  }

  //是否展示编辑按钮
  showEditIcon() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    if(StringUtil.checkNotEmpty(loginUserName) && loginUserName==blog.author){
      this.isShowEditIcon = true;
      setState(() {
        //刷新一下
      });
    }else{
      this.isShowEditIcon = false;
      setState(() {
        //刷新一下
      });
    }
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
                              Column(children: <Widget>[
                                Row(children: <Widget>[
                                    Text(DateUtil.formatPublishTime(this.blog.createdTime),style: TextStyle(fontSize: 12,color: Colors.black54),),
                                    SizedBox(width: 10,),
                                    Text((this.blog!=null?this.blog.views.toString():"0")+"次阅读",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                    SizedBox(width: 10,),
                                    Text((this.blog!=null?this.blog.comments.toString():"0")+"条评论",style: TextStyle(fontSize: 12,color: Colors.black54),),
                                    SizedBox(width: 10,),
                                  ],),
                                Row(children: <Widget>[
                                    this.isShowEditIcon?
                                    GestureDetector(
                                      onTap: ()async{
                                        await NavigatorUtil.goRouterPage(context, "${Routes.editBlog}?blog_id=${this.blog.id}");
                                        initData();
                                      },
                                      child: Row(children: <Widget>[
                                        Text("编辑",style: TextStyle(fontSize: 12,color: Colors.orange),),
                                        Icon(Icons.edit, color: Colors.orange,size: 16,),
                                      ],),
                                    ):Text(""),
                                    SizedBox(width: 10,),
                                    this.isShowEditIcon?
                                    GestureDetector(
                                      onTap: ()async{
                                        deleteBlog(this.blog.id);
                                      },
                                      child: Row(children: <Widget>[
                                        Text("删除",style: TextStyle(fontSize: 12,color: Colors.orange),),
                                        Icon(Icons.delete, color: Colors.orange,size: 16,),
                                      ],),
                                    ):Text(""),
                                  ],
                                )
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
                                  SizedBox(width: 20,),
                                  this.isShowEditIcon?
                                  GestureDetector(
                                    onTap: ()async{
                                      await NavigatorUtil.goRouterPage(context, "${Routes.editBlog}?blog_id=${this.blog.id}");
                                      initData();
                                    },
                                    child: Row(children: <Widget>[
                                      Text("编辑",style: TextStyle(fontSize: 12,color: Colors.orange),),
                                      Icon(Icons.edit, color: Colors.orange,size: 16,),
                                    ],),
                                  ):Text(""),
                                  SizedBox(width: 10,),
                                  this.isShowEditIcon?
                                  GestureDetector(
                                    onTap: ()async{
                                      deleteBlog(this.blog.id);
                                    },
                                    child: Row(children: <Widget>[
                                      Text("删除",style: TextStyle(fontSize: 12,color: Colors.orange),),
                                      Icon(Icons.delete, color: Colors.orange,size: 16,),
                                    ],),
                                  ):Text(""),
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


  //删除博客
  deleteBlog(int blog_id){
    LinkKnownApi.ArticleDelete(blog_id.toString()).then((value) {
      if (value.status == "SUCCESS") {
        UIUtil.showToast("删除成功");
        NavigatorUtil.goBack(context);
      } else {
        UIUtil.showToast("删除失败");
      }
    }).catchError((e) {
      UIUtil.showToast((e as LinkKnownError).errorMsg);
    });
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
