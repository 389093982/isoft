
class StringUtil {

  static bool checkNotEmpty (String str) {
    return str.isNotEmpty;
  }

  static bool checkEmpty (String str) {
    return str.isEmpty;
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


  static String getFileName (String filepath) {
    return filepath.substring(0, filepath.lastIndexOf("."));
  }
}