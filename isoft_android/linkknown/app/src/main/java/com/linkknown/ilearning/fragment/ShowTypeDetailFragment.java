package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.section.ShowTypeDetailBannerSection;
import com.linkknown.ilearning.section.ShowTypeDetailClassifySection;
import com.linkknown.ilearning.section.ShowTypeDetailClassifySection2;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import lombok.Data;

public class ShowTypeDetailFragment extends Fragment implements View.OnClickListener {

    // 下拉刷新布局
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private boolean mIsRefreshing = false;

    private MultiTypeAdapter multiTypeAdapter;

    private List<ShowTypeDetailService.BannerEntity> bannerEntities = new ArrayList<>();
    // 数组是引用传递
    private List<ShowTypeDetailService.HotClassify> hotClassifies = new ArrayList<>();
    private List<ShowTypeDetailService.HotClassify2> hotClassifies2 = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type_detail, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        // 初始化组件
        init();

        sectionAdapter = new SectionedRecyclerViewAdapter();
        showTypeDetailClassifySection = new ShowTypeDetailClassifySection(hotClassifies);
        showTypeDetailClassifySection2 = new ShowTypeDetailClassifySection2(hotClassifies2);
        showTypeDetailBannerSection = new ShowTypeDetailBannerSection(bannerEntities);
        sectionAdapter.addSection(showTypeDetailBannerSection);
        sectionAdapter.addSection(showTypeDetailClassifySection);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        sectionAdapter.addSection(showTypeDetailClassifySection2);
        // 将子视图的SpanSize都设置为 4，那么这个子视图将占整个RecyclerView可用宽度
        final GridLayoutManager glm = new GridLayoutManager(getContext(), 4);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int position) {
                int sectionIndex = sectionAdapter.getSectionIndex(sectionAdapter.getSectionForPosition(position));
                // 1、轮播图 2、header 占满整行
                if (sectionIndex == 0 ||
                        sectionAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 4;
                }
                if (sectionIndex == 2){
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);

        // 绑定 adapter
//        bindAdapter();

        bindAdapterData();

        return rootView;
    }

    private ShowTypeDetailClassifySection showTypeDetailClassifySection;
    private ShowTypeDetailClassifySection2 showTypeDetailClassifySection2;
    private ShowTypeDetailBannerSection showTypeDetailBannerSection;
    private SectionedRecyclerViewAdapter sectionAdapter;

    private void bindAdapterData () {
        LiveEventBus.get("showTypeDetails", List.class).observe(this, list -> {
            hotClassifies.clear();
            hotClassifies2.clear();
            bannerEntities.clear();

            for (Object obj : list){
                if (obj instanceof ShowTypeDetailService.HotClassify) {
                    hotClassifies.add((ShowTypeDetailService.HotClassify)obj);
                }
                if (obj instanceof ShowTypeDetailService.HotClassify2) {
                    hotClassifies2.add((ShowTypeDetailService.HotClassify2)obj);
                }
                if (obj instanceof ShowTypeDetailService.BannerEntity) {
                    bannerEntities.add((ShowTypeDetailService.BannerEntity)obj);
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
        ShowTypeDetailService.loadData();
    }

}
