package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.section.CourseClassifyBannerSection;
import com.linkknown.ilearning.section.CourseClassifyItemSection2;
import com.linkknown.ilearning.section.CourseClassifyQuickSection;
import com.linkknown.ilearning.service.CourseClassifyService;

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

    private Context mContext;
    private boolean mIsRefreshing = false;

    private List<CourseClassifyService.BannerEntity> bannerEntities = new ArrayList<>();
    // 数组是引用传递
    private List<CourseClassifyService.HotClassify> hotClassifies = new ArrayList<>();
    private List<CourseClassifyService.HotClassify2> hotClassifies2 = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type_detail, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        // 初始化组件
        init();

        sectionAdapter = new SectionedRecyclerViewAdapter();
        courseClassifyBannerSection = new CourseClassifyBannerSection(bannerEntities);
        courseClassifyQuickSection = new CourseClassifyQuickSection(getActivity(), hotClassifies);
        courseClassifyItemSection2 = new CourseClassifyItemSection2(getActivity(), hotClassifies2);
        sectionAdapter.addSection(courseClassifyBannerSection);
        sectionAdapter.addSection(courseClassifyQuickSection);
        sectionAdapter.addSection(courseClassifyItemSection2);
        sectionAdapter.addSection(courseClassifyItemSection2);
        sectionAdapter.addSection(courseClassifyItemSection2);
        sectionAdapter.addSection(courseClassifyItemSection2);
        sectionAdapter.addSection(courseClassifyItemSection2);
        sectionAdapter.addSection(courseClassifyItemSection2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(sectionAdapter);

        // 绑定 adapter
//        bindAdapter();

        bindAdapterData();

        return rootView;
    }

    private CourseClassifyBannerSection courseClassifyBannerSection;
    private CourseClassifyQuickSection courseClassifyQuickSection;
    private CourseClassifyItemSection2 courseClassifyItemSection2;

    private SectionedRecyclerViewAdapter sectionAdapter;

    private void bindAdapterData () {
        LiveEventBus.get("showTypeDetails", List.class).observe(this, list -> {
            hotClassifies.clear();
            hotClassifies2.clear();
            bannerEntities.clear();

            for (Object obj : list){
                if (obj instanceof CourseClassifyService.HotClassify) {
                    hotClassifies.add((CourseClassifyService.HotClassify)obj);
                }
                if (obj instanceof CourseClassifyService.HotClassify2) {
                    hotClassifies2.add((CourseClassifyService.HotClassify2)obj);
                }
                if (obj instanceof CourseClassifyService.BannerEntity) {
                    bannerEntities.add((CourseClassifyService.BannerEntity)obj);
                }
            }
            sectionAdapter.notifyDataSetChanged();
            finishRefreshing();
        });

//        LiveEventBus.get("showTypeDetails", List.class).observe(this, list -> {
//            multiTypeAdapter.setItems(list);
//            multiTypeAdapter.notifyDataSetChanged();
//            finishRefreshing();
//        });
    }


    private void init() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // 通过post(Runnable runnable)方法放到UI线程排队执行
        recyclerView.post(() -> {
            // 显示刷新进度条，通过直接设置setRefreshing(true)
            refreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            clearAndLoadData();
        });

        // refreshLayout 设置刷新监听
        refreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            clearAndLoadData();
        });
    }


    @Override
    public void onClick(View v) {

    }

    protected void finishRefreshing() {
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    }



    private void clearAndLoadData() {
        CourseClassifyService.loadData();
    }

}
