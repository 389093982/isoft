package com.linkknown.ilearning.service;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.CourseSearchResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseService {

    public static void showCourseDetailForApp (int course_id) {
        LinkKnownApiFactory.getLinkKnownApi().showCourseDetailForApp(course_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseDetailResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseDetailResponse courseDetailResponse) {
                        if (courseDetailResponse.isSuccess()) {
                            CourseDetailResponse.Course course = courseDetailResponse.getCourse();
//
//                            // 异步加载图片,使用 Glide 第三方库
//                            Glide.with(mContext)
//                                    .load(UIUtils.replaceMediaUrl(course.getSmall_image()))
//                                    // placeholder 图片加载出来前,显示的图片
//                                    // error 图片加载失败后,显示的图片
//                                    .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
//                                    .into(courseImageView);
//
//                            courseNameView.setText(course.getCourse_name());
//                            courseShortDescView.setText(course.getCourse_short_desc());
//                            courseTypeView.setText(course.getCourse_type() + "/" + course.getCourse_sub_type());
//                            courseLabelView.setText(course.getCourse_label());
//
//                            String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
//                            courseNumberView.setText(String.format(courseNumberTextDemo, course.getCourse_number()));
//
//                            String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
//                            watchNumberView.setText(String.format(watchNumberTextDemo, course.getWatch_number()));
//
//                            cVideos.addAll(courseDetailResponse.getCVideos());
                            LiveEventBus.get("courseDetailResponse_" + course_id, CourseDetailResponse.class).post(courseDetailResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            LiveEventBus.get("courseDetailResponse_" + course_id, CourseDetailResponse.class).post(courseDetailResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CourseDetailResponse courseDetailResponse = new CourseDetailResponse();
                        courseDetailResponse.setErrorMsg("数据加载失败!");
                        LiveEventBus.get("courseDetailResponse_" + course_id, CourseDetailResponse.class).post(courseDetailResponse);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void getHotCourseRecommend () {
        LinkKnownApiFactory.getLinkKnownApi().getHotCourseRecommend()
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseMetaResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess() && courseMetaResponse.getCourses() != null) {
                            LiveEventBus.get("courseMetaResponse", CourseMetaResponse.class).post(courseMetaResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            LiveEventBus.get("courseMetaResponse", CourseMetaResponse.class).post(courseMetaResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CourseMetaResponse courseMetaResponse = new CourseMetaResponse();
                        courseMetaResponse.setErrorMsg("数据加载失败!");
                        LiveEventBus.get("courseMetaResponse", CourseMetaResponse.class).post(courseMetaResponse);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void search(String search) {
        LinkKnownApiFactory.getLinkKnownApi().searchCourseList(search)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseSearchResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseSearchResponse courseSearchResponse) {
                        if (StringUtils.isEmpty(courseSearchResponse.getErrorMsg())) {

                            LiveEventBus.get("courseSearchResponse",  CourseSearchResponse.class).post(courseSearchResponse);
                        } else {
                            Log.e("onError =>", courseSearchResponse.getErrorMsg());
                            LiveEventBus.get("courseSearchResponse",  CourseSearchResponse.class).post(courseSearchResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("onError =>", e.getMessage());
                        CourseSearchResponse result = new CourseSearchResponse();
                        result.setErrorMsg(e.getMessage());
                        LiveEventBus.get("courseSearchResponse",  CourseSearchResponse.class).post(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
