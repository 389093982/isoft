
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/first_level_comment.dart';
import 'package:linkknown/page/second_level_comment.dart';

class CommentUtil {

    //显示一级评论
  static showFirstLevelCommentDialog(BuildContext context,String theme_pk,String theme_type,String comment_type){
   showModalBottomSheet(
     isScrollControlled:true,
     context: context,
     builder: (BuildContext context) {
       return Container(
         padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
         child: Container(
           alignment: Alignment.topLeft,
           height: 450,
           padding: EdgeInsets.only(left: 20,top: 20,right: 20),
           child: Column(children: <Widget>[
             Row(children: <Widget>[
               Container(child: Text("+发表评论"),),
               SizedBox(width: 150,),
               Container(child: Text("全部评论(208)"),),
             ],),
             SizedBox(height: 10,),
             Expanded(
               child: ListView(
                 shrinkWrap: true,
                 children: <Widget>[
                   FirstLevelCommentWidget(theme_pk,theme_type,comment_type),
                 ],
               ),
             ),
           ],),
         ),
       );
     },
   ).then((val) {
     print(val);
   });
 }




}