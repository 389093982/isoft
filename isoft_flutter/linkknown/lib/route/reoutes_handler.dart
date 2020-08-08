
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:linkknown/page/advise_edit_page.dart';
import 'package:linkknown/page/advise_page.dart';
import 'package:linkknown/page/blog_detail_page.dart';
import 'package:linkknown/page/bought_course_page.dart';
import 'package:linkknown/page/business_page.dart';
import 'package:linkknown/page/cloud_blog_page.dart';
import 'package:linkknown/page/coming_soon_page.dart';
import 'package:linkknown/page/coupon_goods_page.dart';
import 'package:linkknown/page/course_detail_page.dart';
import 'package:linkknown/page/course_search_page.dart';
import 'package:linkknown/page/customtag_course_page.dart';
import 'package:linkknown/page/edit_blog_page.dart';
import 'package:linkknown/page/forget_pwd_page.dart';
import 'package:linkknown/page/huodong_page.dart';
import 'package:linkknown/page/linkknown_with_me_page.dart';
import 'package:linkknown/page/login_page.dart';
import 'package:linkknown/page/main_page.dart';
import 'package:linkknown/page/message_page.dart';
import 'package:linkknown/page/about_page.dart';
import 'package:linkknown/page/modify_header_page.dart';
import 'package:linkknown/page/my_attention_page.dart';
import 'package:linkknown/page/my_coupon_page.dart';
import 'package:linkknown/page/my_fensi_page.dart';
import 'package:linkknown/page/open_vip_page.dart';
import 'package:linkknown/page/order_detail_page.dart';
import 'package:linkknown/page/pay_order_commit_page.dart';
import 'package:linkknown/page/pay_order_page.dart';
import 'package:linkknown/page/personal_center_page.dart';
import 'package:linkknown/page/receive_coupon_center_page.dart';
import 'package:linkknown/page/regist_page.dart';
import 'package:linkknown/page/setting_page.dart';
import 'package:linkknown/page/shopping_cart_page.dart';
import 'package:linkknown/page/splash_page.dart';
import 'package:linkknown/page/to_select_available_coupon_page.dart';
import 'package:linkknown/page/user_agreement_page.dart';
import 'package:linkknown/page/user_info_page.dart';
import 'package:linkknown/page/user_signature_page.dart';
import 'package:linkknown/page/video_play_page.dart';
import 'package:linkknown/utils/fluro_convert_utils.dart';
import 'package:linkknown/utils/login_util.dart';
import 'package:linkknown/utils/ui_util.dart';

// 临时存储复杂的参数
var routerParamMap = Map();

// splash 页面
var splashHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return SplashPage();
    });

// 登录页
var mainHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return MainPage();
    });

// 登录页
var loginHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return LoginPage();
    });

// 注册页
var registHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return RegistPage();
    });

// 忘记密码页
var forgetPwdHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return ForgetPwdPage();
    });

// 修改头像页
var modifyHeaderHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return ModifyHeaderPage();
    });

// 关于链知页
var aboutHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AboutPage();
    });

// 课程详情页
var courseDetailHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      int course_id = int.parse(params['course_id'].first);
      return CourseDetailPage(course_id);
    });

// 课程搜索页
var courseSearchHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String search = params['search'].first??'';
      String isCharge = params['isCharge'].first??'';
      return CourseSearchPage(FluroConvertUtil.fluroCnParamsDecode(search), isCharge);
    });

// 视频播放界面
var videoPlayHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String index = params['index'].first;       // 播放当前视频的索引
      return VideoPlayPage(routerParamMap["videoplay_courseKey"], routerParamMap["videoplay_cVideosKey"], int.parse(index));
    });

// customTag 课程搜索页
var customTagCourseHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String custom_tag = params['custom_tag'].first;       // 播放当前视频的索引
      return CustomTagCoursePage(custom_tag);
    });

//开通会员
var openVipHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return OpenVipPage();
    });

//我的关注
var myAttentionHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return MyAttentionPage();
    });

//我的粉丝
var myFensiHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return MyFensiPage();
    });

//个性签名
var userSignatureHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return UserSignaturePage();
    });

//用户信息
var userInfoHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return UserInfoPage();
    });


//优惠券
var myCouponHandler = new Handler(
  handlerFunc: (BuildContext context, Map<String,List<String>> params){
    return MyCouponPage();
  });

//可使用优惠券的商品
var couponGoodsHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      String youhui_type = params['youhui_type'].first;
      String goods_min_amount = params['goods_min_amount'].first;
      return CouponGoodsPage(youhui_type,goods_min_amount);
    });

//领券中心
var receiveCouponCenterHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return ReceiveCouponCenterPage();
    });

//购物车
var shoppingCartHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return ShoppingCartPage();
    });

//订单
var payOrderHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return PayOrderPage();
    });

//订单详情
var orderDetailHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      String orderId = params['orderId'].first??"0";
      return OrderDetailPage(FluroConvertUtil.fluroCnParamsDecode(orderId));
    });

//订单提交页面
var payOrderCommitHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      String goodsType = params['goodsType'].first??"0";
      String goodsId = params['goodsId'].first??"0";
      String goodsImg = params['goodsImg'].first??"0";
      String goodsDesc = params['goodsDesc'].first??"0";
      String price = params['price'].first??"0";
      return PayOrderCommitPage(goodsType,goodsId,FluroConvertUtil.fluroCnParamsDecode(goodsImg),FluroConvertUtil.fluroCnParamsDecode(goodsDesc),price);
    });

//选择可用优惠券
var toSelectAvailableCouponHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      String userName = params['userName'].first??"0";
      String target_type = params['target_type'].first??"0";
      String target_id = params['target_id'].first??"0";
      String paid_amount = params['paid_amount'].first??"0";
      String today = params['today'].first??"0";
      return ToSelectAvailableCouponPage(userName,target_type,target_id,paid_amount,today);
    });

//活动
var huodongHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return HuodongPage();
    });

//考试
var kaoshiHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return SplashPage();
    });

// 云博客页
var cloudBlogHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return CloudBlogPage();
    });

// 编辑博客
var editBlogHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String blog_id = params['blog_id']?.first??"-1";
      return EditBlogPage(blog_id:blog_id??"0");
    });

// 博客详情
var BlogDetailHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String blog_id = params['blog_id'].first??"0";
      return BlogDetailgPage(int.parse(blog_id));
    });

// 个人中心页
var personalCenterHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      String userName = params['userName'].first??"";
      return PersonalCenterPage(userName);
    });

// 已购课程页
var boughtCourseHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return BoughtCoursePage();
    });

// 我要吐槽页
var adviseHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AdvisePage();
    });

var adviseEditHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      return AdviseEditPage();
    });

//我与链知
var linkknownWithMeHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return LinkknownWithMePage();
    });

// 用户协议
var userAgreementHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return UserAgreementPage();
    });

// 商业合作
var businessHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return BusinessPage();
    });

// 消息中心
var messageHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return MessagePage();
    });

// 设置
var settingHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return SettingPage();
    });

// 敬请期待页面
var comingSoonHandler = new Handler(
    handlerFunc: (BuildContext context, Map<String,List<String>> params){
      return ComingSoonPage();
    });


