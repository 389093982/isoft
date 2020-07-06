
import 'package:dio/dio.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/element.dart';
import 'package:linkknown/model/user.dart';

class LinkKnownApi {
  static Dio _dio;
  static final String baseUrl = 'http://192.168.1.11:6001';

  static void init() async {
    _dio = Dio(BaseOptions(
        baseUrl: baseUrl,
        followRedirects: false,
      //连接服务器超时时间，单位是毫秒.
      connectTimeout: 10000,
      headers: {
        "client_type": "app",
      },
    ))
      ..interceptors.add(LogInterceptor(responseBody: true, requestBody: true));
  }

  static Future<Response> doPost(
    String url, {
    Map<String, dynamic> params,
  }) async {
    try {
      return await _dio.post(url, queryParameters: params);
    } on DioError catch (e) {
      if (e != null && e.response != null && e.response.statusCode == 401) {
        throw LinkKnownError.unAuthorizedLogin();
      } else {
        throw LinkKnownError(-1, e.message);
      }
    }
  }

  // 登录
  static Future<LoginUserResponse> postLogin(String username, String passwd, String redirectUrl) async {
    var response = await doPost('/api/iwork/httpservice/isoft_linkknown_api/PostLogin', params: {
      'username': username,
      'passwd': passwd,
      'redirectUrl': redirectUrl,
    });
    return LoginUserResponse.fromJson(response.data);
  }


  // 课程搜索接口
  static Future<CourseMetaResponse> searchCourseList(String search, String isCharge, int current_page, int offset) async {
    var response = await doPost('/api/iwork/httpservice/isoft_linkknown_api/SearchCourseList', params: {
      'search': search,
      'isCharge': isCharge,
      'current_page': current_page,
      'offset': offset,
    });
    return CourseMetaResponse.fromJson(response.data);
  }

  // 查询课程详情
  static Future<CourseDetailResponse> showCourseDetailForApp(int course_id, String user_name) async {
    var response = await doPost('/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetailForApp', params: {
      'course_id': course_id,
      'user_name': user_name,
    });
    return CourseDetailResponse.fromJson(response.data);
  }

  // 占位符查询
  static Future<ElementResponse> filterElementByPlacement(String placement) async {
    var response = await doPost('/api/iwork/httpservice/isoft_linkknown_api/FilterElementByPlacement', params: {
      'placement': placement,
    });
    return ElementResponse.fromJson(response.data);
  }
}
