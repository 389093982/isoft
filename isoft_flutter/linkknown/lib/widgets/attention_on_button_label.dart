import 'package:flutter/material.dart';

class AttentionOnButtonLabel extends StatelessWidget {
  static AttentionOnButtonLabel getCommonLabel(String labelText) {
    return AttentionOnButtonLabel(labelText);
  }

  String labelText;

  AttentionOnButtonLabel(this.labelText);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 5, horizontal: 8),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: Colors.pinkAccent,
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
            color: Colors.white,
          ),
        ),
      )
    );
  }
}
