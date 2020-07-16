
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/advise_edit_page.dart';
import 'package:linkknown/page/advise_page.dart';
import 'package:linkknown/page/business.dart';
import 'package:linkknown/page/course_detail.dart';
import 'package:linkknown/page/course_search.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/page/main_page.dart';
import 'package:linkknown/page/message_page.dart';
import 'package:linkknown/page/mine_about.dart';
import 'package:linkknown/page/my_coupon_page.dart';
import 'package:linkknown/page/personal_center_page.dart';
import 'package:linkknown/page/regist_page.dart';
import 'package:linkknown/page/setting_page.dart';
import 'package:linkknown/page/shopping_cart_page.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/page/user_agreement.dart';
import 'package:linkknown/page/video_play.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';

// 临时存储复杂的参数
var routerParamMap = Map();

// splash 页面
var splashHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return SplashPage();
    });

// 登录页
var mainHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return MainPage();
    });

// 登录页
var loginHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return LoginPage();
    });

// 注册页
var registHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return RegistPage();
    });

// 关于链知页
var aboutHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AboutPage();
    });

// 课程详情页
var courseDetailHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      int course_id = int.parse(params['course_id'].first);
      return CourseDetailPage(course_id);
    });

// 课程搜索页
var courseSearchHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String search = params['search'].first;
      String isCharge = params['isCharge'].first;
      return CourseSearchPage(FluroConvertUtil.fluroCnParamsDecode(search), isCharge);
    });

// 视频播放界面
var videoPlayHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String index = params['index'].first;       // 播放当前视频的索引
      return VideoPlayPage(routerParamMap["videoplay_courseKey"], routerParamMap["videoplay_cVideosKey"], int.parse(index));
    });

//优惠券
var myCouponHandler = new Handler(
  handlerFunc: (BuildContext context, Map<String,List<String>> params){
    return MyCouponPage();
  });

//购物车
var shoppingCartHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return ShoppingCartPage();
    });

//订单
var payOrderHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return RegistPage();
    });

//活动
var huodongHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return RegistPage();
    });

//考试
var kaoshiHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return RegistPage();
    });

// 云博客页
var cloudBlogHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return RegistPage();
    });

// 个人中心页
var personalCenterHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return PersonalCenterPage();
    });

// 已购课程页
var boughtCourseHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return RegistPage();
    });

// 我要吐槽页
var adviseHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AdvisePage();
    });

var adviseEditHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AdviseEditPage();
    });

//我与链知
var linkKnownWithMeHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return RegistPage();
    });

// 用户协议
var userAgreementHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return UserAgreementPage();
    });

// 商业合作
var businessHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return BusinessPage();
    });
