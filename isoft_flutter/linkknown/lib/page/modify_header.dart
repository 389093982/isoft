import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/fileupload_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/image_select.dart';

class ModifyHeaderPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => ModifyHeaderPageState();
}

class ModifyHeaderPageState extends State<ModifyHeaderPage> {

  String loginUserSamllIcon = "testUrl";
  String filePath;

  @override
  void initState() {
    super.initState();
    queryLoginUserSamllIcon();
  }

  queryLoginUserSamllIcon() async {
    String loginUserName = await LoginUtil.getLoginUserName();
    if(loginUserName!=null && loginUserName!=""){
      LinkKnownApi.getUserDetail(loginUserName).then((GetUserDetailResponse) {
        if (GetUserDetailResponse != null && GetUserDetailResponse.status == "SUCCESS") {
          if(StringUtil.checkNotEmpty(GetUserDetailResponse.user.smallIcon)){
            loginUserSamllIcon = GetUserDetailResponse.user.smallIcon;
          }
          setState(() {
          //刷新
          });
        }
      }).catchError((e) {
        UIUtils.showToast((e as LinkKnownError).errorMsg);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("点击图片更换头像"),
      ),
      body: Container(
        padding: EdgeInsets.all(60),
        child: ListView(
          children: <Widget>[
            SizedBox(height: 100,),
            Row(
              children: <Widget>[
                Expanded(
                  child: ImageSelectWidget(
                    label: "上传头像",
                    currentSmallIcon:loginUserSamllIcon,
                    onSelect: (_filePath){
                      filePath = _filePath;
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 40,),
            CommonButton(
              callback: modifyHeader,
              content: '提交',
              width: double.infinity,
            ),
          ],
        ),
      ),
    );
  }

  modifyHeader () async {
    if(StringUtil.checkEmpty(filePath)){
      UIUtils.showToast("没有更新头像..");
      return;
    }
    FileUploadResponse fileUploadResponse = await LinkKnownApi.fileUpload("user", "small_icon", filePath);
    if (fileUploadResponse.status == "SUCCESS") {
      String userName = await LoginUtil.getUserName();
      BaseResponse baseResponse = await LinkKnownApi.updateUserIcon(userName, fileUploadResponse.fileServerPath);
      if (baseResponse.status == "SUCCESS") {
        UIUtils.showToast2("设置成功！");
        NavigatorUtil.goBack(context);
      } else {
        UIUtils.showToast2("设置失败！");
      }
    } else {
      UIUtils.showToast2("文件上传失败！");
    }
  }
}
