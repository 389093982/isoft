
import 'package:flutter/material.dart';

class CourseSearchPage extends StatelessWidget {

  String search;
  String isCharge;

  CourseSearchPage(this.search, this.isCharge);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        preferredSize:
        Size.fromHeight(MediaQuery.of(context).size.height * 0.07),
        child: SafeArea(
          top: true,
          child: Offstage(),
        ),
      ),
      backgroundColor: Colors.white,
      body: Column(
        children: <Widget>[
          Text("1111111111111"),
        ],
      ),
    );
  }

}