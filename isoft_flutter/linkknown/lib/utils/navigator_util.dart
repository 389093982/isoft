import 'dart:io';

import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/route/routes.dart';

import '../application.dart';

class NavigatorUtil {

  // 静态私有方法,无返回值
  static _navigateTo(BuildContext context, String path,
      // 默认值参数
      {bool replace = false,
      bool clearStack = false,
      Duration transitionDuration = const Duration(milliseconds: 250),
      RouteTransitionsBuilder transitionBuilder}) {
    Application.router.navigateTo(context, path,
        replace: replace,
        clearStack: clearStack,
        transitionDuration: transitionDuration,
        transitionBuilder: transitionBuilder,
        transition: TransitionType.material);
  }

  /// 登录页
  static void goLoginPage(BuildContext context) {
    _navigateTo(context, Routes.login, clearStack: true);
  }

  /// 注册页
  static void goRegistPage(BuildContext context) {
    _navigateTo(context, Routes.regist, clearStack: true);
  }

  /// 主页
  static void goMainPage(BuildContext context) {
    _navigateTo(context, Routes.main, clearStack: true);
  }

  // 动态跳往页面
  static void goRouterPage(BuildContext context, String router) {
    _navigateTo(context, router, clearStack: true);
  }

}
