import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/refresh_token_response.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/navigator_util.dart';
import 'package:linkknown/utils/string_util.dart';
import 'package:linkknown/utils/utils.dart';

class AutoLoginDialogHelper {

  // 用于对话框弹框去重
  static bool isShowDialogFlag = false;

  // 根据错误码弹框提示前去登录
  static bool checkCanShowUnLoginDialog(BuildContext context, dynamic err) {
    if (err is LinkKnownError &&
        (err as LinkKnownError).errorCode == 401 &&
        isShowDialogFlag == false) {
      isShowDialogFlag = true;

      showRefreshOrConfirmDialog(context);

      // 防止异常导致  isShowDialogFlag 没有设置为 false
      Future.delayed(Duration(seconds: 10), () {
        isShowDialogFlag = false;
      });
    }
  }

  // 主动弹框提示前去登录
  static bool openUnLoginDialog(BuildContext context) {
    isShowDialogFlag = true;

    showRefreshOrConfirmDialog(context);

    // 防止异常导致  isShowDialogFlag 没有设置为 false
    Future.delayed(Duration(seconds: 10), () {
      isShowDialogFlag = false;
    });
  }

  static void showRefreshOrConfirmDialog(BuildContext context) async {
    if (await LoginUtil.checkCanRefreshToken(context)) {
      _showAutoRefreshLoginDialog(context);
    } else {
      _showUnLoginDialog(context);
    }
  }

  static void _showAutoRefreshLoginDialog(BuildContext context) async {
    RefreshTokenResponse refreshTokenResponse =
    await LinkKnownApi.refreshToken(await LoginUtil.getTokenString());
    if (refreshTokenResponse.status == "SUCCESS") {
      LoginUtil.memoryRefreshToken(context, refreshTokenResponse.tokenString,
          int.parse(refreshTokenResponse.expireSecond));

      showDialog(
          context: context,
          builder: (context) {
            return RefreshTokenWidget(
              closeVoidCallback: () {
                isShowDialogFlag = false;
                Navigator.of(context, rootNavigator: true).pop();
              },
            );
          });
    } else {
      // 自动刷新失败
      _showUnLoginDialog(context);
    }
  }

  static void _showUnLoginDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) {
        // 用Scaffold返回显示的内容，能跟随主题
        return Scaffold(
          backgroundColor: Colors.transparent, // 背景透明
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
                      child: Text(
                        "温馨提示",
                        style: TextStyle(color: Colors.white, fontSize: 20),
                      ),
                    ),
                  ),
                  Container(
                    height: 80,
                    child: Center(
                      child: Text(
                        "您还未登录，前去登录？",
                        style: TextStyle(color: Colors.black, fontSize: 20),
                      ),
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
                            child: Text(
                              "取消",
                              style:
                              TextStyle(color: Colors.black, fontSize: 20),
                            ),
                            onPressed: () {
                              isShowDialogFlag = false;

                              Navigator.of(context, rootNavigator: true).pop();
                            },
                          ),
                          SizedBox(
                            width: 50,
                          ),
                          MaterialButton(
                            color: Colors.red,
                            child: Text(
                              "确认",
                              style:
                              TextStyle(color: Colors.white, fontSize: 20),
                            ),
                            onPressed: () {
                              isShowDialogFlag = false;

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

class RefreshTokenWidget extends StatefulWidget {
  VoidCallback closeVoidCallback;

  RefreshTokenWidget({this.closeVoidCallback});

  @override
  State<StatefulWidget> createState() {
    return _RefreshTokenState();
  }
}

class _RefreshTokenState extends State<RefreshTokenWidget>
    with TickerProviderStateMixin {
  String infoText = "自动登录中...";
  String headerIcon;

  // 旋转动画控制器
  AnimationController rotationAnimationController;
  Animation rotationAnimation;

  @override
  void initState() {
    super.initState();

    rotationAnimationController =
        AnimationController(duration: const Duration(seconds: 2), vsync: this);
    rotationAnimation =
        Tween(begin: 0.0, end: 1.0).animate(rotationAnimationController);
    rotationAnimationController.forward();

    rotationAnimationController.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        setState(() {
          infoText = "登录成功！请刷新重试";
        });

        Future.delayed(Duration(seconds: 1), () {
          widget.closeVoidCallback();
        });
      }
    });

    LoginUtil.getSmallIcon().then((_headerIcon) {
      setState(() {
        headerIcon = _headerIcon;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    // 用Scaffold返回显示的内容，能跟随主题
    return Scaffold(
      backgroundColor: Colors.transparent, // 背景透明
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
                  child: Text(
                    infoText,
                    style: TextStyle(color: Colors.white, fontSize: 20),
                  ),
                ),
              ),
              Container(
                height: 150,
                child: Center(
                  child: RotationTransition(
                    turns: rotationAnimation,
                    child: ClipOval(
                      child: StringUtil.checkNotEmpty(headerIcon)
                          ? Image.network(
                        UIUtils.replaceMediaUrl(headerIcon),
                        width: 80,
                        height: 80,
                        fit: BoxFit.fill,
                      )
                          : Image.asset(
                        "images/linkknown.jpg",
                        width: 80,
                        height: 80,
                        fit: BoxFit.fill,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ), // 自带loading效果，需要宽高设置可在外加一层sizedbox，设置宽高即可
      ),
    );
  }

  @override
  void dispose() {
    rotationAnimationController?.dispose();
    super.dispose();
  }
}