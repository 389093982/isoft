package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.section.CourseDetailCVideoListSection;
import com.linkknown.ilearning.service.CourseService;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CourseDetailActivityBak extends AppCompatActivity {

    private Context mContext;
    private Intent intent;
    private List<CourseDetailResponse.CVideo> cVideos = new ArrayList<>();

    @BindView(R.id.detail_goback)
    public ImageView gobackView;

    @BindView(R.id.cVideoRecyclerView)
    public RecyclerView cVideoRecyclerView;

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
        setContentView(R.layout.activity_course_detail_bak);

        // 初始化
        ButterKnife.bind(this);

        mContext = this;

        intent = getIntent();

        // 发送异步请求获取数据
        initData();

        initAdapter();
        // 返回箭头点击事件
        gobackView.setOnClickListener(v -> finish());
    }


    private void initAdapter() {
        LiveEventBus.get("courseDetailResponse_" + intent.getIntExtra("course_id", -1), CourseDetailResponse.class)
                .observeSticky(this, courseDetailResponse -> {
                    if (courseDetailResponse.isSuccess()) {

                        bindViewHolderForCourse(courseDetailResponse);
                        bindViewHolderForCVideoList();

                    } else {
                        Log.e("onNext =>", "系统异常,请联系管理员~");
                    }
                });
    }

    private void bindViewHolderForCVideoList() {
        // 设置视频列表 section 部分
        SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CourseDetailCVideoListSection courseDetailCVideoListSection = new CourseDetailCVideoListSection(this, cVideos);
        sectionedRecyclerViewAdapter.addSection(courseDetailCVideoListSection);
        cVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cVideoRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    private void bindViewHolderForCourse(CourseDetailResponse courseDetailResponse) {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();

        // 异步加载图片,使用 Glide 第三方库
        Glide.with(mContext)
                .load(UIUtils.replaceMediaUrl(course.getSmall_image()))
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
    }

    private void initData () {
        CourseService.showCourseDetailForApp(intent.getIntExtra("course_id", -1));
    }
}
