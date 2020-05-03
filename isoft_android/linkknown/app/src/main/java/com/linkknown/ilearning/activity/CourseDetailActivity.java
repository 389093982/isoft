package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

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
    private CommonAdapter<CourseDetailResponse.CVideos> cVideosCommonAdapter;

    @BindView(R.id.detail_goback)
    public ImageView gobackView;

    @BindView(R.id.cVideoListView)
    public ListView cVideoListView;

    @BindView(R.id.courseImageView)
    public ImageView courseImageView;
    @BindView(R.id.courseNameView)
    public TextView courseNameView;
    @BindView(R.id.courseShortDescView)
    public TextView courseShortDescView;
    @BindView(R.id.courseTypeView)
    public TextView courseTypeView;
    @BindView(R.id.courseLabelView)
    public TextView courseLabelView;
    @BindView(R.id.courseNumberView)
    public TextView courseNumberView;
    @BindView(R.id.watchNumberView)
    public TextView watchNumberView;

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
        // 返回箭头点击事件
        gobackView.setOnClickListener(v -> finish());
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
                            CourseDetailResponse.Course course = courseDetailResponse.getCourse();

                            // 异步加载图片,使用 Glide 第三方库
                            Glide.with(mContext)
                                    .load(course.getSmall_image().replace("localhost", "192.168.1.2"))
                                    // placeholder 图片加载出来前,显示的图片
                                    // error 图片加载失败后,显示的图片
                                    .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                                    .into(courseImageView);

                            courseNameView.setText(course.getCourse_name());
                            courseShortDescView.setText(course.getCourse_short_desc());
                            courseTypeView.setText(course.getCourse_type() + "/" + course.getCourse_sub_type());
                            courseLabelView.setText(course.getCourse_label());

                            String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
                            courseNumberView.setText(String.format(courseNumberTextDemo, course.getCourse_number()));

                            String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
                            watchNumberView.setText(String.format(watchNumberTextDemo, course.getWatch_number()));

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
