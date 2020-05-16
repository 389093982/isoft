package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.section.CourseDetailCVideoListSection;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CourseIntroduceFragment extends BaseLazyLoadFragment {

    private Context mContext;
    private CourseDetailResponse courseDetailResponse;

    public CourseIntroduceFragment(CourseDetailResponse courseDetailResponse) {
        this.courseDetailResponse = courseDetailResponse;
    }

    @BindView(R.id.courseNameText)
    public TextView courseNameText;
    @BindView(R.id.playNumberText)
    public TextView playNumberText;
    @BindView(R.id.courseNumberText)
    public TextView courseNumberText;
    @BindView(R.id.courseShortDescText)
    public TextView courseShortDescText;
    @BindView(R.id.courseOperateRecyclerView)
    public RecyclerView courseOperateRecyclerView;
    @BindView(R.id.cVideoRecyclerView)
    public RecyclerView cVideoRecyclerView;
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    @BindView(R.id.userNameText)
    public TextView userNameText;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initData() {
        init();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_introduce;
    }

    private void init () {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        List<CourseDetailResponse.CVideo> cVideos = courseDetailResponse.getCVideos();
        // 设置课程名称
        courseNameText.setText(course.getCourse_name());
        // 设置播放次数
        playNumberText.setText(course.getWatch_number() + "");
        // 课程集数
        courseNumberText.setText(course.getCourse_number() + "");
        // 课程描述
        courseShortDescText.setText(course.getCourse_short_desc());

        List<String> courseOperates = Arrays.asList("分享", "投硬币", "收藏", "缓存");
        userNameText.setText(courseDetailResponse.getUser().getNick_name());
        UIUtils.setImage(mContext, headerIcon, courseDetailResponse.getUser().getSmall_icon());

        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(String.class, new MultiItemView<String>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_course_operate;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull String s, int i) {
                viewHolder.setText(R.id.operateNameText, s);
            }
        });
        multiTypeAdapter.setItems(courseOperates);
        courseOperateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        courseOperateRecyclerView.setAdapter(multiTypeAdapter);

        // 设置视频列表 section 部分
        SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CourseDetailCVideoListSection courseDetailCVideoListSection = new CourseDetailCVideoListSection(mContext, cVideos);
        sectionedRecyclerViewAdapter.addSection(courseDetailCVideoListSection);
        cVideoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        cVideoRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    @OnClick(R.id.userInfoLayout)
    public void showUserDetail () {
        UIUtils.gotoActivity(mContext, UserDetailActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME, courseDetailResponse.getCourse().getCourse_author());
            return intent;
        });
    }
}
