import 'package:flutter/material.dart';

/// Flutter 在 Android 端的滚动视图，比如 SingleChildScrollView 和 ListView 等，如果不加处理，
/// 在拉到顶或者拉到底的时候，是有一个带有 accentColor 颜色的水波纹效果的，同时 iOS 端是一个软性回弹效果，
/// 如果想在 iPhone 上保留 iOS 的效果，但是 Android 端去除这个效果，可以按以下操作：
/// 自定义一个 NoShadowScrollBehavior ，代码很简单，利用 switch 的穿透，在苹果设备上原样返回子 Widget ，
/// 而其他平台，包括 Android ，将 showLeading 和 showTrailing 设置为 false ，然后用 ScrollConfiguration
/// 包裹 ScrollView 或者 ListView，在 behavior 里将上方的 NoShadowScrollBehavior 赋值即可：
class NoShadowScrollBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(BuildContext context, Widget child, AxisDirection axisDirection) {
    switch (getPlatform(context)) {
      case TargetPlatform.iOS:
      case TargetPlatform.macOS:
        return child;
      case TargetPlatform.android:
      case TargetPlatform.fuchsia:
      case TargetPlatform.linux:
      case TargetPlatform.windows:
        return GlowingOverscrollIndicator(
          child: child,
          //不显示头部水波纹
          showLeading: false,
          //不显示尾部水波纹
          showTrailing: false,
          axisDirection: axisDirection,
          color: Theme.of(context).accentColor,
        );
    }
    return null;
  }
}