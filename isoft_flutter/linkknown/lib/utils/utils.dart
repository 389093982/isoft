
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:linkknown/config/env_config.dart';

class UIUtils {
  static void showToast(String msg) {
    Fluttertoast.showToast(msg: msg, gravity: ToastGravity.CENTER);
  }

  static void showToast2(String msg) {
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        timeInSecForIos: 1,
        backgroundColor: Colors.deepOrangeAccent,
        textColor: Colors.white,
        fontSize: ScreenUtil().setSp(28.0));
  }

  static void showToast3(String msg) {
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.black54,
        textColor: Colors.white,
        fontSize: ScreenUtil().setSp(28.0));
  }

  static void cancelToast() {
    Fluttertoast.cancel();
  }

  static String replaceMediaUrl (String url) {
    return url.replaceFirst(LinkKnownConfig.config.hostApiBaseUrl, LinkKnownConfig.config.apiBaseUrl);
  }

  static bool isValidPrice (String price) {
    return double.parse(price) > 0;
  }

}
