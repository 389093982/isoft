import 'package:flutter/material.dart';

class CommonLabel extends StatelessWidget {

  static CommonLabel getCommonLabel(String labelText) {
    return CommonLabel(labelText);
  }

  static CommonLabel getCommonLabel2(String labelText) {
    return CommonLabel(
      labelText,
      bgColor: Colors.red,
      padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
    );
  }

  static CommonLabel getCommonLabel3(String labelText) {
    return CommonLabel(
      labelText,
      bgColor: Colors.grey[500],
      padding: EdgeInsets.symmetric(vertical: 2, horizontal: 5),
      radius: 0,
    );
  }

  static CommonLabel getCommonLabel4(String labelText) {
    return CommonLabel(
      labelText,
      textColor: Colors.white,
      bgColor: Colors.yellow[800],
      padding: EdgeInsets.symmetric(vertical: 2, horizontal: 5),
      radius: 0,
    );
  }

  String labelText;
  Color textColor;
  Color bgColor;
  EdgeInsetsGeometry padding;
  double radius;

  CommonLabel(
    this.labelText, {
    this.textColor = Colors.white,
    this.bgColor = Colors.grey,
    this.padding = const EdgeInsets.symmetric(vertical: 2, horizontal: 5),
    this.radius = 10.0,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: padding,
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: bgColor,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(radius)),
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
