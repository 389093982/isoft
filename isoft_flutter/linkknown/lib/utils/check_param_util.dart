
import 'package:linkknown/utils/string_util.dart';

// 参数校验工具类
class CheckParamUtil {

  static final String REGEX_EMAIL = r'^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$';
  static final String REGEX_PHONE = r'^[1][3,4,5,7,8][0-9]{9}$';
  static final String REGEX_PASSWD = r'^[a-zA-Z0-9]{6,20}$';

  static bool checkRegex (String checkStr, String regex) {
    if (StringUtil.checkNotEmpty(checkStr)) {
      RegExp exp = RegExp(regex);
      bool matched = exp.hasMatch(checkStr);
      return matched;
    }
    return false;
  }


}