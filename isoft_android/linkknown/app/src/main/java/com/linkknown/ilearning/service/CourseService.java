package com.linkknown.ilearning.service;

import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseService {

    public static void queryCustomTagCourse (String custom_tag, int current_page, int offset) {
        LinkKnownApiFactory.getLinkKnownApi().queryCustomTagCourse(custom_tag, current_page, offset)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseMetaResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            LiveEventBus.get(Constants.COURSE_CUSTOM_TAG_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            LiveEventBus.get(Constants.COURSE_CUSTOM_TAG_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CourseMetaResponse courseMetaResponse = new CourseMetaResponse();
                        courseMetaResponse.setErrorMsg("数据加载失败!");
                        LiveEventBus.get(Constants.COURSE_CUSTOM_TAG_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
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
                            LiveEventBus.get(Constants.COURSE_HOT_RECOMMEND_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            LiveEventBus.get(Constants.COURSE_HOT_RECOMMEND_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        CourseMetaResponse courseMetaResponse = new CourseMetaResponse();
                        courseMetaResponse.setErrorMsg("数据加载失败!");
                        LiveEventBus.get(Constants.COURSE_HOT_RECOMMEND_PREFIX, CourseMetaResponse.class).post(courseMetaResponse);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
