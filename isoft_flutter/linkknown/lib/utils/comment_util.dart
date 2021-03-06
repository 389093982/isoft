
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/page/first_level_comment_widget.dart';
import 'package:linkknown/page/second_level_comment_widget.dart';
import 'package:linkknown/provider/first_level_comment_refresh_notifer.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/ui_util.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:provider/provider.dart';

class CommentUtil {

    //显示一级评论
  static showFirstLevelCommentDialog(BuildContext context,String theme_pk,String theme_type,String comment_type,String author,String currentCommentCounts){
   showModalBottomSheet(
     isScrollControlled:true,
     context: context,
     builder: (BuildContext context) {
       return Container(
         padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
         child: Container(
           alignment: Alignment.topLeft,
           height: 480,
           padding: EdgeInsets.only(left: 10,top: 10,right: 10),
           child: Column(children: <Widget>[
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
             SizedBox(height: 10,),
             Row(children: <Widget>[
               InkWell(
                 onTap: (){
                   publisFirstLevelComment(context,theme_pk,theme_type,comment_type,author);
                 },
                 child: Text("+发表评论"),
               ),
               SizedBox(width: 170,),
               Container(child: Text("全部评论("+currentCommentCounts+")"),),
             ],),
             SizedBox(height: 10,),
             FirstLevelCommentWidget(theme_pk,theme_type,comment_type),
           ],),
         ),
       );
     },
   ).then((val) {
     print(val);
   });
 }

 //评论内容
  static String firstLevelCommentContent;

 //发布一级评论--弹框
 static publisFirstLevelComment(BuildContext context,String theme_pk,String theme_type,String comment_type,String author) async {
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
                         labelText: '评论内容..',
                       ),
                       onChanged: (String value) {
                         firstLevelCommentContent = value;
                       },
                     ),
                     VEmptyView(40),
                     VEmptyView(40),
                     CommonButton(
                       callback: () {
                         addComment(context,int.parse(theme_pk),theme_type,comment_type,author);
                       },
                       content: '评 论',
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
     UIUtil.showToast("未登录..");
   }
 }

 //添加一级评论
 static addComment(BuildContext context,int theme_pk,String theme_type,String comment_type,String author) {
   String content = firstLevelCommentContent;
   int org_parent_id = 0;
   int parent_id = 0;                   // 一级评论
   String refer_user_name = author;     // 被评论人

   LinkKnownApi.AddComment(theme_pk, theme_type, comment_type, content, org_parent_id, parent_id, refer_user_name).then((value) {
     if(value.status=="SUCCESS"){
       UIUtil.showToast("评论成功");
       Navigator.of(context).pop();
       Provider.of<FirstLevelCommentRefreshNotifer>(context).update(true);
     }else{
       UIUtil.showToast(value.errorMsg);
     }
   }).catchError((e) {
     UIUtil.showToast((e as LinkKnownError).errorMsg);
   });
 }


}