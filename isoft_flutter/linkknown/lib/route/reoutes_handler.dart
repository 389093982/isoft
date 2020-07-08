import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/advise_edit_page.dart';
import 'package:linkknown/page/advise_page.dart';
import 'package:linkknown/page/course_detail.dart';
import 'package:linkknown/page/course_search.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/page/main_page.dart';
import 'package:linkknown/page/personal_center_page.dart';
import 'package:linkknown/page/regist_page.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';


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
var buyCourseHandler = new Handler(
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

// 关于链知页
var aboutHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return RegistPage();
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
