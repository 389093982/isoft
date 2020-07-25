
import 'package:flutter/material.dart';
import 'package:linkknown/utils/date_util.dart';

class CopyRightWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text("Copyright © 2019-${DateUtil.getToday_YYYYMMDD().substring(0,4)} 链知网络科技版权所有",style: TextStyle(color: Colors.black54));
  }

}