package com.linkknown.ilearning.service;

import com.linkknown.ilearning.model.CourseMetaResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LinkKnownService {

//    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend2" )
//    Call<CourseMeta> GetHotCourseRecommend2(
//            @Query("username") String username,
//            @Query("password") String password
//    );

    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend2" )
    Observable<CourseMetaResponse> getHotCourseRecommend();
}
