import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:linkknown/config/env_config.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/route/routes.dart';
import 'package:provider/provider.dart';

import 'application.dart';

void runAppWithConfig(EnvConfig config) {
  // 初始化配置
  LinkKnownConfig.config = config;

  Router router = Router();
  Routes.configureRoutes(router);
  Application.router = router;

//  SystemChrome.setSystemUIOverlayStyle(
//    SystemUiOverlayStyle(statusBarColor: Colors.transparent),
//  );

  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light.copyWith(
    statusBarColor: Colors.red,
  ));

  //打开主页
  // Provider不止提供了ChangeNotifierProvider，还有 Provider,ListenableProvider,ValueListenableProvider,StreamProvider,
  // 如果想管理多个对象可以用 MultiProvider,如下
  runApp(new MultiProvider(
    providers: [
      new ChangeNotifierProvider<UserModel>(
        create: (_) => UserModel(),
      ),
    ],
    child: new MyApp(),
  ));
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      debugShowCheckedModeBanner: true,
      title: '链知课堂',
      theme: ThemeData(
        primaryColor: Colors.red,
        // 全局背景颜色 scaffoldBackgroundColor
        // 局部页面背景颜色使用 Scaffold 的 background 属性
        scaffoldBackgroundColor: Colors.grey[50],
      ),

      // home 指定为开屏页
      home: new SplashPage(),
      onGenerateRoute: Application.router.generator,
    );
  }
}
