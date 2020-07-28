import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/goods_item.dart';

class PayOrderCommitPage extends StatefulWidget {
  String goodsType;
  String goodsId;
  String goodsImg;
  String goodsDesc;
  String price;

  PayOrderCommitPage(this.goodsType,this.goodsId,this.goodsImg,this.goodsDesc,this.price);

  @override
  _PayOrderCommitPageState createState() => _PayOrderCommitPageState();
}

class _PayOrderCommitPageState extends State<PayOrderCommitPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("提交订单"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: Container(
//        alignment: Alignment.topRight,
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                Container(child: Text("支付方式:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text("微信支付"),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品描述:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text(widget.goodsDesc),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
