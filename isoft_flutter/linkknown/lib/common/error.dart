import 'package:flutter/material.dart';
import 'package:linkknown/utils/navigator_util.dart';

class LinkKnownError {
  int errorCode;
  String errorMsg;

  LinkKnownError(this.errorCode, this.errorMsg);

  LinkKnownError.unAuthorizedLogin({String msg = "UnAuthorizedLogin"}) {
    errorCode = 401;
    errorMsg = msg;
  }

  // 用于对话框弹框去重
  static bool isShowDialogFlag = false;

  static bool checkCanShowUnLoginDialog(BuildContext context, dynamic err) {
    if (err is LinkKnownError &&
        (err as LinkKnownError).errorCode == 401 &&
        isShowDialogFlag == false) {
      isShowDialogFlag = true;

      showUnLOginDialog(context);

      isShowDialogFlag = false;
    }
  }

  static void showUnLOginDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) {
        // 用Scaffold返回显示的内容，能跟随主题
        return Scaffold(
          backgroundColor: Colors.transparent,    // 背景透明
          body: Center(
            // 居中显示
            child: Container(
              color: Colors.white,
              width: MediaQuery.of(context).size.width / 1.3,
              height: 200,
              child: Column(
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Container(
                    height: 50,
                    color: Colors.red,
                    child: Center(
                      child: Text("温馨提示", style: TextStyle(color: Colors.white, fontSize: 20),),
                    ),
                  ),
                  Container(
                    height: 80,
                    child: Center(
                      child: Text("您还未登录，前去登录？", style: TextStyle(color: Colors.black, fontSize: 20),),
                    ),
                  ),
                  Container(
                    height: 70,
                    child: Center(
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: <Widget>[
                          MaterialButton(
                            color: Colors.grey[200],
                            child: Text("取消", style: TextStyle(color: Colors.black, fontSize: 20),),
                            onPressed: (){
                              Navigator.of(context, rootNavigator: true).pop();
                            },
                          ),
                          SizedBox(width: 50,),
                          MaterialButton(
                            color: Colors.red,
                            child: Text("确认", style: TextStyle(color: Colors.white, fontSize: 20),),
                            onPressed: (){
                              Navigator.of(context, rootNavigator: true).pop();
                              NavigatorUtil.goLoginPage(context);
                            },
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ), // 自带loading效果，需要宽高设置可在外加一层sizedbox，设置宽高即可
          ),
        );
      },
    );
  }
}
