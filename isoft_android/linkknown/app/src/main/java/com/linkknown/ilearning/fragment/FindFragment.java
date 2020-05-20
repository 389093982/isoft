package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.ShowAndCloseMoreSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FindFragment extends BaseLazyLoadFragment implements ShowAndCloseMoreSection.ClickListener {
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
        showTagList = new ArrayList<>(Arrays.asList("测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2"));
        CommonTagSection showCommonTagSection = new CommonTagSection(mContext, showTagList);
        CommonTagSection hideCommonTagSection = new CommonTagSection(mContext, hideTagList);
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
        hideTagList.addAll(new ArrayList<>(Arrays.asList("测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2","测试1", "测试2")));
        tagRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeMore() {
        hideTagList.clear();
        tagRecyclerViewAdapter.notifyDataSetChanged();
    }
}
