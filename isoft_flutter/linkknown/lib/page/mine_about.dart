import 'dart:async';

import 'package:flutter/material.dart';
import 'package:infinite_cards/infinite_cards.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/click_item.dart';
import 'package:linkknown/widgets/copy_right.dart';
import 'package:package_info/package_info.dart';

class AboutPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _AboutPageState();
}

class _AboutPageState extends State<AboutPage> {
  String version = "";

  InfiniteCardsController infiniteCardsController;
  Timer timer;

  @override
  void initState() {
    super.initState();

    infiniteCardsController = InfiniteCardsController(
      itemBuilder: _renderItem,
      itemCount: 5,
      animType: AnimType.SWITCH,
    );
    timer = Timer.periodic(Duration(seconds: 3), (Timer t) {
      infiniteCardsController.reset(animType: AnimType.TO_END);
      infiniteCardsController.next();
    });

    initData();
  }

  Widget _renderItem(BuildContext context, int index) {
    return Image(
      image: AssetImage(index % 2 == 0 ? 'images/nuli.jpg' : 'images/image_wenjuan.png',),
    );
  }

  initData() async {
    PackageInfo packageInfo = await CommonUtil.getPackageInfo();
    setState(() {
      version = packageInfo.version;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('关于链知'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: Stack(
        children: <Widget>[
          Align(
            alignment: Alignment.topCenter,
            child: Container(
              margin: EdgeInsets.only(top: 50),
              child: Column(
                children: <Widget>[
                  InfiniteCards(
                    controller: infiniteCardsController,
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height / 3,
                  ),
//                  ClipOval(
//                    child: Image.asset(
//                      "images/linkknown.jpg",
//                      width: 100,
//                      height: 100,
//                    ),
//              ),
                  SizedBox(height: 10,),
                  ClickItem(
                    title: "应用名称",
                    content: "链知课堂",
                  ),
                  ClickItem(
                    title: "当前版本",
                    content: "v$version",
                  ),
                  ClickItem(
                    title: "用户协议",
                    onTap: () {
                      NavigatorUtil.goRouterPage(context, Routes.userAgreement);
                    },
                  ),
                  ClickItem(
                    title: "商业合作",
                    onTap: () {
                      NavigatorUtil.goRouterPage(context, Routes.business);
                    },
                  ),
                ],
              ),
            ),
          ),
          Align(
            alignment: Alignment.bottomCenter,
            child: Padding(
              padding: EdgeInsets.only(bottom: 20),
              child: CopyRightWidget(),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }
}
