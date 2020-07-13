import 'package:flutter/material.dart';

class CommonLabel extends StatelessWidget {
  static CommonLabel getCommonLabel(String labelText) {
    return CommonLabel(labelText);
  }

  static CommonLabel getCommonLabel2(String labelText) {
    return CommonLabel(labelText, bgColor: Colors.red,padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),);
  }

  String labelText;
  Color textColor;
  Color bgColor;
  EdgeInsetsGeometry padding;

  CommonLabel(this.labelText,
      {this.textColor = Colors.white, this.bgColor = Colors.grey, this.padding,});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: padding??EdgeInsets.symmetric(vertical: 2, horizontal: 5),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: bgColor,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(10.0)),
        //设置四周边框
        border: new Border.all(width: 1, color: Colors.white),
      ),
      child: Text(
        labelText,
        style: TextStyle(
          fontSize: 12,
          color: textColor,
        ),
      ),
    );
  }
}
