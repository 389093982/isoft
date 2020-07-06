
class StringUtil {

  static bool checkNotEmpty (String str) {
    return !checkEmpty(str);
  }

  static bool checkEmpty (String str) {
    return str == null || str == "";
  }

  static List<String> splitLabel (String str) {
    str = str ?? "";
    List<String> result = [];
    str.replaceAll("|", "/").split("/").forEach((str) {
      if (checkNotEmpty(str)) {
        result.add(str);
      }
    });
    return result;
  }
}