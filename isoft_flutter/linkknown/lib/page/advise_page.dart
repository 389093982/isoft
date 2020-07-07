import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/utils.dart';

class AdvisePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('我要吐槽'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: () {
              Navigator.pop(context);
            }),
        actions: <Widget>[
          IconButton(
            icon: SvgPicture.asset("images/add.svg", color: Colors.white, width: 20, height: 20,),
            onPressed: () {
              NavigatorUtil.goRouterPage(context, Routes.adviseEdit);
            },
          ),
        ],
      ),
      body: Text("1111111111"),
    );
  }
}
