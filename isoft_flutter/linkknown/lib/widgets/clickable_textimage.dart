

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

typedef OnTap = void Function();

class ClickableTextImage extends StatelessWidget {

  String imgpath;
  String text;
  OnTap onTap;
  Color bg_color;
  Color icon_color;

  ClickableTextImage({this.imgpath, this.text, this.onTap,this.bg_color,this.icon_color});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Column(
        children: <Widget>[
          ClipOval(
            child: Container(
              color: bg_color==null?Colors.red:bg_color,
              padding: EdgeInsets.all(10),
              child: imgpath.endsWith(".svg") ? SvgPicture.asset(imgpath, width: 30, height: 30, color: this.icon_color,): Image.asset(imgpath, width: 30, height: 30,),
            ),
          ),
          Text(text,style: TextStyle(fontSize: 12),),
        ],
      ),
    );
  }

}