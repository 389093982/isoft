
class StringUtil {

  static bool checkNotEmpty (String str) {
    return !checkEmpty(str);
  }

  static bool checkEmpty (String str) {
    return str == null || str == "";
  }
}