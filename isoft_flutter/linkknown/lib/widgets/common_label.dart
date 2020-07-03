import 'package:flutter/material.dart';

class CommonLabel extends StatelessWidget {
  String labelText;

  CommonLabel(this.labelText);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 2, horizontal: 5),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: Colors.grey,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(10.0)),
        //设置四周边框
        border: new Border.all(width: 1, color: Colors.white),
      ),
      child: Text(
        labelText,
        style: TextStyle(
          fontSize: 12,
          color: Colors.white,
        ),
      ),
    );
  }
}
