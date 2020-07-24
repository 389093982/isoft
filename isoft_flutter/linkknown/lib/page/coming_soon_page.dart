
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ComingSoonPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Image.asset("images/page_coming_soon.gif", width: double.infinity, height: double.infinity, fit: BoxFit.fill,),
    );
  }

}