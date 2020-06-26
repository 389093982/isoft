import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/model/user.dart';

class LinkKnownApi {
  static Dio _dio;
  static final String baseUrl = 'http://192.168.1.5:6001';

  static void init() async {
    _dio = Dio(BaseOptions(baseUrl: baseUrl, followRedirects: false))
      ..interceptors.add(LogInterceptor(responseBody: true, requestBody: true));
  }

  static Future<Response> doPost(
    BuildContext context,
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
  static Future<LoginUserResponse> postLogin(BuildContext context, String username, String passwd, String redirectUrl) async {
    var response = await doPost(context, '/api/iwork/httpservice/isoft_linkknown_api/PostLogin', params: {
      'username': username,
      'passwd': passwd,
      'redirectUrl': redirectUrl,
    });
    return LoginUserResponse.fromJson(response.data);
  }
}
