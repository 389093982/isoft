

import 'package:shared_preferences/shared_preferences.dart';

class SharedPreferenceUtil {

  // 异步保存
  static Future save(String key, String value) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    sp.setString(key, value);
  }

  // 异步读取
  static Future<String> get(String key) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.getString(key);
  }

  // 异步读取
  static Future<bool> remove(String key) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.remove(key);
  }

}