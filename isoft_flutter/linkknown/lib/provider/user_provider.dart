
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/user.dart';

class UserModel with ChangeNotifier {

  LoginUserResponse _user;

  LoginUserResponse get user => _user;

  /// 登录
  Future<LoginUserResponse> postLogin(BuildContext context, String username, String passwd, String redirectUrl) async {
    return LinkKnownApi.postLogin(context, username, passwd, redirectUrl);
  }

}