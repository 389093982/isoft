import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';

class MingYanWidget extends StatefulWidget {
  @override
  _MingYanState createState() => _MingYanState();
}

class _MingYanState extends State<MingYanWidget> with TickerProviderStateMixin {
  String text = "";
  int showIndex = 0;

  List mingyans = [
    "发现程序之美",
    "遇见最好的自己",
    "今天你学习了吗？",
    "路漫漫其修远兮",
    "吾将上下而求索",
  ];

  Timer timer;


  @override
  void initState() {
    super.initState();

    timer = Timer.periodic(Duration(seconds: 3), (Timer t) {
      setState(() {
        if (showIndex < mingyans.length - 1) {
          showIndex ++;
        } else {
          showIndex = 0;
        }
        text = mingyans[showIndex];
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Text(text, style: TextStyle(fontSize: 14, color: Colors.white),);
  }

  @override
  void dispose() {
    super.dispose();
    timer?.cancel();
  }
}
