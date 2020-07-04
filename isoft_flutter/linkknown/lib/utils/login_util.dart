
import 'package:linkknown/model/user.dart';
import 'package:linkknown/utils/shared_preference_util.dart';

class LoginUtil {

  static final String USER_NICK_NAME = "USER_NICK_NAME";
  static final String USER_USER_NAME = "USER_USER_NAME";
  static final String USER_PASSWD = "USER_PASSWD";

  static void memoryAccount (String userName, String passwd, LoginUserResponse user) {
    SharedPreferenceUtil.save(USER_NICK_NAME, user.nickName);
    SharedPreferenceUtil.save(USER_USER_NAME, userName);
    SharedPreferenceUtil.save(USER_PASSWD, passwd);
  }

  static Future<String> getUserName () async {
    return await SharedPreferenceUtil.get(USER_USER_NAME);
  }

  static Future<String> getPasswd () async {
    return await SharedPreferenceUtil.get(USER_PASSWD);
  }

}