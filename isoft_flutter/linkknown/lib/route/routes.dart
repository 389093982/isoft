// 路由类
import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/route/reoutes_handler.dart';

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

    router.define(root, handler: splashHandler);
    router.define(main, handler: mainHandler);
    router.define(login, handler: loginHandler);
    router.define(regist, handler: registHandler);
    // 云博客页
    router.define(cloudBlog, handler: cloudBlogHandler);
    // 个人中心页
    router.define(personalCenter, handler: personalCenterHandler);
    // 已购课程页
    router.define(buyCourse, handler: buyCourseHandler);
    // 我要吐槽页
    router.define(advise, handler: adviseHandler);
    // 关于链知页
    router.define(about, handler: aboutHandler);

    // 课程详情页
    router.define(courseDetail, handler: courseDetailHandler);
  }
}
