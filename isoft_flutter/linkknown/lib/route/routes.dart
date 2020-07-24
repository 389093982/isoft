
import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/route/reoutes_handler.dart';

// 路由类
class Routes {
  //====================【基础路由】====================
  static String root = "/";
  static String main = "/main";
  static String login = "/login";
  static String regist = "/regist";


  //====================【首页课程相关】====================
  // 课程详情页
  static String courseDetail = "/courseDetail";
  // 课程搜索页
  static String courseSearch = "/courseSearch";
  // 视频播放界面
  static String videoPlay = "/videoPlay";
  // customTag 课程搜索页
  static String customTagCourse = "/customTagCoursePage";



  //====================【分类】====================



  //====================【发现】====================



  //====================【我的】====================
  //优惠券
  static String myCoupon = "/myCoupon";
  //购物车
  static String shoppingCart = "/shoppingCart";
  //订单
  static String payOrder = "/payOrder";
  //活动中心
  static String huodong = "/huodong";
  //考试
  static String kaoshi = "/kaoshi";

  // 云博客
  static String cloudBlog = "/cloudBlog";
  //编辑博客
  static String editBlog = "/editBlog";
  //博客详情
  static String blogDetail = "/blogDetail";
  // 个人中心
  static String personalCenter = "/personalCenter";
  // 已购课程页面
  static String boughtCourse = "/boughtCourse";
  // 我要吐槽（意见、建议）
  static String advise = "/advise";
  // 意见编辑页
  static String adviseEdit = "/adviseEdit";
  // 关于链知
  static String about = "/about";
  //我与链知
  static String linkknownWithMe = "/linkKnownWithMe";
  // 用户协议
  static String userAgreement = "/userAgreement";
  // 商业合作
  static String business = "/business";
  // 消息中心
  static String message = "/message";
  // 设置
  static String setting = "/setting";
  // 敬请期待页面
  static String comingSoon = "/comingSoon";


  static void configureRoutes(Router router) {
    router.notFoundHandler = new Handler(
        handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      print("ROUTE WAS NOT FOUND !!!");
      return LoginPage();
    });

    //====================【基础路由】====================
    router.define(root, handler: splashHandler);
    router.define(main, handler: mainHandler);
    router.define(login, handler: loginHandler);
    router.define(regist, handler: registHandler);

    //====================【首页课程相关】====================
    // 课程详情页
    router.define(courseDetail, handler: courseDetailHandler);
    // 课程搜索页
    router.define(courseSearch, handler: courseSearchHandler);
    // 视频播放界面
    router.define(videoPlay, handler: videoPlayHandler);
    // customTag 课程搜索页
    router.define(customTagCourse, handler: customTagCourseHandler);

    //====================【分类】====================



    //====================【发现】====================



    //====================【我的】====================
    // 优惠券
    router.define(myCoupon, handler: myCouponHandler);
    // 购物车
    router.define(shoppingCart, handler: shoppingCartHandler);
    // 订单
    router.define(payOrder, handler: payOrderHandler);
    // 活动
    router.define(huodong, handler: huodongHandler);
    // 考试
    router.define(kaoshi, handler: kaoshiHandler);
    // 云博客页
    router.define(cloudBlog, handler: cloudBlogHandler);
    //编辑博客
    router.define(editBlog, handler: editBlogHandler);
    //博客详情
    router.define(blogDetail, handler: BlogDetailHandler);
    // 个人中心页
    router.define(personalCenter, handler: personalCenterHandler);
    // 已购课程页
    router.define(boughtCourse, handler: boughtCourseHandler);
    // 我要吐槽页
    router.define(advise, handler: adviseHandler);
    router.define(adviseEdit, handler: adviseEditHandler);
    // 关于链知页
    router.define(about, handler: aboutHandler);
    //我与链知
    router.define(linkknownWithMe, handler: linkknownWithMeHandler);
    // 用户协议
    router.define(userAgreement, handler: userAgreementHandler);
    // 商业合作
    router.define(business, handler: businessHandler);
    // 消息中心
    router.define(message, handler: messageHandler);
    // 设置
    router.define(setting, handler: settingHandler);
    // 敬请期待页面
    router.define(comingSoon, handler: comingSoonHandler);

  }
}
