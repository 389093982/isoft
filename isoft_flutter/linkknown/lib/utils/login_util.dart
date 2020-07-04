
import 'package:linkknown/model/user.dart';
import 'package:linkknown/utils/shared_preference_util.dart';
import 'package:linkknown/utils/string_util.dart';

class LoginUtil {

  static final String USER_NICK_NAME = "USER_NICK_NAME";
  static final String USER_USER_NAME = "USER_USER_NAME";
  static final String USER_PASSWD = "USER_PASSWD";
  static final String USER_EXPIRE_SECOND = "USER_EXPIRE_SECOND";
  static final String USER_TOKEN_STRING = "USER_TOKEN_STRING";

  static void memoryAccount (String userName, String passwd, LoginUserResponse user) {
    SharedPreferenceUtil.save(USER_NICK_NAME, user.nickName);
    SharedPreferenceUtil.save(USER_USER_NAME, userName);
    SharedPreferenceUtil.save(USER_PASSWD, passwd);
    int expireSecond = new DateTime.now().millisecondsSinceEpoch + int.parse(user.expireSecond) * 1000;
    SharedPreferenceUtil.save(USER_EXPIRE_SECOND, expireSecond.toString());
    SharedPreferenceUtil.save(USER_TOKEN_STRING, user.tokenString);
  }

  // 判断登录 tokenString 是否已经过期
  static Future<bool> checkHasExpired () async {
    int expireSecond = await getExpiredTime();
    return new DateTime.now().millisecondsSinceEpoch > expireSecond;
  }

  // 获取登录过期时间
  static Future<int> getExpiredTime() async {
    String expireSecond = await SharedPreferenceUtil.get(USER_EXPIRE_SECOND);
    return int.parse(expireSecond);
  }

  static Future<String> getUserName () async {
    return await SharedPreferenceUtil.get(USER_USER_NAME);
  }

  static Future<String> getPasswd () async {
    return await SharedPreferenceUtil.get(USER_PASSWD);
  }

  static Future<String> getTokenString () async {
    return await SharedPreferenceUtil.get(USER_TOKEN_STRING);
  }

  static Future<bool> checkHasLogin () async{
    String userName = await getUserName();
    String tokenString = await getTokenString();
    bool expired = await checkHasExpired();
    return StringUtil.checkNotEmpty(userName) && StringUtil.checkNotEmpty(tokenString) && !expired;
  }

  // 注出时只清除 tokenString,而不清除账号和密码
  static Future<bool> logout () async {
    return await SharedPreferenceUtil.remove(USER_TOKEN_STRING);
  }

}