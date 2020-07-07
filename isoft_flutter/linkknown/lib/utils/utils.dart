
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';

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

  static String replaceMediaUrl (String url) {
    return url.replaceFirst("http://localhost:6001", "http://192.168.1.11:6001");
  }

  static bool isValidPrice (String price) {
    return double.parse(price) > 0;
  }

}
