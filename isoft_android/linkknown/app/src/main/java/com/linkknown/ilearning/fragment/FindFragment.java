package com.linkknown.ilearning.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseSearchActivity;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.ShowAndCloseMoreSection;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindFragment extends BaseLazyLoadFragment implements ShowAndCloseMoreSection.ClickListener, CommonTagSection.ClickListener {
    private Context mContext;

    @BindView(R.id.tagRecyclerView)
    public RecyclerView tagRecyclerView;

    @BindView(R.id.searchTextView)
    public TextView searchTextView;
    private Disposable searchTextViewDisposable;
    private int showHintIndex = 0;

    private SectionedRecyclerViewAdapter tagRecyclerViewAdapter;
    List<String> showTagList = new ArrayList<>();
    List<String> hideTagList = new ArrayList<>();

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        initTagView();
        initSearchTextView();
    }

    private void initSearchTextView () {
        // 定时任务定时修改 TextView 中的提示文字
        searchTextViewDisposable = Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())                   // 在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(aLong -> {
                    searchTextView.setHint(Constants.COURSE_SEARCH_HINT_LIST.get(showHintIndex));
                    showHintIndex = showHintIndex == Constants.COURSE_SEARCH_HINT_LIST.size() - 1 ? 0 : ++ showHintIndex;
                });
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

    // 释放资源
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (searchTextViewDisposable != null) {
            searchTextViewDisposable.dispose();
        }
    }
}
