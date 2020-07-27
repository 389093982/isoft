import 'package:flutter/material.dart';

class FunctionButtonLabel extends StatelessWidget {
  static FunctionButtonLabel getCommonLabel() {
    return FunctionButtonLabel();
  }

  double borderHeight;
  String labelText;
  double labelSize;
  double borderRadius;
  Color bgColor;
  Color borderColor;


  FunctionButtonLabel({this.borderHeight=20,this.labelText="按钮",this.labelSize=14,this.borderRadius=0,this.bgColor=Colors.red,this.borderColor=Colors.red});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 5, horizontal: 8),
      // 边框设置
      decoration: new BoxDecoration(
        //背景
        color: bgColor,
        //设置四周圆角 角度
        borderRadius: BorderRadius.all(Radius.circular(borderRadius)),
        //设置四周边框
        border: new Border.all(width: 1, color: borderColor),
      ),
      child: Container(
        height: borderHeight,
        child: Text(
          labelText,
          style: TextStyle(
            fontSize: labelSize,
            color: Colors.white,
          ),
        ),
      )
    );
  }
}
