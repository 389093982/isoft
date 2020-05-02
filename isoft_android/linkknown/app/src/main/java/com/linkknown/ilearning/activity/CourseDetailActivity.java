package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseDetailActivity extends AppCompatActivity {

    private Context mContext;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        mContext = this;
        intent = getIntent();

        // 发送异步请求获取数据
        initData();
    }

    private void initData () {
        LinkKnownApiFactory.getLinkKnownService().showCourseDetail(intent.getIntExtra("course_id", -1))
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseDetailResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseDetailResponse courseDetailResponse) {
                        if (courseDetailResponse.isSuccess()) {
                            ToastUtil.showText(mContext, courseDetailResponse.toString());
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
