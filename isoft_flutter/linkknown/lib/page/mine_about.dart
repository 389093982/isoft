
import 'package:flutter/material.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/widgets/copy_right.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class AboutPage extends StatelessWidget{
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
                    child: Image.asset("images/linkknown.jpg", width: 100, height: 100,),
                  ),
                  Text("当前版本：v1.0.0",style: TextStyle(color: Colors.black54),),
                  VEmptyView(20),
                  Text("链知课堂", style: TextStyle(color: Colors.black54,fontSize: 18),),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    mainAxisSize:MainAxisSize.min,
                    children: <Widget>[
                      InkWell(
                        onTap: (){
                          NavigatorUtil.goRouterPage(context, Routes.userAgreement);
                        },
                        child: Text("用户协议"),
                      ),
                      Container(width: 20,),
                      InkWell(
                        onTap: (){
                          NavigatorUtil.goRouterPage(context, Routes.business);
                        },
                        child: Text("商业合作"),
                      ),
                    ],
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