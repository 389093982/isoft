
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
               Container(child: Text("+发表评论"),),
               SizedBox(width: 150,),
               Container(child: Text("全部评论(208)"),),
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




}