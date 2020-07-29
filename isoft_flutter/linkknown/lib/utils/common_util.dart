
import 'package:linkknown/utils/shared_preference_util.dart';
import 'package:sprintf/sprintf.dart';

class CommonUtil {

  static recordSearchHistory (String text) async {
    List<String> history = await SharedPreferenceUtil.getList(SharedPreferenceUtil.COURSE_SEARCH_HISTORY);
    history = history ?? [];
    if (history.contains(text)) {
      history.remove(text);
    }
    history.insert(0, text);
    if (history.length > 20) {
      history = history.sublist(0, 20);
    }
    return SharedPreferenceUtil.saveList(SharedPreferenceUtil.COURSE_SEARCH_HISTORY, history);
  }

  static Future<List<String>> getSearchHistory () async {
    List<String> history = await SharedPreferenceUtil.getList(SharedPreferenceUtil.COURSE_SEARCH_HISTORY);
    return history??[];
  }

  static clearHistory () async {
    await SharedPreferenceUtil.remove(SharedPreferenceUtil.COURSE_SEARCH_HISTORY);
  }

  // 格式化视频时长 秒转分秒
  static String formateDuration(int duration) {
    if (duration <= 0){
      return "00:00";
    }
    int second = duration % 60;
    int minute = (duration / 60).toInt();
    return sprintf("%2d:%2d", [minute, second]);
  }

}
