
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/utils/utils.dart';

class PersonalCenterPage extends StatefulWidget {

  @override
  _PersonalCenterState createState() => _PersonalCenterState();

}

class _PersonalCenterState  extends State<PersonalCenterPage> {

  String text = "aaa";

  @override
  Widget build(BuildContext context) {
    UIUtils.showToast(text);
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('个人中心'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: GestureDetector(
        onTap: () {
          setState(() {
            text += "a";
          });
        },
        child: AAAA(text),
      ),
    );
  }

}



class AAAA extends StatefulWidget {

  String text;

  AAAA(String text) {
    this.text = text;
    UIUtils.showToast("调用了 Widget 的构造器");
  }

  @override
  AAAAAAAAAAAAAA createState() => AAAAAAAAAAAAAA(text);

}

class AAAAAAAAAAAAAA  extends State<AAAA> {

  String text;

  AAAAAAAAAAAAAA(String text) {
    this.text = text;
    UIUtils.showToast("调用了 State 的构造器");
  }

  @override
  Widget build(BuildContext context) {
    return Text(text);
  }

}

