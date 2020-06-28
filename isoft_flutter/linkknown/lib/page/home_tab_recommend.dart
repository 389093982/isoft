
import 'package:flutter/material.dart';

class TabRecommendWidget extends StatefulWidget {
  @override
  _TabRecommenState createState() => _TabRecommenState();
}

class _TabRecommenState extends State<TabRecommendWidget> with TickerProviderStateMixin {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Center(
        child: Text("好无聊奥~~"),
      ),
    );
  }

}