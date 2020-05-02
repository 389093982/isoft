package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseDetailActivity extends AppCompatActivity {

    private Context mContext;
    private Intent intent;
    private List<CourseDetailResponse.CVideos> cVideos = new ArrayList<>();

    @BindView(R.id.cVideoListView)
    public ListView cVideoListView;
    private CommonAdapter<CourseDetailResponse.CVideos> cVideosCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // 初始化
        ButterKnife.bind(this);

        mContext = this;

        intent = getIntent();

        // 发送异步请求获取数据
        initData();

        cVideosCommonAdapter = new CommonAdapter<CourseDetailResponse.CVideos>((ArrayList<CourseDetailResponse.CVideos>)cVideos,R.layout.item_cvideo) {
            @Override
            public void bindView(ViewHolder holder, CourseDetailResponse.CVideos cVideo) {
                holder.setText(R.id.cVideoName, cVideo.getVideo_name());
            }
        };
        cVideoListView.setAdapter(cVideosCommonAdapter);
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
                            cVideos.addAll(courseDetailResponse.getCVideos());
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
                        cVideosCommonAdapter.notifyDataSetChanged();
                    }
                });
    }
}
