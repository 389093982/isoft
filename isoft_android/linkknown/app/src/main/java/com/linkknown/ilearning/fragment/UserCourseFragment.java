package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseCardAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.OnLoadMoreListener;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.FavoriteResponse;
import com.linkknown.ilearning.model.HistoryResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private CourseCardAdapter baseQuickAdapter;

    private Handler handler = new Handler();
    private Paginator paginator;

    // 下拉全局刷新组件
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();

    private String userName;
    private int display_type;

    public UserCourseFragment (int display_type) {
        this.display_type = display_type;
    }


    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        userName = this.getArguments().getString(Constants.USER_NAME);
        baseQuickAdapter = new CourseCardAdapter(mContext, courseMetaList);

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(baseQuickAdapter);
    }


    //初始化数据
    @Override
    protected void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }


    private void loadPageData(int current_page, int pageSize) {
        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }
    }


    //查询课程
    private void executeLoadPageData(int current_page, int pageSize) {
        Observable<CourseMetaResponse> observable = getCourseMetaResponseObservable(current_page, pageSize);
        if (observable != null){
            observable.subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                        @Override
                        public void onNext(CourseMetaResponse o) {
                            if (o.isSuccess()){
                                if (CollectionUtils.isNotEmpty(o.getCourses())){
                                    if (current_page == 1) {
                                        // 先清空再添加
                                        courseMetaList.clear();
                                    }
                                    courseMetaList.addAll(o.getCourses());
                                    baseQuickAdapter.setList(courseMetaList);

                                    // 当前这次数据加载完毕，调用此方法
                                    baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                }else{
                                    if (current_page == 1) {
                                        courseMetaList.clear();
                                        baseQuickAdapter.setList(courseMetaList);
                                        baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                        TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                        emptyTipText.setText("暂无课程");
                                    }
                                    baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                    baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                                }

                                paginator = o.getPaginator();
                                // 最后一页
                                if (CommonUtil.isLastPage(paginator)) {
                                    baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                                }
                            }else{
                                baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                            }
                            swipeRefreshLayoutHelper.finishRefreshing();
                        };
                        @Override
                        public void onError(Throwable e) {
                            Log.e("ShowCourseDetail error", e.getMessage());
                            ToastUtil.showText(mContext,"查询失败！");
                            swipeRefreshLayoutHelper.finishRefreshing();
                            // 当前这次数据加载错误，调用此方法
                            baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                        }
                    });
        }else{
            baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }

    // 获取请求数据
    private Observable<CourseMetaResponse> getCourseMetaResponseObservable(int current_page, int pageSize) {
        if (display_type == DISPLAY_TYPE_NEW) {
            return LinkKnownApiFactory.getLinkKnownApi().getCourseListByUserName(userName, current_page, pageSize);
        } else if (display_type == DISPLAY_TYPE_FAVORITE) {
            return LinkKnownApiFactory.getLinkKnownApi().getUserFavoriteList(userName, Constants.FAVORITE_TYPE_COURSE_COLLECT,current_page, pageSize)
                    .flatMap((Function<FavoriteResponse, ObservableSource<CourseMetaResponse>>) favoriteResponse -> {
                        List<String> ids = new ArrayList<>();
                        for (FavoriteResponse.Favorite favorite : favoriteResponse.getFavorites()) {
                            ids.add(favorite.getFavorite_id() + "");
                        }
                        return LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(
                                StringUtils.join(ids.toArray(new String[]{}), ",")).flatMap((Function<CourseMetaResponse, ObservableSource<CourseMetaResponse>>) courseMetaResponse -> {
                            courseMetaResponse.setPaginator(favoriteResponse.getPaginator());
                            return Observable.just(courseMetaResponse);
                        });
                    });
        } else if (display_type == DISPLAY_TYPE_VIEWED) {
            return LinkKnownApiFactory.getLinkKnownApi().showCourseHistory(current_page, pageSize)
                    .flatMap(new Function<HistoryResponse, ObservableSource<CourseMetaResponse>>() {
                        @Override
                        public ObservableSource<CourseMetaResponse> apply(HistoryResponse historyResponse) throws Exception {
                            List<String> ids = new ArrayList<>();
                            for (HistoryResponse.History history : historyResponse.getHistorys()) {
                                ids.add(history.getHistory_value());
                            }
                            return LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(
                                    StringUtils.join(ids.toArray(new String[]{}), ",")).flatMap((Function<CourseMetaResponse, ObservableSource<CourseMetaResponse>>) courseMetaResponse -> {
                                        courseMetaResponse.setPaginator(historyResponse.getPaginator());
                                        return Observable.just(courseMetaResponse);
                                    });
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
