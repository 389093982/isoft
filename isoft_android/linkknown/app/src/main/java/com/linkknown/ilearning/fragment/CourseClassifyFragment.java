package com.linkknown.ilearning.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.section.CourseClassifyBannerSection;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.section.CourseClassifyQuickSection;
import com.linkknown.ilearning.service.CourseClassifyService;
import com.linkknown.ilearning.service.CourseService;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CourseClassifyFragment extends Fragment implements View.OnClickListener {

    // 下拉刷新布局
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private boolean mIsRefreshing = false;

    private List<CourseClassifyService.BannerEntity> bannerEntities = new ArrayList<>();
    // 数组是引用传递
    private List<CourseClassifyService.HotClassify> hotClassifies = new ArrayList<>();
    private List<CourseMetaResponse.CourseMeta> hotCourseMetas = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type_detail, container, false);
        ButterKnife.bind(this, rootView);

        // 初始化组件
        init();

        // 加载数据
        loadData();

        // 更新 UI
        notifyDataSetChanged();

        return rootView;
    }

    // adapter
    private SectionedRecyclerViewAdapter sectionAdapter;
    // sections
    private CourseClassifyBannerSection courseClassifyBannerSection;
    private CourseClassifyQuickSection courseClassifyQuickSection;
    private CourseHotRecommendSection courseHotRecommendSection;

    private void init () {
        sectionAdapter = new SectionedRecyclerViewAdapter();
        courseClassifyBannerSection = new CourseClassifyBannerSection(bannerEntities);
        courseClassifyQuickSection = new CourseClassifyQuickSection(getActivity(), hotClassifies);
        courseHotRecommendSection = new CourseHotRecommendSection(getActivity(), hotCourseMetas);
        sectionAdapter.addSection(courseClassifyBannerSection);
        sectionAdapter.addSection(courseClassifyQuickSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        sectionAdapter.addSection(courseHotRecommendSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(sectionAdapter);
    }

    private void notifyDataSetChanged () {
        LiveEventBus.get(Constants.COURSE_HOT_RECOMMEND_PREFIX, CourseMetaResponse.class).observeSticky(this, courseMetaResponse -> {
            if (courseMetaResponse != null && CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                hotCourseMetas.clear();
                hotCourseMetas.addAll(courseMetaResponse.getCourses());
                // 属性 UI
                sectionAdapter.notifyDataSetChanged();
            }
        });

        LiveEventBus.get("showTypeDetails", List.class).observe(this, list -> {
            // 清空旧数据
            hotClassifies.clear();
            bannerEntities.clear();

            for (Object obj : list){
                if (obj instanceof CourseClassifyService.HotClassify) {
                    hotClassifies.add((CourseClassifyService.HotClassify)obj);
                }
                if (obj instanceof CourseClassifyService.BannerEntity) {
                    bannerEntities.add((CourseClassifyService.BannerEntity)obj);
                }
            }
            // 属性 UI
            sectionAdapter.notifyDataSetChanged();
            // 隐藏刷新效果图标
            finishRefreshing();
        });
    }


    private void loadData() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // 通过post(Runnable runnable)方法放到UI线程排队执行
        recyclerView.post(() -> {
            // 显示刷新进度条，通过直接设置setRefreshing(true)
            refreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            CourseClassifyService.loadData();
            CourseService.getHotCourseRecommend();
        });

        // refreshLayout 设置刷新监听
        refreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            CourseClassifyService.loadData();
            CourseService.getHotCourseRecommend();
        });
    }


    @Override
    public void onClick(View v) {

    }

    protected void finishRefreshing() {
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    }


}
