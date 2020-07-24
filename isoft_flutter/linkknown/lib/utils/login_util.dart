
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/model/login_user_response.dart';
import 'package:linkknown/utils/shared_preference_util.dart';
import 'package:linkknown/utils/string_util.dart';

class LoginUtil {

  static final String NICK_NAME = "NICK_NAME";
  static final String USER_NAME = "USER_NAME";
  static final String PASSWD = "PASSWD";
  static final String USER_EXPIRE_SECOND = "USER_EXPIRE_SECOND";
  static final String USER_TOKEN_STRING = "USER_TOKEN_STRING";
  static final String SMALL_ICON = "SMALL_ICON";
  static final String GENDER = "GENDER";
  static final String BIRTHDAY = "BIRTHDAY";
  static final String USER_POINTS = "USER_POINTS";
  static final String ROLE_NAME = "ROLE_NAME";
  static final String USER_SIGNATURE = "USER_SIGNATURE";
  static final String VIP_LEVEL = "VIP_LEVEL";
  static final String VIP_EXPIRED_TIME = "VIP_EXPIRED_TIME";
  static final String HAT = "HAT";
  static final String HAT_IN_USE = "HAT_IN_USE";
  static final String CURRENT_RESIDENCE = "CURRENT_RESIDENCE";
  static final String HOMETOWN = "HOMETOWN";
  static final String ATTENTION_COUNTS = "ATTENTION_COUNTS";
  static final String FENSI_COUNTS = "FENSI_COUNTS";

  //账户登录基本信息
  static void memoryAccount (String userName, String passwd, LoginUserResponse user) {
    SharedPreferenceUtil.save(NICK_NAME, user.nickName);
    SharedPreferenceUtil.save(SMALL_ICON, user.headerIcon);
    SharedPreferenceUtil.save(USER_NAME, userName);
    SharedPreferenceUtil.save(PASSWD, passwd);
    int expireSecond = new DateTime.now().millisecondsSinceEpoch + int.parse(user.expireSecond) * 1000;
    SharedPreferenceUtil.save(USER_EXPIRE_SECOND, expireSecond.toString());
    SharedPreferenceUtil.save(USER_TOKEN_STRING, user.tokenString);
  }

  //用户其他详细信息
  static void memoryUserDetail (GetUserDetailResponse getUserDetailResponse) {
    SharedPreferenceUtil.save(GENDER, getUserDetailResponse.user.gender);
    SharedPreferenceUtil.save(BIRTHDAY, getUserDetailResponse.user.birthday);
    SharedPreferenceUtil.save(USER_POINTS, getUserDetailResponse.user.userPoints.toString());
    SharedPreferenceUtil.save(ROLE_NAME, getUserDetailResponse.user.roleName);
    SharedPreferenceUtil.save(USER_SIGNATURE, getUserDetailResponse.user.userSignature);
    SharedPreferenceUtil.save(VIP_LEVEL, getUserDetailResponse.user.vipLevel.toString());
    SharedPreferenceUtil.save(VIP_EXPIRED_TIME, getUserDetailResponse.user.vipExpiredTime);
    SharedPreferenceUtil.save(HAT, getUserDetailResponse.user.hat);
    SharedPreferenceUtil.save(HAT_IN_USE, getUserDetailResponse.user.hatInUse);
    SharedPreferenceUtil.save(CURRENT_RESIDENCE, getUserDetailResponse.user.currentResidence);
    SharedPreferenceUtil.save(HOMETOWN, getUserDetailResponse.user.hometown);
    SharedPreferenceUtil.save(ATTENTION_COUNTS, getUserDetailResponse.user.attentionCounts.toString());
    SharedPreferenceUtil.save(FENSI_COUNTS, getUserDetailResponse.user.fensiCounts.toString());
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

  static Future<String> getPasswd () async {
    return await SharedPreferenceUtil.get(PASSWD);
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

  //以下是获取用户基本信息的方法
  static Future<String> getNickName () async {
    return await SharedPreferenceUtil.get(NICK_NAME);
  }

  static Future<String> getLoginUserName () async {
    bool hasLogin = await LoginUtil.checkHasLogin();
    if (hasLogin) {
      return await SharedPreferenceUtil.get(USER_NAME);
    }
    return "";
  }

  static Future<String> getUserName () async {
    return await SharedPreferenceUtil.get(USER_NAME);
  }

  static Future<String> getSmallIcon () async {
    return await SharedPreferenceUtil.get(SMALL_ICON);
  }

  static Future<String> getGender () async {
    return await SharedPreferenceUtil.get(GENDER);
  }

  static Future<String> getBirthday () async {
    return await SharedPreferenceUtil.get(BIRTHDAY);
  }

  static Future<String> getUserPoints () async {
    return await SharedPreferenceUtil.get(USER_POINTS);
  }

  static Future<String> getRoleName () async {
    return await SharedPreferenceUtil.get(ROLE_NAME);
  }

  static Future<String> getUserSignature () async {
    return await SharedPreferenceUtil.get(USER_SIGNATURE);
  }

  static Future<String> getVipLevel () async {
    return await SharedPreferenceUtil.get(VIP_LEVEL);
  }

  static Future<String> getVipExpiredTime () async {
    return await SharedPreferenceUtil.get(VIP_EXPIRED_TIME);
  }

  static Future<String> getHat () async {
    return await SharedPreferenceUtil.get(HAT);
  }

  static Future<String> getHatInUse () async {
    return await SharedPreferenceUtil.get(HAT_IN_USE);
  }

  static Future<String> getCurrentResidence () async {
    return await SharedPreferenceUtil.get(CURRENT_RESIDENCE);
  }

  static Future<String> getHomeTown () async {
    return await SharedPreferenceUtil.get(HOMETOWN);
  }

  static Future<String> getAttentionCounts () async {
    return await SharedPreferenceUtil.get(ATTENTION_COUNTS);
  }

  static Future<String> getFensiCounts () async {
    return await SharedPreferenceUtil.get(FENSI_COUNTS);
  }

}