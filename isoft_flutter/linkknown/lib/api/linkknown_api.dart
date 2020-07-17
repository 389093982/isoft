import 'dart:io';

import 'package:dio/dio.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/config/env_config.dart';
import 'package:linkknown/model/advise_list.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/element.dart';
import 'package:linkknown/model/favorite_count_response.dart';
import 'package:linkknown/model/favorite_is_response.dart';
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/model/login_user_response.dart';
import 'package:linkknown/model/message.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/pay_order_response.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/model/user_link_agent_response.dart';
import 'package:linkknown/utils/login_util.dart';

class LinkKnownApi {
  static Dio _dio;
  static final String baseUrl = LinkKnownConfig.config.apiBaseUrl;
  static String tokenString = "";

  // 登录成功后调用此方法更新全局的 tokenString
  static void updateTokenString() async {
    tokenString = await LoginUtil.getTokenString();
  }

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

  static Future<Response> doPost(String url, {
    Map<String, dynamic> params,
  }) async {
    try {
      _dio.options.headers.addAll(
          new Map<String, String>.from({"tokenString": tokenString}));
      return await _dio.post(url,
        queryParameters: params,);
    } on DioError catch (e) {
      if (e != null && e.response != null && e.response.statusCode == 401) {
        throw LinkKnownError.unAuthorizedLogin();
      } else {
        throw LinkKnownError(-1, e.message);
      }
    }
  }

  // 登录
  static Future<LoginUserResponse> postLogin(String username, String passwd,
      String redirectUrl) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/PostLogin',
        params: {
          'username': username,
          'passwd': passwd,
          'redirectUrl': redirectUrl,
        });
    return LoginUserResponse.fromJson(response.data);
  }

  // 查询用户基本信息
  static Future<GetUserDetailResponse> getUserDetail(String userName) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/GetUserDetail',
        params: {
          'userName': userName,
        });
    return GetUserDetailResponse.fromJson(response.data);
  }

  // 课程搜索接口
  static Future<CourseMetaResponse> searchCourseList(String search,
      String isCharge, int current_page, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/SearchCourseList',
        params: {
          'search': search,
          'isCharge': isCharge,
          'current_page': current_page,
          'offset': offset,
        });
    return CourseMetaResponse.fromJson(response.data);
  }

  // 查询课程详情
  static Future<CourseDetailResponse> showCourseDetailForApp(int course_id,
      String user_name) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetailForApp',
        params: {
          'course_id': course_id,
          'user_name': user_name,
        });
    return CourseDetailResponse.fromJson(response.data);
  }

  // 占位符查询
  static Future<ElementResponse> filterElementByPlacement(
      String placement) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/FilterElementByPlacement',
        params: {
          'placement': placement,
        });
    return ElementResponse.fromJson(response.data);
  }

  // 添加意见
  static Future<BaseResponse> insertAdvise(String advise,
      String advise_type) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/InsertAdvise',
        params: {
          'advise': advise,
          'advise_type': advise_type,
        });
    return BaseResponse.fromJson(response.data);
  }

  // 分页查询意见
  static Future<AdviseListResponse> queryPageAdvise(int current_page,
      int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryPageAdvise',
        params: {
          'current_page': current_page,
          'offset': offset,
        });
    return AdviseListResponse.fromJson(response.data);
  }

  // 查询收藏总人数
  static Future<FavoriteCountResponse> queryFavoriteCount(int favorite_id,
      String favorite_type) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryFavoriteCount',
        params: {
          'favorite_id': favorite_id,
          'favorite_type': favorite_type,
        });
    return FavoriteCountResponse.fromJson(response.data);
  }

  // 判断是否收藏
  static Future<IsFavoriteResponse> isFavorite(String user_name, int favorite_id,
      String favorite_type) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/IsFavorite',
        params: {
          'user_name': user_name,
          'favorite_id': favorite_id,
          'favorite_type': favorite_type,
        });
    return IsFavoriteResponse.fromJson(response.data);
  }

  // 切换收藏状态
  static Future<BaseResponse> toggleFavorite(int favorite_id,
      String favorite_type) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/ToggleFavorite',
        params: {
          'favorite_id': favorite_id,
          'favorite_type': favorite_type,
        });
    return BaseResponse.fromJson(response.data);
  }

  // 查询我的优惠券
  static Future<MyCouponResponse> queryPersonalCouponList(String isExpired,
      String isUsed, int current_page, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryPersonalCouponList',
        params: {
          'isExpired': isExpired,
          'isUsed': isUsed,
          'current_page': current_page,
          'offset': offset,
        });
    return MyCouponResponse.fromJson(response.data);
  }

  // 查询消息
  static Future<MessageListResponse> queryPageMessageList(int current_page, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/QueryPageMessageList',
        params: {
          'current_page': current_page,
          'offset': offset,
        });
    return MessageListResponse.fromJson(response.data);
  }


  // 查询我的购物车
  static Future<PayShoppinpCartResponse> queryPayShoppingCartList(int current_page, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryPayShoppingCartList',
        params: {
          'current_page': current_page,
          'offset': offset,
        });
    return PayShoppinpCartResponse.fromJson(response.data);
  }


  // 我的订单
  static Future<PayOrderResponse> queryPayOrderList(int currentPage, int offset,String user_name,String scope,{String goods_type}) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryPayOrderList',
        params: {
          'currentPage': currentPage,
          'offset': offset,
          'user_name': user_name,
          'goods_type': goods_type??"",
          'scope': scope,
        });
    return PayOrderResponse.fromJson(response.data);
  }



  // 我的客户
  static Future<UserLinkAgentResponse> QueryUserLinkAgent(int currentPage, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/QueryUserLinkAgent',
        params: {
          'current_page': currentPage,
          'offset': offset,
        });
    return UserLinkAgentResponse.fromJson(response.data);
  }


  //查询邀请我的人
  static Future<UserLinkAgentResponse> QueryTodayInviteMe(int currentPage, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/QueryTodayInviteMe',
        params: {
          'current_page': currentPage,
          'offset': offset,
        });
    return UserLinkAgentResponse.fromJson(response.data);
  }


  //添加客户
  static Future<BaseResponse> AddUserLinkAgent(String user_name) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/AddUserLinkAgent',
        params: {
          'user_name': user_name,
        });
    return BaseResponse.fromJson(response.data);
  }


  //同意邀请
  static Future<BaseResponse> AgreeUserLinkAgent(String agent_user_name) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/AgreeUserLinkAgent',
        params: {
          'agent_user_name': agent_user_name,
        });
    return BaseResponse.fromJson(response.data);
  }


}