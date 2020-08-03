import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/goods_item.dart';

class UserSignaturePage extends StatefulWidget {
  @override
  _UserSignaturePageState createState() => _UserSignaturePageState();
}

class _UserSignaturePageState extends State<UserSignaturePage> {

  TextEditingController signatureController;

  @override
  void initState() {
    super.initState();
    initSignature();
  }

  initSignature() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    LinkKnownApi.getUserDetail(loginUserName).then((value) {
      if (value != null) {
        if (value.status == "SUCCESS") {
          signatureController = new TextEditingController(text: value.user.userSignature);
          setState(() {
            //刷新
          });
        }
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        child: AppBar(
          title: Text("编辑个性签名"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: Container(
        padding: EdgeInsets.all(20),
        child: Column(children: <Widget>[
          SizedBox(height: 10,),
          TextField(
            controller: signatureController,
            maxLines: 2,
            maxLength: 30,
            decoration: InputDecoration(
              labelText: '个性签名',
            ),
          ),
          SizedBox(height: 20,),
          CommonButton(
            callback: () {
              editUserSignature();
            },
            content: '提 交',
            //width: double.infinity,
          ),
        ],),
      ),
    );
  }


  //编辑个性签名
  editUserSignature(){
    if(StringUtil.checkEmpty(signatureController.text)){
      UIUtils.showToast("个性签名不能为空..");
      return;
    }
    LinkKnownApi.EditUserSignature(signatureController.text).then((value) {
      if (value != null) {
        if (value.status == "SUCCESS") {
          UIUtils.showToast("更新成功");
          NavigatorUtil.goBack(context);
        }
      }
    }).catchError((e) {
      UIUtils.showToast((e as LinkKnownError).errorMsg);
    });

  }

  @override
  void dispose() {
    signatureController?.dispose();
    super.dispose();
  }


}


