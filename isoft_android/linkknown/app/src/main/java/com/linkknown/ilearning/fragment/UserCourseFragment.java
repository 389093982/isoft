package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserCourseFragment extends BaseLazyLoadFragment {

    private Context mContext;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    // 下拉全局刷新组件
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    private String userName;
    // 分页信息
    private Paginator paginator;

    @Override
    protected void initView(View mRootView) {
        Log.i("initView", "execute method: UserCourseFragment initView start");
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        userName = this.getArguments().getString(Constants.USER_NAME);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CourseHotRecommendSection courseHotRecommendSection = new CourseHotRecommendSection(mContext, courseMetaList, () -> {
            Log.i("initView", "execute method: UserCourseFragment initView loadPageData start");
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        sectionedRecyclerViewAdapter.addSection(courseHotRecommendSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(sectionedRecyclerViewAdapter);

//        refreshLayout.setColorSchemeColors();
        refreshLayout.setOnRefreshListener(() -> {
            // 刷新中
            refreshLayout.setRefreshing(true);
            // 重新加载数据
            loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
        });
    }

    @Override
    protected void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData(int current_page, int pageSize) {
        LinkKnownApiFactory.getLinkKnownApi().getCourseListByUserName(userName, current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            if (current_page == 1) {
                                courseMetaList.clear();     // 加载第一页数据时需要先进行清空
                            }
                            courseMetaList.addAll(courseMetaResponse.getCourses());
                            sectionedRecyclerViewAdapter.notifyDataSetChanged();
                            paginator = courseMetaResponse.getPaginator();
                        }
                        // 取消刷新效果
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError=>", Constants.TIP_SYSTEM_ERROR);
                        ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_user_course;
    }
}
