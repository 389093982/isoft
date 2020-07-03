
// 路由类
import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/route/route_handles.dart';

class Routes {
  static String root = "/";
  static String main = "/main";
  static String home = "/home";
  static String login = "/login";
  static String regist = "/regist";

  static void configureRoutes(Router router) {
    router.notFoundHandler = new Handler(
        handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      print("ROUTE WAS NOT FOUND !!!");
      return LoginPage();
    });
    router.define(root, handler: splashHandler);
    router.define(main, handler: mainHandler);
    router.define(login, handler: loginHandler);
    router.define(regist, handler: registHandler);
  }
}
