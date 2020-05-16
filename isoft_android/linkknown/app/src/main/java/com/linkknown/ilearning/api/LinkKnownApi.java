package com.linkknown.ilearning.api;

import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.model.CreateVerifyCodeResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.model.FavoriteResponse;
import com.linkknown.ilearning.model.LoginUserResponse;
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

    // 课程搜索接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/SearchCourseList")
    Observable<CourseSearchResponse> searchCourseList(@Query("search") String search);

    // 注册接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/Regist")
    Observable<RegistResponse> regist(@Query("username") String username,
                                      @Query("passwd") String passwd,
                                      @Query("nickname") String nickname,
                                      @Query("verifyCode") String verifyCode,
                                      @Query("third_user_type") String third_user_type);

    // 占位符查询接口
    @GET("/api/iwork/httpservice/isoft_linkknown_api/FilterElementByPlacement")
    Observable<LoginUserResponse> filterElementByPlacement(@Query("placement") String placement);

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


}