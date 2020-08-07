import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/model/query_coupon_by_id_response.dart';
import 'package:linkknown/model/search_coupon_for_pay_response.dart';
import 'package:linkknown/route/routes.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/function_button_label.dart';
import 'package:linkknown/widgets/goods_item.dart';
import 'package:provider/provider.dart';

class OpenVipPage extends StatefulWidget {

  @override
  _OpenVipPageState createState() => _OpenVipPageState();
}

class _OpenVipPageState extends State<OpenVipPage> {

  String loginUserName;

  @override
  void initState() {
    super.initState();
  }

  queryLoginUserName() async {
    this.loginUserName = await LoginUtil.getLoginUserName();
    setState(() {
      //刷新
    });
  }


  @override
  Widget build(BuildContext context) {
    if(this.loginUserName==null){
      queryLoginUserName();
      return Text("");
    }
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("会员中心"),
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
                Container(child: Text("当前账号:"),),
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
                    child: Text("111"),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("商品价格:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text("待定",style: TextStyle(fontSize: 20,color: Colors.red),),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            SizedBox(height: 20,),
            Divider(color: Colors.black45, height: 2,),
            SizedBox(height: 20,),
            SizedBox(height: 40,),
            Row(
              children: <Widget>[
                Expanded(
                  child: GestureDetector(
                    onTap: (){
//                      nowToPay();
                    },
                    child: Container(
                      alignment: Alignment.topCenter,
                      child: FunctionButtonLabel(labelText: "                    立即付款                    ",
                        labelSize: 18,borderHeight: 28,bgColor: Colors.green,borderColor: Colors.green,),
                    ),
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
