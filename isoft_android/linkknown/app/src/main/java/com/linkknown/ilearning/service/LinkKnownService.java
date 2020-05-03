package com.linkknown.ilearning.service;

import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkKnownService {

    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend")
    Observable<CourseMetaResponse> getHotCourseRecommend();


    @GET("/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetailForApp")
    Observable<CourseDetailResponse> showCourseDetailForApp(@Query("course_id") int course_id);


    @GET("/api/iwork/httpservice/isoft_linkknown_api/PostLogin")
    Observable<LoginResponse> postLogin(@Query("username") String username,
                                        @Query("passwd") String passwd,
                                        @Query("redirectUrl") String redirectUrl);
}
