import 'dart:ui';

import 'package:city_pickers/city_pickers.dart';
import 'package:city_pickers/modal/result.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/utils/date_util.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';

class UserInfoPage extends StatefulWidget {
  @override
  _UserInfoPageState createState() => _UserInfoPageState();
}

class _UserInfoPageState extends State<UserInfoPage> {

  TextEditingController nickNameController;
  TextEditingController birthDayController;
  TextEditingController currentResidenceController;
  TextEditingController hometownController;
  String _gender = "";

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
          _gender = value.user.gender;
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
              controller: birthDayController,
              maxLines: 1,
              maxLength: 10,
              readOnly: true,
              decoration: InputDecoration(
                labelText: '生日',
              ),
              onTap: (){
                int year = int.parse(birthDayController.text.substring(0,4));
                int month = int.parse(birthDayController.text.substring(5,7));
                int day = int.parse(birthDayController.text.substring(8,10));
                showDatePicker(
                  context: context,
                  initialDate: new DateTime(year,month,day), //默认日期
                  firstDate: DateTime(1900), //选择最早的日期范围
                  lastDate: DateTime(3020), //最晚的日期范围
                  locale: Locale('zh', 'CH'),
                ).then((DateTime selectedDateTime) {
                  setState(() {
                    birthDayController = new TextEditingController(text: selectedDateTime.toString().substring(0,10));
                  });
                }).catchError((err) {
                  print(err);
                });
              },
            ),
            TextField(
              controller: currentResidenceController,
              maxLines: 1,
              maxLength: 50,
              readOnly: true,
              decoration: InputDecoration(
                labelText: '现居住地址',
              ),
              onTap: (){
                showCurrentAddressDialog(context);
              },
            ),
            TextField(
              controller: hometownController,
              maxLines: 1,
              maxLength: 50,
              readOnly: true,
              decoration: InputDecoration(
                labelText: '家乡',
              ),
              onTap: (){
                showHomeTownAddressDialog(context);
              },
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Radio(
                  value: "male",
                  activeColor: Colors.red,
                  groupValue: this._gender,
                  onChanged: (value) {
                    setState(() {
                      this._gender = value;
                    });
                  },
                ),
                Text('男'),
                SizedBox(width: 10,),
                Radio(
                  value: "female",
                  activeColor: Colors.red,
                  groupValue: this._gender,
                  onChanged: (value) {
                    setState(() {
                      this._gender = value;
                    });
                  },
                ),
                Text('女'),
              ],
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


  //现居住地址选择弹框
  showCurrentAddressDialog(context) async {
    Result temp = await CityPickers.showCityPicker(
      context: context,
      itemExtent: ScreenUtil().setHeight(100),
      itemBuilder: (item, list, index) {
        return Center(child: Text(item,
                maxLines: 1,
                style: TextStyle(fontSize: ScreenUtil().setSp(26.0))
        ));
      },
      height: ScreenUtil().setHeight(500),
    );

    setState(() {
      //UIUtils.showToast(temp.provinceName + temp.cityName + temp.areaName); //temp.areaId 不需要id
      currentResidenceController = new TextEditingController(text: temp.provinceName +"-"+ temp.cityName +"-"+ temp.areaName);
    });
  }


  //家乡地址选择弹框
  showHomeTownAddressDialog(context) async {
    Result temp = await CityPickers.showCityPicker(
      context: context,
      itemExtent: ScreenUtil().setHeight(100),
      itemBuilder: (item, list, index) {
        return Center(child: Text(item,
            maxLines: 1,
            style: TextStyle(fontSize: ScreenUtil().setSp(26.0))
        ));
      },
      height: ScreenUtil().setHeight(500),
    );

    setState(() {
      //UIUtils.showToast(temp.provinceName + temp.cityName + temp.areaName); //temp.areaId 不需要id
      hometownController = new TextEditingController(text: temp.provinceName +"-"+ temp.cityName +"-"+ temp.areaName);
    });
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

    if(StringUtil.checkEmpty(currentResidenceController.text)){
      UIUtils.showToast("现居住地址不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(hometownController.text)){
      UIUtils.showToast("家乡不能为空..");
      return;
    }

    if(StringUtil.checkEmpty(_gender)){
      UIUtils.showToast("性别不能为空..");
      return;
    }

    String user_name = await LoginUtil.getLoginUserName();
    String nick_name = nickNameController.text;

    int year = int.parse(birthDayController.text.substring(0,4));
    int month = int.parse(birthDayController.text.substring(5,7));
    int day = int.parse(birthDayController.text.substring(8,10));
    String birthday = new DateTime(year,month,day).millisecondsSinceEpoch.toString();//转为毫秒传到后台

    String current_residence = currentResidenceController.text;
    String hometown = hometownController.text;
    String gender= _gender;
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

  @override
  void dispose() {
    nickNameController?.dispose();
    birthDayController?.dispose();
    currentResidenceController?.dispose();
    hometownController?.dispose();
    super.dispose();
  }

}