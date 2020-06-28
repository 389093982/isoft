import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/provider/user_provider.dart';
import 'package:linkknown/route/routes.dart';
import 'package:provider/provider.dart';

import 'application.dart';

void main() {
  Router router = Router();
  Routes.configureRoutes(router);
  Application.router = router;
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
      debugShowCheckedModeBanner: false,
      title: 'Welcome to Flutter',
      // home 指定为开屏页
      home: new SplashPage(),
      onGenerateRoute: Application.router.generator,
    );
  }
}