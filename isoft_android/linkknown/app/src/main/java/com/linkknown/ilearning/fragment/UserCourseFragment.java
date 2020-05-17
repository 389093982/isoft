package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.api.LinkKnownApi;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.listener.OnLoadMoreListener;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.FavoriteResponse;
import com.linkknown.ilearning.model.HistoryResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// 用户关联课程类
public class UserCourseFragment extends BaseLazyLoadFragment {
    // 分别对应创建的课程、收藏的课程和观看的视频
    public static final int DISPLAY_TYPE_NEW = 0;
    public static final int DISPLAY_TYPE_FAVORITE = 1;
    public static final int DISPLAY_TYPE_VIEWED = 2;

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
    private int display_type;

    public UserCourseFragment (int display_type) {
        this.display_type = display_type;
    }

    @Override
    protected void initView(View mRootView) {
        Log.i("initView", "execute method: UserCourseFragment initView start");
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        userName = this.getArguments().getString(Constants.USER_NAME);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CourseHotRecommendSection courseHotRecommendSection = new CourseHotRecommendSection(mContext, courseMetaList);
        sectionedRecyclerViewAdapter.addSection(courseHotRecommendSection);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // header 显示 2 行
                if (sectionedRecyclerViewAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(sectionedRecyclerViewAdapter);

//        refreshLayout.setColorSchemeColors();
        refreshLayout.setOnRefreshListener(() -> {
            // 刷新中
            refreshLayout.setRefreshing(true);
            // 重新加载数据
            loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
        });

        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                    loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData(int current_page, int pageSize) {
        Observable<CourseMetaResponse> observable = getCourseMetaResponseObservable(current_page, pageSize);
        if (observable != null){
            observable.subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
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
    }

    // 获取请求数据
    private Observable<CourseMetaResponse> getCourseMetaResponseObservable(int current_page, int pageSize) {
        if (display_type == DISPLAY_TYPE_NEW) {
            return LinkKnownApiFactory.getLinkKnownApi().getCourseListByUserName(userName, current_page, pageSize);
        } else if (display_type == DISPLAY_TYPE_FAVORITE) {
            return LinkKnownApiFactory.getLinkKnownApi().getUserFavoriteList(userName, Constants.FAVORITE_TYPE_COURSE_COLLECT)
                    .flatMap((Function<FavoriteResponse, ObservableSource<CourseMetaResponse>>) favoriteResponse -> {
                        List<String> ids = new ArrayList<>();
                        for (FavoriteResponse.Favorite favorite : favoriteResponse.getFavorites()) {
                            ids.add(favorite.getFavorite_id() + "");
                        }
                        return LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(
                                StringUtils.join(ids.toArray(new String[]{}), ","));
                    });
        } else if (display_type == DISPLAY_TYPE_VIEWED) {
            return LinkKnownApiFactory.getLinkKnownApi().showCourseHistory()
                    .flatMap(new Function<HistoryResponse, ObservableSource<CourseMetaResponse>>() {
                        @Override
                        public ObservableSource<CourseMetaResponse> apply(HistoryResponse historyResponse) throws Exception {
                            List<String> ids = new ArrayList<>();
                            for (HistoryResponse.History history : historyResponse.getHistorys()) {
                                ids.add(history.getHistory_value());
                            }
                            return LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(
                                    StringUtils.join(ids.toArray(new String[]{}), ","));
                        }
                    });
        }
        return null;
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