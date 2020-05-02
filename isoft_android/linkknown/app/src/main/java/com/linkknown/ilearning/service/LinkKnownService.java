package com.linkknown.ilearning.service;

import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkKnownService {

    @GET("/api/iwork/httpservice/isoft_linkknown_api/GetHotCourseRecommend2" )
    Observable<CourseMetaResponse> getHotCourseRecommend();


    @GET("/api/iwork/httpservice/isoft_linkknown_api/ShowCourseDetail2" )
    Observable<CourseDetailResponse> showCourseDetail(
            @Query("course_id") int course_id
    );
}
