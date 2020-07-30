
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:linkknown/utils/utils.dart';

/// 双击返回退出
class DoubleTapBackExitApp extends StatefulWidget {

  const DoubleTapBackExitApp({
    Key key,
    @required this.child,
    this.duration = const Duration(milliseconds: 2500),
  }): super(key: key);

  final Widget child;
  /// 两次点击返回按钮的时间间隔
  final Duration duration;

  @override
  _DoubleTapBackExitAppState createState() => _DoubleTapBackExitAppState();
}

class _DoubleTapBackExitAppState extends State<DoubleTapBackExitApp> {

  DateTime  _lastTime;

  @override
  Widget build(BuildContext context) {
    // 在fultter 中有个控件专门拦截返回导航键，这个控件就是WillPopScope
    // onWillPop是一个回调函数，在点击返回键的时候触发，单击导航退出app就可以通过这个控件来控制
    return WillPopScope(
      onWillPop: _isExit,
      child: widget.child,
    );
  }

  Future<bool> _isExit() async {
    if (_lastTime == null || DateTime.now().difference(_lastTime) > widget.duration) {
      _lastTime = DateTime.now();
      UIUtils.showToast2('再次点击退出应用');
      return Future.value(false);
    }
    // 退出应用
    await SystemNavigator.pop();
    return Future.value(true);
  }
}

