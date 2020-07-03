import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/paginator.dart';

class CourseMetaResponse extends BaseResponse {
  List<CourseMeta> courses;
  Paginator paginator;
}

class CourseMeta {
  int id;
  String course_name;
  String course_author;
  String course_type;
  String small_image;
  String course_sub_type;
  String course_short_desc;
  int course_number;
  int watch_number;
  String course_label;
  String isCharge;
  String custom_tag;
  String custom_tag_name;
  String price;
  String old_price;
  String is_show_old_price;
}
