import 'package:flutter/material.dart';

class AttentionOffButtonLabel extends StatelessWidget {
  static AttentionOffButtonLabel getCommonLabel(String labelText) {
    return AttentionOffButtonLabel(labelText);
  }

  String labelText;

  AttentionOffButtonLabel(this.labelText);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 5, horizontal: 8),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: Colors.white,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(4.0)),
        //设置四周边框
        border: new Border.all(width: 1, color: Colors.pinkAccent),
      ),
      child: Container(
        width: 40,
        child: Text(
          labelText,
          style: TextStyle(
            fontSize: 12,
            color: Colors.pinkAccent,
          ),
        ),
      )
    );
  }
}
