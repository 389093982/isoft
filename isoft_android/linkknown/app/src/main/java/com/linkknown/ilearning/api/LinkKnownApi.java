package com.linkknown.ilearning.api;

import com.linkknown.ilearning.model.AdviseListResponse;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CouponDatasResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.CreateVerifyCodeResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.model.FavoriteCountResponse;
import com.linkknown.ilearning.model.FavoriteResponse;
import com.linkknown.ilearning.model.HistoryResponse;
import com.linkknown.ilearning.model.IsFavoriteResponse;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.MessageListResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.model.RefreshTokenResponse;
import com.linkknown.ilearning.model.RegistResponse;
import com.linkknown.ilearning.model.UserDetailResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkKnownApi {

    // 查看热门推荐的课程
    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend")
    Observable<CourseMetaResponse> getHotCourseRecommend();

    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetCourseListByUserName")
    Observable<CourseMetaResponse> getCourseListByUserName(@Query("userName") String userName,
                                                           @Query("current_page") int current_page,
                                                           @Query("offset") int offset);

    // 根据自定义 tag 搜索课程
    @GET("/api/iwork/httpservice/isoft_linkknown_api/QueryCustomTagCourse")
    Observable<CourseMetaResponse> queryCustomTagCourse(@Query("custom_tag") String custom_tag,
                                                        @Query("current_page") int current_page,
                                                        @Query("offset") int offset);

    // 查询课程详情
    @GET("/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetailForApp")
    Observable<CourseDetailResponse> showCourseDetailForApp(@Query("course_id") int course_id);


    // 登录接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetUserDetail")
    Observable<UserDetailResponse> getUserDetail(@Query("userName") String userName);

    // 登录接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/PostLogin")
    Observable<LoginUserResponse> postLogin(@Query("username") String username,
                                            @Query("passwd") String passwd,
                                            @Query("redirectUrl") String redirectUrl);

    @GET("/api/iwork/httpservice/isoft_linkknown_api/RefreshToken")
    Observable<RefreshTokenResponse> refreshToken(@Query("tokenString") String tokenString);

    // 课程搜索接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/SearchCourseList")
    Observable<CourseMetaResponse> searchCourseList(@Query("search") String search,
                                                    @Query("isCharge") String isCharge,
                                                    @Query("current_page") int current_page,
                                                    @Query("offset") int offset);

    // 根据 id 列表获取课程信息
    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetCourseListByIds")
    Observable<CourseMetaResponse> getCourseListByIds(@Query("ids") String ids);

    // 注册接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/Regist")
    Observable<RegistResponse> regist(@Query("username") String username,
                                      @Query("passwd") String passwd,
                                      @Query("nickname") String nickname,
                                      @Query("verifyCode") String verifyCode,
                                      @Query("third_user_type") String third_user_type);

    // 占位符查询接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/FilterElementByPlacement")
    Observable<ElementResponse> filterElementByPlacement(@Query("placement") String placement);

    // 评论查询接口,查询一级评论
    @GET("/api/iwork/httpservice/isoft_linkknown_api/FilterCommentFirstLevel")
    Observable<CommentResponse> filterCommentFirstLevel(@Query("theme_pk") int theme_pk,
                                                          @Query("theme_type") String theme_type,
                                                          @Query("comment_type") String comment_type,
                                                          @Query("current_page") int current_page,
                                                          @Query("offset") int offset);

    // 评论查询接口,查询二级评论
    @GET("/api/iwork/httpservice/isoft_linkknown_api/FilterCommentSecondLevel")
    Observable<CommentResponse> filterCommentSecondLevel(@Query("theme_pk") int theme_pk,
                                                         @Query("theme_type") String theme_type,
                                                         @Query("org_parent_id") int org_parent_id);

    // 新增评论接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/AddComment")
    Observable<EditCommentResponse> addComment(@Query("theme_pk") int theme_pk,
                                               @Query("theme_type") String theme_type,
                                               @Query("comment_type") String comment_type,
                                               @Query("content") String content,
                                               @Query("org_parent_id") int org_parent_id,
                                               @Query("parent_id") int parent_id,
                                               @Query("refer_user_name") String refer_user_name);

    // 删除评论接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/deleteComment")
    Observable<BaseResponse> deleteComment(@Query("level") int level,
                                           @Query("id") int id,
                                           @Query("theme_pk") int theme_pk,
                                           @Query("theme_type") String theme_type,
                                           @Query("org_parent_id") int org_parent_id);

    // 查询用户收藏列表
    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetUserFavoriteList")
    Observable<FavoriteResponse> getUserFavoriteList(@Query("user_name") String user_name,
                                                     @Query("favorite_type") String favorite_type);

    // 生成验证码接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/createVerifyCode")
    Observable<CreateVerifyCodeResponse> createVerifyCode(@Query("username") String username);

    // 课程观看记录
    @GET("/api/iwork/httpservice/isoft_linkknown_api/ShowCourseHistory")
    Observable<HistoryResponse> showCourseHistory();


    // 是否收藏
    @GET("/api/iwork/httpservice/isoft_linkknown_api/IsFavorite")
    Observable<IsFavoriteResponse> isFavorite(@Query("user_name") String user_name,
                                              @Query("favorite_id") int favorite_id,
                                              @Query("favorite_type") String favorite_type);

    // 切换收藏状态
    @GET("/api/iwork/httpservice/isoft_linkknown_api/ToggleFavorite")
    Observable<BaseResponse> toggleFavorite(@Query("favorite_id") int favorite_id,
                                              @Query("favorite_type") String favorite_type);

    // 查询收藏总人数
    @GET("/api/iwork/httpservice/isoft_linkknown_api/queryFavoriteCount")
    Observable<FavoriteCountResponse> queryFavoriteCount(@Query("favorite_id") int favorite_id,
                                                         @Query("favorite_type") String favorite_type);

    // 查询个人优惠券信息
    @GET("/api/iwork/httpservice/isoft_linkknown_api/QueryPersonalCouponList")
    Observable<CouponListResponse> queryPersonalCouponList(@Query("isExpired") String isExpired,
                                                           @Query("isUsed") String isUsed,
                                                           @Query("current_page") int current_page,
                                                           @Query("offset") int pageSize);

    // 查询领券中心分页优惠券信息
    @GET("/api/iwork/httpservice/isoft_linkknown_api/QueryCouponCenterList")
    Observable<CouponListResponse> queryCouponCenterList(@Query("current_page") int current_page,
                                                           @Query("offset") int pageSize);

    @GET("/api/iwork/httpservice/isoft_linkknown_api/ReceiveCoupon")
    Observable<BaseResponse> receiveCoupon(@Query("activity_id") String activity_id);

    // 查询我的订单
    @GET("/api/iwork/httpservice/isoft_linkknown_api/queryPayOrderList")
    Observable<PayOrderResponse> queryPayOrderList(@Query("currentPage") int current_page,
                                                   @Query("offset") int pageSize,
                                                   @Query("user_name") String user_name,
                                                   @Query("scope") String scope);

    // 根据优惠券ID查询优惠券
    @GET("/api/iwork/httpservice/isoft_linkknown_api/QueryPagePayCoupon")
    Observable<CouponDatasResponse> QueryPagePayCoupon(@Query("coupon_id") String coupon_id,
                                                  @Query("currentPage") int current_page,
                                                  @Query("offset") int pageSize);


    // 查询购物车
    @GET("/api/iwork/httpservice/isoft_linkknown_api/queryPayShoppingCartList")
    Observable<PayShoppinpCartResponse> queryPayShoppingCartList(@Query("current_page") int current_page,
                                                                 @Query("offset") int pageSize);


    // 从购物车里删除
    @GET("/api/iwork/httpservice/isoft_linkknown_api/deleteFromShoppingCart")
    Observable<BaseResponse> deleteFromShoppingCart(@Query("goods_type") String goodsType,
                                                    @Query("goods_id") String goodsId);


    // 查询消息
    @GET("/api/iwork/httpservice/isoft_linkknown_api/QueryPageMessageList")
    Observable<MessageListResponse> queryPageMessageList(@Query("current_page") int current_page,
                                                         @Query("offset") int pageSize);

    // 意见、建议或吐槽
    @GET("/api/iwork/httpservice/isoft_linkknown_api/queryPageAdvise")
    Observable<AdviseListResponse> queryPageAdvise(@Query("current_page") int current_page,
                                                   @Query("offset") int pageSize);

    @GET("/api/iwork/httpservice/isoft_linkknown_api/InsertAdvise")
    Observable<BaseResponse> insertAdvise(@Query("advise") String advise,
                                                   @Query("advise_type") String advise_type);
}