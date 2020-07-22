import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';

class MingYanWidget extends StatefulWidget {
  @override
  _MingYanState createState() => _MingYanState();
}

class _MingYanState extends State<MingYanWidget> with TickerProviderStateMixin {
  String text = "";

  List mingyans = [
    "发现程序之美，遇见最好的自己",
    "今天你学习了吗？",
    "你是最棒的，奔跑吧孩子！",
    "路漫漫其修远兮，吾将上下而求索",
  ];

  Timer timer;


  @override
  void initState() {
    super.initState();

    timer = Timer.periodic(Duration(seconds: 3), (Timer t) {
      setState(() {
        text = mingyans[Random().nextInt(mingyans.length)];
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
