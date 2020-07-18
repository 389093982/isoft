import 'package:flutter/material.dart';

class ButtonLabel extends StatelessWidget {
  static ButtonLabel getCommonLabel(String labelText) {
    return ButtonLabel(labelText);
  }

  static ButtonLabel getCommonLabel2(String labelText) {
    return ButtonLabel(labelText, bgColor: Colors.red,padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),);
  }

  String labelText;
  Color textColor;
  Color bgColor;
  EdgeInsetsGeometry padding;

  ButtonLabel(this.labelText,
      {this.textColor = Colors.black54, this.bgColor = Colors.grey, this.padding,});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: padding??EdgeInsets.symmetric(vertical: 2, horizontal: 5),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: Colors.grey[100],
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(10.0)),
        //设置四周边框
        border: new Border.all(width: 1, color: Colors.black54),
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
