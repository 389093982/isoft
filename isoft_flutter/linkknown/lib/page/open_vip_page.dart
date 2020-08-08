import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/constants.dart';
import 'package:linkknown/response/base_response.dart';
import 'package:linkknown/response/pay_shopping_cart_response.dart';
import 'package:linkknown/response/query_coupon_by_id_response.dart';
import 'package:linkknown/response/search_coupon_for_pay_response.dart';
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
  String openingTime = "1个月";
  String payAmount = "12";

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
                    child: Text(this.loginUserName),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("会员类型:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text("[ 链知课堂&网站 ] 会员"),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(
                  child: Image.asset(
                    "images/vip_card.png",
                    height: 160,
                    width: 280,
                    fit: BoxFit.fill,
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("开通时长:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: DropdownButtonHideUnderline(
                      child: DropdownButton(
                          items: <String>['1个月', '3个月', '6个月', '12个月', '24个月'].map<DropdownMenuItem<String>>((String value) {
                            return DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            );
                          }).toList(),
                          value: openingTime,
                          onChanged:(String selectedValue){
                            setState(() {
                              this.openingTime = selectedValue;
                              getPayAmountByOpeningTime(this.openingTime);
                            });
                          }
                      ),
                    ),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Row(
              children: <Widget>[
                Container(child: Text("支付金额:"),),
                Expanded(
                  child: Container(
                    alignment: Alignment.topRight,
                    child: Text(Constants.RMB+payAmount,style: TextStyle(fontSize: 20,color: Colors.red),),
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
            Divider(color: Colors.black45, height: 2,),
            SizedBox(height: 20,),
            SizedBox(height: 40,),
            Row(
              children: <Widget>[
                Expanded(
                  child: GestureDetector(
                    onTap: (){
                      nowToPay();
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


  //根据开通时长获取支付金额
  getPayAmountByOpeningTime(String openingTime){
    if(openingTime.contains("1个月")){
      this.payAmount = "12";
    }else if(openingTime.contains("3个月")){
      this.payAmount = "30";
    }else if(openingTime.contains("6个月")){
      this.payAmount = "50";
    }else if(openingTime.contains("12个月")){
      this.payAmount = "80";
    }else if(openingTime.contains("24个月")){
      this.payAmount = "150";
    }
  }


  //立即付款
  nowToPay(){
    //1.微信支付
    //2.支付成功 添加订单到pay_order 表
  }



}
