import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:linkknown/config/env_config.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/route/routes.dart';
import 'package:provider/provider.dart';

import 'application.dart';

// Flutter官方自带的splash启动页是在android或者ios的文件里面设置，但是不能添加倒计时之类的效果，自己做启动页需要全屏效果显示的时候，
// 调用SystemChrome.setEnabledSystemUIOverlays([]); 这个方法把状态栏和虚拟按键隐藏掉，跳转到其他页面后需要调用
// SystemChrome.setEnabledSystemUIOverlays([SystemUiOverlay.top]);把状态栏显示出来，需要一起调用底部虚拟按键（华为系列某些手机有虚拟按键），
// 则SystemChrome.setEnabledSystemUIOverlays([SystemUiOverlay.top, SystemUiOverlay.bottom]);

//  SystemChrome.setEnabledSystemUIOverlays([]); // 隐藏
//  SystemChrome.setEnabledSystemUIOverlays([SystemUiOverlay.bottom]);  // 显示底部
//  SystemChrome.setEnabledSystemUIOverlays(SystemUiOverlay.values); //恢复
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
