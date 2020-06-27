import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/home_page.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/page/main_page.dart';
import 'package:linkknown/page/splash_page.dart';


// splash 页面
var splashHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<Object>> params) {
      return SplashPage();
    });

// 登录页
var mainHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<Object>> params) {
      return MainPage();
    });

// 登录页
var loginHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<Object>> params) {
      return LoginPage();
    });

