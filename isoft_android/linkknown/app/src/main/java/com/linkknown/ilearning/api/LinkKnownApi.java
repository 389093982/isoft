package com.linkknown.ilearning.api;

import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.RegistResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkKnownApi {

    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend")
    Observable<CourseMetaResponse> getHotCourseRecommend();


    @GET("/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetailForApp")
    Observable<CourseDetailResponse> showCourseDetailForApp(@Query("course_id") int course_id);


    @GET("/api/iwork/httpservice/isoft_linkknown_api/PostLogin")
    Observable<LoginUserResponse> postLogin(@Query("username") String username,
                                            @Query("passwd") String passwd,
                                            @Query("redirectUrl") String redirectUrl);

    @GET("/api/iwork/httpservice/isoft_linkknown_api/SearchCourseList")
    Observable<CourseSearchResponse> searchCourseList(@Query("search") String search);

    @GET("/api/iwork/httpservice/isoft_linkknown_api/Regist")
    Observable<RegistResponse> regist(@Query("username") String username,
                                      @Query("passwd") String passwd,
                                      @Query("nickname") String nickname,
                                      @Query("verifyCode") String verifyCode,
                                      @Query("third_user_type") String third_user_type);

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
}