import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/login_dialog.dart';
import 'package:linkknown/common/scroll_helper.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/common_loading.dart';
import 'package:linkknown/widgets/goods_item.dart';

class UserInfoPage extends StatefulWidget {
  @override
  _UserInfoPageState createState() => _UserInfoPageState();
}

class _UserInfoPageState extends State<UserInfoPage> {

  TextEditingController nickNameController;
  TextEditingController birthDayController;
  TextEditingController genderController;
  TextEditingController currentResidenceController;
  TextEditingController hometownController;

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
          nickNameController = new TextEditingController(text: value.user.nickName);
          birthDayController = new TextEditingController(text: DateUtil.format2StandardTime(value.user.birthday).substring(0,10));
          genderController = new TextEditingController(text: value.user.gender);
          currentResidenceController = new TextEditingController(text: value.user.currentResidence);
          hometownController = new TextEditingController(text: value.user.hometown);
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
          title: Text("用户基本信息"),
        ),
        preferredSize: Size.fromHeight(60.0),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(20),
          child: Column(children: <Widget>[
            SizedBox(height: 10,),
            TextField(
              controller: nickNameController,
              maxLines: 1,
              maxLength: 10,
              decoration: InputDecoration(
                labelText: '昵称',
              ),
            ),
            TextField(
              controller: genderController,
              maxLines: 1,
              maxLength: 6,
              decoration: InputDecoration(
                labelText: '性别',
              ),
            ),
            TextField(
              controller: birthDayController,
              maxLines: 1,
              maxLength: 10,
              decoration: InputDecoration(
                labelText: '生日',
              ),
            ),
            TextField(
              controller: currentResidenceController,
              maxLines: 1,
              maxLength: 50,
              decoration: InputDecoration(
                labelText: '现居住地址',
              ),
            ),
            TextField(
              controller: hometownController,
              maxLines: 1,
              maxLength: 50,
              decoration: InputDecoration(
                labelText: '家乡',
              ),
            ),
            SizedBox(height: 20,),
            CommonButton(
              callback: () {
                editUserInfo();
              },
              content: '提 交',
              //width: double.infinity,
            ),
          ],),
        ),
      ),
    );
  }


  //编辑基本信息
  editUserInfo() async {
    if(StringUtil.checkEmpty(nickNameController.text)){
      UIUtils.showToast("昵称不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(birthDayController.text)){
      UIUtils.showToast("生日不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(genderController.text)){
      UIUtils.showToast("性别不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(currentResidenceController.text)){
      UIUtils.showToast("现居住地址不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(hometownController.text)){
      UIUtils.showToast("家乡不能为空..");
      return;
    }

    String user_name = await LoginUtil.getLoginUserName();
    String nick_name = nickNameController.text;
    String gender= genderController.text;

    int year = int.parse(birthDayController.text.substring(0,4));
    int month = int.parse(birthDayController.text.substring(5,7));
    int day = int.parse(birthDayController.text.substring(8,10));
    String birthday = new DateTime(year,month,day).millisecondsSinceEpoch.toString();//转为毫秒传到后台

    String current_residence = currentResidenceController.text;
    String hometown = hometownController.text;
    String hat = "";
    String hat_in_use = "N";

    LinkKnownApi.UpdateUserDetail(user_name,nick_name,gender,birthday,current_residence,hometown,hat,hat_in_use).then((value) {
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


}


