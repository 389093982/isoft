import 'package:flutter/material.dart';
import 'package:linkknown/widgets/v_empty_view.dart';

class BusinessPage extends StatefulWidget {
  @override
  _BusinessState createState() => _BusinessState();
}

class _BusinessState extends State<BusinessPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: new Text('商业合作'),
        leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: () {
              Navigator.pop(context);
            }),
      ),
      body: Container(
        child: ListView(
          children: <Widget>[
            Image.asset(
              "images/business.jpg",
              height: 200,
              fit: BoxFit.fill,
            ),
            Container(
              color: Colors.red,
              height: 220,
              child: Column(
                children: <Widget>[
                  VEmptyView(50),
                  Text("链知科技", style: TextStyle(color: Colors.white, fontSize: 25),),
                  Text("在线平台 / IT互联网 / 共享教育", style: TextStyle(color: Colors.white),),
                  Text("链接知识的桥梁", style: TextStyle(color: Colors.white),),
                  MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: new Text('加入商业合作群', style: TextStyle(color: Colors.white),),
                  ),
                ],
              ),
            ),
            Card(
              color: Colors.white,
              elevation: 2.0, //设置阴影
              child: Padding(
                padding: EdgeInsets.all(5),
                child: Column(
                  children: <Widget>[
                    Text("加入商业合作群", style: TextStyle(fontSize: 25),),
                    Text("商业合作是链知网重点培养的对象，希望通过链知平台可以最大作用于商家产品，实现共赢！如有“新商机”可以联系我们，我们竭诚为您服务！加入商业合作群途径：添加 QQ 群 745073865"),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
