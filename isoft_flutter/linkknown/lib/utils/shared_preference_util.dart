

import 'package:shared_preferences/shared_preferences.dart';

class SharedPreferenceUtil {

  static final String COURSE_SEARCH_HISTORY = "COURSE_SEARCH_HISTORY";

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

  // 异步读取
  static Future<List<String>> getList(String key) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.getStringList(key);
  }

  // 异步保存
  static Future saveList(String key, List<String> value) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    sp.setStringList(key, value);
  }
}