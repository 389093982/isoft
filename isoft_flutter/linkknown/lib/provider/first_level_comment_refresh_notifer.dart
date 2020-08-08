
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';
import 'package:linkknown/response/login_user_response.dart';
import 'package:linkknown/response/query_page_blog_response.dart';

class FirstLevelCommentRefreshNotifer with ChangeNotifier {

  bool hasChanged = false;

  update (bool hasChanged) {
    this.hasChanged = hasChanged;
    notifyListeners();
  }

}