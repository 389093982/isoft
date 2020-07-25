import 'dart:io';

import 'package:dio/dio.dart';
import 'package:linkknown/common/error.dart';
import 'package:linkknown/config/env_config.dart';
import 'package:linkknown/model/advise_list.dart';
import 'package:linkknown/model/base.dart';
import 'package:linkknown/model/blog_detail_response.dart';
import 'package:linkknown/model/course_detail.dart';
import 'package:linkknown/model/course_history_response.dart';
import 'package:linkknown/model/course_meta.dart';
import 'package:linkknown/model/customtag_course_response.dart';
import 'package:linkknown/model/element.dart';
import 'package:linkknown/model/favorite_count_response.dart';
import 'package:linkknown/model/favorite_is_response.dart';
import 'package:linkknown/model/first_level_comment_response.dart';
import 'package:linkknown/model/get_my_catalogs_response.dart';
import 'package:linkknown/model/get_user_detail_response.dart';
import 'package:linkknown/model/get_user_info_by_names_response.dart';
import 'package:linkknown/model/login_user_response.dart';
import 'package:linkknown/model/message.dart';
import 'package:linkknown/model/my_coupon_response.dart';
import 'package:linkknown/model/pay_order_response.dart';
import 'package:linkknown/model/pay_shopping_cart_response.dart';
import 'package:linkknown/model/query_page_blog_response.dart';
import 'package:linkknown/model/refresh_token_response.dart';
import 'package:linkknown/model/second_level_comment_response.dart';
import 'package:linkknown/model/user_favorite_list_response.dart';
import 'package:linkknown/model/user_link_agent_response.dart';
import 'package:linkknown/utils/login_util.dart';

class LinkKnownApi {
  static Dio _dio;
  static final String baseUrl = LinkKnownConfig.config.apiBaseUrl;
  static String tokenString = "";

  // 登录成功后调用此方法更新全局的 tokenString
  static void updateTokenString({disalbe: false}) async {
    // disable 为 true 时表示注销 tokenString,如登出操作
    if (disalbe) {
      tokenString = "";
    } else {
      tokenString = await LoginUtil.getTokenString();
    }
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

  // 注册接口
  static Future<BaseResponse> regist(String username, String passwd, String nickname, String gender, String verifyCode, String third_user_type) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/regist',
        params: {
          'username': username,
          'passwd': passwd,
          'nickname': nickname,
          'gender': gender,
          'verifyCode': verifyCode,
          'third_user_type': third_user_type,
        });
    return BaseResponse.fromJson(response.data);
  }

  // 生成验证码接口
  static Future<BaseResponse> createVerifyCode(String username) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/createVerifyCode',
        params: {
          'username': username,
        });
    return BaseResponse.fromJson(response.data);
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


  //发布的课程
  static Future<CourseMetaResponse> getCourseListByUserName(String userName,int currentPage,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/getCourseListByUserName',
        params: {
          'userName': userName,
          'current_page': currentPage,
          'offset': offset,
        });
    return CourseMetaResponse.fromJson(response.data);
  }


  //查询收藏 -- 课程id集合
  static Future<UserFavoriteListResponse> getUserFavoriteList(String userName,String favorite_type,int currentPage,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/getUserFavoriteList',
        params: {
          'user_name': userName,
          'favorite_type': favorite_type,
          'current_page': currentPage,
          'offset': offset,
        });
    return UserFavoriteListResponse.fromJson(response.data);
  }


  //观看的课程 -- ids集合
  static Future<CourseHistoryResponse> showCourseHistory(int currentPage,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/showCourseHistory',
        params: {
          'current_page': currentPage,
          'offset': offset,
        });
    return CourseHistoryResponse.fromJson(response.data);
  }


  //根据ids批量查询课程
  static Future<CourseMetaResponse> GetCourseListByIds(String ids) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/GetCourseListByIds',
        params: {
          'ids': ids,
        });
    return CourseMetaResponse.fromJson(response.data);
  }


  // 查询博客
  static Future<QueryPageBlogResponse> queryPageBlog(String search_type,String search_data,String search_user_name,int current_page,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/queryPageBlog',
        params: {
          'search_type': search_type,
          'search_data': search_data,
          'search_user_name': search_user_name,
          'current_page': current_page,
          'offset': offset,
        });
    return QueryPageBlogResponse.fromJson(response.data);
  }


  // 根据usernames批量查询用户基本信息
  static Future<GetUserInfoByNamesResponse> GetUserInfoByNames(String usernames) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/GetUserInfoByNames',
        params: {
          'usernames': usernames,
        });
    return GetUserInfoByNamesResponse.fromJson(response.data);
  }


  // 添加博客分类
  static Future<BaseResponse> BlogCatalogEdit(String catalog_name,String catalog_desc) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/BlogCatalogEdit',
        params: {
          'catalog_name':catalog_name,
          'catalog_desc':catalog_desc,
        });
    return BaseResponse.fromJson(response.data);
  }


  // 查询我的博客分类
  static Future<GetMyCatalogsResponse> GetMyCatalogs() async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/GetMyCatalogs',
        params: {});
    return GetMyCatalogsResponse.fromJson(response.data);
  }


  // 发布博客
  static Future<BaseResponse> BlogArticleEdit(String article_id,String blog_title,String key_words,String catalog_name,int blog_status,String content,String link_href,String first_img) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/BlogArticleEdit',
        params: {
          'article_id':article_id,
          'blog_title':blog_title,
          'key_words':key_words,
          'catalog_name':catalog_name,
          'blog_status':blog_status,
          'content':content,
          'link_href':link_href,
          'first_img':first_img,
        });
    return BaseResponse.fromJson(response.data);
  }


  // 博客详情
  static Future<BlogDetailResponse> ShowBlogArticleDetail(String id) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/ShowBlogArticleDetail',
        params: {
          'id':id,
        });
    return BlogDetailResponse.fromJson(response.data);
  }


  // 评论查询接口,查询一级评论
  static Future<FirstLevelCommentResponse> FilterCommentFirstLevel(String theme_pk,String theme_type,String comment_type,int current_page,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/FilterCommentFirstLevel',
        params: {
          'theme_pk':theme_pk,
          'theme_type':theme_type,
          'comment_type':comment_type,
          'current_page':current_page,
          'offset':offset,
        });
    return FirstLevelCommentResponse.fromJson(response.data);
  }


  // 评论查询接口,查询二级评论
  static Future<SecondLevelCommentResponse> FilterCommentSecondLevel(int theme_pk,String theme_type,int org_parent_id,int current_page,int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/FilterCommentSecondLevel',
        params: {
          'theme_pk':theme_pk,
          'theme_type':theme_type,
          'org_parent_id':org_parent_id,
          'current_page':current_page,
          'offset':offset,
        });
    return SecondLevelCommentResponse.fromJson(response.data);
  }


  // 新增评论接口
  static Future<BaseResponse> AddComment(int theme_pk,String theme_type,String comment_type,String content,int org_parent_id,int parent_id,String refer_user_name) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/AddComment',
        params: {
          'theme_pk':theme_pk,
          'theme_type':theme_type,
          'comment_type':comment_type,
          'content':content,
          'org_parent_id':org_parent_id,
          'parent_id':parent_id,
          'refer_user_name':refer_user_name,
        });
    return BaseResponse.fromJson(response.data);
  }


  // 删除评论接口
  static Future<BaseResponse> deleteComment(int level,int id,int theme_pk,String theme_type,int org_parent_id) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/deleteComment',
        params: {
          'level':level,
          'id':id,
          'theme_pk':theme_pk,
          'theme_type':theme_type,
          'org_parent_id':org_parent_id,
        });
    return BaseResponse.fromJson(response.data);
  }

  // 根据自定义 tag 搜索课程
  static Future<CustomTagCourseResponse> queryCustomTagCourse(String custom_tag, int current_page, int offset) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/QueryCustomTagCourse',
        params: {
          'custom_tag':custom_tag,
          'current_page':current_page,
          'offset':offset,
        });
    return CustomTagCourseResponse.fromJson(response.data);
  }

  // 根据自定义 tag 搜索课程
  static Future<RefreshTokenResponse> refreshToken(String tokenString) async {
    var response = await doPost(
        '/api/iwork/httpservice/isoft_linkknown_api/RefreshToken',
        params: {
          'tokenString':tokenString,
        });
    return RefreshTokenResponse.fromJson(response.data);
  }

}