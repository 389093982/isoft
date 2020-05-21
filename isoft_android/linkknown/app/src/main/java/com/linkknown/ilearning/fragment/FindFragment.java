package com.linkknown.ilearning.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseSearchActivity;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.ShowAndCloseMoreSection;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FindFragment extends BaseLazyLoadFragment implements ShowAndCloseMoreSection.ClickListener, CommonTagSection.ClickListener {
    private Context mContext;

    @BindView(R.id.tagRecyclerView)
    public RecyclerView tagRecyclerView;
    private SectionedRecyclerViewAdapter tagRecyclerViewAdapter;
    List<String> showTagList = new ArrayList<>();
    List<String> hideTagList = new ArrayList<>();

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        initTagView();
    }

    private void initTagView () {
        tagRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        showTagList = new ArrayList<>(Arrays.asList("docker", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2"));
        CommonTagSection showCommonTagSection = new CommonTagSection(mContext, this, showTagList);
        CommonTagSection hideCommonTagSection = new CommonTagSection(mContext, this, hideTagList);
        ShowAndCloseMoreSection showAndCloseMoreSection = new ShowAndCloseMoreSection(mContext, this);
        tagRecyclerViewAdapter.addSection(showCommonTagSection);
        tagRecyclerViewAdapter.addSection(hideCommonTagSection);
        tagRecyclerViewAdapter.addSection(showAndCloseMoreSection);
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tagRecyclerView.setAdapter(tagRecyclerViewAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void showMore() {
        hideTagList.addAll(new ArrayList<>(Arrays.asList("docker", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2")));
        tagRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeMore() {
        hideTagList.clear();
        tagRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 前往搜索界面
     */
    @OnClick(R.id.searchCardView)
    void startSearchActivity() {
        UIUtils.gotoActivity(mContext, CourseSearchActivity.class);
    }

    @Override
    public void onClickTag(String tagText) {
        UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
            intent.putExtra("search", tagText);
            return intent;
        });
    }
}
