
import 'package:flutter/material.dart';
import 'package:linkknown/api/linkknown_api.dart';

class CourseModel with ChangeNotifier {


  // 课程搜索接口
  Future<LoginUserResponse> searchCourseList(BuildContext context, String search, String isCharge, int current_page, int offset) async {
    return LinkKnownApi.searchCourseList(context, search, isCharge, current_page, offset);
  }

}