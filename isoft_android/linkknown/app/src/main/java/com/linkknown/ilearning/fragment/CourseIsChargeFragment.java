package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.section.CourseHotRecommendSection;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseIsChargeFragment extends BaseLazyLoadFragment {

    private Context mContext;
    private String isCharge;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();
    SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    CourseHotRecommendSection courseHotRecommendSection;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        // 从 activity 中接受参数
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isCharge = bundle.getString("isCharge");
        }

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        courseHotRecommendSection = new CourseHotRecommendSection(mContext, courseMetaList);

        sectionedRecyclerViewAdapter.addSection(courseHotRecommendSection);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // header 显示 2 行
                if (sectionedRecyclerViewAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER
                        || sectionedRecyclerViewAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_EMPTY
                        || sectionedRecyclerViewAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_LOADING) {
                    return 2;
                }
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    @Override
    protected void initData() {
        changeSectionState(courseHotRecommendSection, Section.State.LOADING);

        LinkKnownApiFactory.getLinkKnownApi().searchCourseList("", isCharge)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            // 有数据
                            if (CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                                // 先清空再添加
                                courseMetaList.clear();
                                courseMetaList.addAll(courseMetaResponse.getCourses());
                                // 修改状态为
                                changeSectionState(courseHotRecommendSection, Section.State.LOADED);
                            } else {
                                // 没数据
                                // 修改状态为
                                changeSectionState(courseHotRecommendSection, Section.State.EMPTY);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("searchCourseList error", e.getMessage());
                        changeSectionState(courseHotRecommendSection, Section.State.LOADED);
                    }
                });
    }

    private void changeSectionState (Section section, Section.State state) {
        SectionAdapter sectionAdapter = sectionedRecyclerViewAdapter.getAdapterForSection(section);
        // 改变当前 section 状态之前先存储当前 section 的信息
        final Section.State previousState = section.getState();
        final int previousItemsQty = section.getContentItemsTotal();
        final boolean hasHeader = section.hasHeader();
        if (state == Section.State.EMPTY) {
            // 移除头部
            section.setHasHeader(false);
            if (hasHeader) {
                sectionAdapter.notifyHeaderRemoved();
            }
            // 设置空状态
            section.setState(Section.State.EMPTY);
            // 根据先前状态进行设置
            if (previousState == Section.State.LOADED){
                // LOADED 状态转变有数量改变动画效果
                sectionAdapter.notifyStateChangedFromLoaded(previousItemsQty);
            } else {
                sectionAdapter.notifyNotLoadedStateChanged(previousState);
            }
        } else if (state == Section.State.LOADING) {
            // 移除头部
            section.setHasHeader(false);
            if (hasHeader) {
                sectionAdapter.notifyHeaderRemoved();
            }
            // 设置空状态
            section.setState(Section.State.LOADING);
            // 根据先前状态进行设置
            if (previousState == Section.State.LOADED){
                sectionAdapter.notifyStateChangedFromLoaded(previousItemsQty);
            } else {
                sectionAdapter.notifyNotLoadedStateChanged(previousState);
            }
        } else if (state == Section.State.LOADED) {
            // 添加头部
            section.setHasHeader(true);
            if (!hasHeader) {
                sectionAdapter.notifyHeaderInserted();
            }
            // 设置状态
            section.setState(Section.State.LOADED);
            sectionAdapter.notifyStateChangedToLoaded(previousState);
        }
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_ischarge;
    }
}
