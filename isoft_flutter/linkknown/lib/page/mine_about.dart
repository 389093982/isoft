import 'package:flutter/material.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/common_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/click_item.dart';
import 'package:linkknown/widgets/copy_right.dart';
import 'package:linkknown/widgets/v_empty_view.dart';
import 'package:package_info/package_info.dart';

class AboutPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _AboutPageState();
}

class _AboutPageState extends State<AboutPage> {
  String version = "";

  @override
  void initState() {
    super.initState();

    initData();
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
              margin: EdgeInsets.only(top: 120),
              child: Column(
                children: <Widget>[
                  ClipOval(
                    child: Image.asset(
                      "images/linkknown.jpg",
                      width: 100,
                      height: 100,
                    ),
                  ),
                  SizedBox(height: 20,),
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
}
