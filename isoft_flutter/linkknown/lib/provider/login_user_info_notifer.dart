
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/model/login_user_response.dart';

class LoginUserInfoNotifer with ChangeNotifier {

  LoginUserResponse loginUserResponse;

  update (LoginUserResponse loginUserResponse) {
    this.loginUserResponse = loginUserResponse;

    notifyListeners();
  }

  logout () {
    loginUserResponse = null;
    notifyListeners();
  }
}