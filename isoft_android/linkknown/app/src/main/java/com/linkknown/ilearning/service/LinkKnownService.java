package com.linkknown.ilearning.service;

import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.model.LoginUserResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkKnownService {

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

}
