import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class DividerLineView extends StatelessWidget {
  EdgeInsetsGeometry margin;

  DividerLineView({this.margin});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: this.margin ?? EdgeInsets.all(0),
      child: Divider(
        color: Colors.grey[450],
        height: ScreenUtil().setHeight(1.0),
      ),
    );
  }
}
