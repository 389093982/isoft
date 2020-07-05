
// 路由类
import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:linkknown/page/course_detail.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/page/main_page.dart';
import 'package:linkknown/page/regist_page.dart';
import 'package:linkknown/page/splash_page.dart';

class Routes {
  static String root = "/";
  static String main = "/main";
  static String login = "/login";
  static String regist = "/regist";
  // 云博客
  static String cloudBlog = "/cloudBlog";
  // 个人中心
  static String personalCenter = "/personalCenter";
  // 已购课程页面
  static String buyCourse = "/buyCourse";
  // 我要吐槽（意见、建议）
  static String advise = "/advise";
  // 关于链知
  static String about = "/about";

  // 课程详情页
  static String courseDetail = "/courseDetail";

  static void configureRoutes(Router router) {
    router.notFoundHandler = new Handler(
        handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      print("ROUTE WAS NOT FOUND !!!");
      return LoginPage();
    });

    // splash 页
    bindRouterAndRouter(router, root, SplashPage());
    // 主页
    bindRouterAndRouter(router, main, MainPage());
    // 登录页
    bindRouterAndRouter(router, login, LoginPage());
    // 注册页
    bindRouterAndRouter(router, regist, RegistPage());
    // 云博客页
    bindRouterAndRouter(router, cloudBlog, RegistPage());
    // 个人中心页
    bindRouterAndRouter(router, personalCenter, RegistPage());
    // 已购课程页面页
    bindRouterAndRouter(router, buyCourse, RegistPage());
    // 我要吐槽页
    bindRouterAndRouter(router, advise, RegistPage());
    // 关于链知页
    bindRouterAndRouter(router, about, RegistPage());

    // 课程详情页
    bindRouterAndRouter(router, courseDetail, CourseDetailPage());
  }

  // 绑定路由和页面
  static bindRouterAndRouter (Router router, String path, Widget pageWidget) {
    var handler = new Handler(
        handlerFunc: (BuildContext context, Map<String, List<Object>> params) {
          return pageWidget;
        });
    router.define(path, handler: handler);
  }

}

