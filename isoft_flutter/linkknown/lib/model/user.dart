import 'package:linkknown/model/base.dart';

class LoginUserResponse extends BaseResponse {
  String domain;
  String expireSecond;
  String headerIcon;
  String loginStatus;
  String loginSuccess;
  String nickName;
  String redirectUrl;
  String roleName;
  String tokenString;
  String userName;

  LoginUserResponse (
      {this.domain,
      this.expireSecond,
      this.headerIcon,
      this.loginStatus,
      this.loginSuccess,
      this.nickName,
      this.redirectUrl,
      this.roleName,
      this.tokenString,
      this.userName});

  LoginUserResponse.fromJson(Map<String, dynamic> json) {
    domain = json['domain'];
    errorMsg = json['errorMsg'];
    expireSecond = json['expireSecond'];
    headerIcon = json['headerIcon'];
    loginStatus = json['loginStatus'];
    loginSuccess = json['loginSuccess'];
    nickName = json['nickName'];
    redirectUrl = json['redirectUrl'];
    roleName = json['roleName'];
    status = json['status'];
    tokenString = json['tokenString'];
    userName = json['userName'];
  }

}
