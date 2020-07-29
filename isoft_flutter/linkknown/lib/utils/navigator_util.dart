import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/route/routes.dart';

import '../application.dart';

class NavigatorUtil {
  // 静态私有方法,无返回值
  static _navigateTo(BuildContext context, String path,
      // 默认值参数
      {Duration transitionDuration = const Duration(milliseconds: 250),
      RouteTransitionsBuilder transitionBuilder}) async {
    return await Application.router.navigateTo(context, path,
        transitionDuration: transitionDuration,
        transitionBuilder: transitionBuilder,
        transition: TransitionType.inFromRight);
  }

  // 返回
  static void goBack(BuildContext context) {
    Navigator.pop(context);
  }

  /// 登录页
  static void goLoginPage(BuildContext context) {
    _navigateTo(context, Routes.login);
  }

  /// 注册页
  static void goRegistPage(BuildContext context) {
    _navigateTo(context, Routes.regist);
  }

  /// 主页
  static void goMainPage(BuildContext context) {
    _navigateTo(context, Routes.main);
  }

  // 动态跳往页面
  static void goRouterPage(BuildContext context, String router) async {
    return _navigateTo(context, router);
  }
}
