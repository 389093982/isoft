
import 'package:linkknown/utils/shared_preference_util.dart';
import 'package:sprintf/sprintf.dart';
import 'dart:convert';

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

  // 记录课程播放记录
  static void recordVideoPlayHistory(int courseId, int videoId, {int progress}) async {
    CourseVideoCurrentPlaying playing = CourseVideoCurrentPlaying(courseId: courseId, videoId: videoId, progress: progress);
    await SharedPreferenceUtil.save(SharedPreferenceUtil.COURSE_HISTORY_PLAYING, jsonEncode(playing.toJson()));
    // 根据 id 获取观看记录
    List<String> videoIds = await SharedPreferenceUtil.getList(SharedPreferenceUtil.COURSE_HISTORY_PLAYED + "${courseId}");
    videoIds = videoIds??[];
    videoIds..add(videoId.toString())..toSet()..toList();
    // 重新存储进去
    await SharedPreferenceUtil.saveList(SharedPreferenceUtil.COURSE_HISTORY_PLAYED + "${courseId}", videoIds);
  }

  // 读取课程播放记录
  static Future<List<String>> readVideoPlayHistory(int courseId) async {
    // 根据 id 获取观看记录
    List<String> videoIds = await SharedPreferenceUtil.getList(SharedPreferenceUtil.COURSE_HISTORY_PLAYED + "${courseId}");
    return videoIds??[];
  }

  // 读取正在播放的课程信息
  static Future<CourseVideoCurrentPlaying> readVideoPlaying() async {
    // 根据 id 获取观看记录
    String playing = await SharedPreferenceUtil.get(SharedPreferenceUtil.COURSE_HISTORY_PLAYING);
    return CourseVideoCurrentPlaying.fromJson(jsonDecode(playing));
  }

}

class CourseVideoCurrentPlaying {
  int courseId;
  int videoId;
  int progress;     // 播放进度,单位秒,用于下次继续播放

  CourseVideoCurrentPlaying(
      {this.courseId,
        this.videoId,
        this.progress});

  CourseVideoCurrentPlaying.fromJson(Map<String, dynamic> json) {
    courseId = json['courseId'];
    videoId = json['videoId'];
    progress = json['progress'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['courseId'] = this.courseId;
    data['videoId'] = this.videoId;
    data['progress'] = this.progress;
    return data;
  }
}