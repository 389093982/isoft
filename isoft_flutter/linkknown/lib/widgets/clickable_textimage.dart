

import 'package:flutter/material.dart';

typedef OnTap = void Function();

class ClickableTextImage extends StatelessWidget {

  String imgpath;
  String text;
  OnTap onTap;

  ClickableTextImage({this.imgpath, this.text, this.onTap});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Column(
        children: <Widget>[
          ClipOval(
            child: Container(
              color: Colors.red,
              padding: EdgeInsets.all(10),
              child: Image.asset(imgpath, width: 30, height: 30,),
            ),
          ),
          Text(text),
        ],
      ),
    );
  }

}