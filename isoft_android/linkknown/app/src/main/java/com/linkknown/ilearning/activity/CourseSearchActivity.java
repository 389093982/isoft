package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.util.KeyBoardUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseSearchActivity extends BaseActivity {

    private Context mContext;
    private String search;

    @BindView(R.id.searchEditText)
    public EditText searchEditText;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();
    SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    CourseHotRecommendSection courseHotRecommendSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 解决安卓软键盘遮挡输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.activity_course_search);

        ButterKnife.bind(this);
        mContext = this;

        initView();

        initData();
    }

    private void initView() {
        initSearchText();
    }

    private void initSearchText() {
        search = getIntent().getStringExtra("search");
        if (StringUtils.isNotEmpty(search)) {
            searchEditText.setText(search);
        }

        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(searchEditText)
                .map(CharSequence::toString)
                .subscribe(s -> {
                    if (!TextUtils.isEmpty(s)) {
                        searchEditTextClear.setVisibility(View.VISIBLE);
                    } else {
                        searchEditTextClear.setVisibility(View.GONE);
                    }
                });
        // 清空输入框
        RxView.clicks(searchEditTextClear)
                .subscribe(aVoid -> searchEditText.setText(""));

        RxView.clicks(searchBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .map(aVoid -> searchEditText.getText().toString().trim())
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribe(s -> {
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchEditText, mContext);
//                    showSearchAnim();
//                    clearData();
                    search = s;
                    initSearchData();
                });

    }

    private void initSearchData() {
        if (StringUtils.isEmpty(search)) {
            return;
        }
        // 防止重复创建 adapter
        if (sectionedRecyclerViewAdapter == null) {
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
        changeSectionState(courseHotRecommendSection, Section.State.LOADING);

        LinkKnownApiFactory.getLinkKnownApi().searchCourseList(search)
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

    private void initData() {
        if (StringUtils.isNotEmpty(search)) {
            initSearchData();
        }
    }

    // 返回
    @OnClick(R.id.goback)
    public void goback() {
        finish();
    }
}
