package com.linkknown.ilearning.manager;

import android.util.Log;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseSearchResponse;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseServiceManager {

    public static void search(String search) {
        LinkKnownApiFactory.getLinkKnownService().searchCourseList(search)
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
