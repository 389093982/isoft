import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/styles/textstyles.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/fileupload_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/utils.dart';
import 'package:linkknown/widgets/common_button.dart';
import 'package:linkknown/widgets/image_select.dart';

class ModifyHeaderPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => ModifyHeaderPageState();
}

class ModifyHeaderPageState extends State<ModifyHeaderPage> {

  String filePath;

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
            Row(
              children: <Widget>[
                Expanded(
                  child:
                  ImageSelectWidget(
                    label: "从相册中获取",
                    source: ImageSelectWidget.SOURCE_GALLERY,
                    onSelect: (_filePath){
                      filePath = _filePath;
                    },
                  ),
                ),
                Expanded(
                  child: ImageSelectWidget(
                    label: "拍照上传头像",
                    source: ImageSelectWidget.SOURCE_CAMERA,
                    onSelect: (_filePath){
                      filePath = _filePath;
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 20,),
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
    FileUploadResponse fileUploadResponse = await LinkKnownApi.fileUpload("user", "small_icon", filePath);
    if (fileUploadResponse.status == "SUCCESS") {
      String userName = await LoginUtil.getUserName();
      BaseResponse baseResponse = await LinkKnownApi.updateUserIcon(userName, fileUploadResponse.fileServerPath);
      if (baseResponse.status == "SUCCESS") {
        UIUtils.showToast2("设置成功！");
      } else {
        UIUtils.showToast2("设置失败！");
      }
    } else {
      UIUtils.showToast2("文件上传失败！");
    }
  }
}
