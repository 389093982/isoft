package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.adapter.CourseCardAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseFilterFragment extends BaseLazyLoadFragment {

    private int spanCount;
    public CourseFilterFragment() {}

    public CourseFilterFragment(int spanCount) {
        this.spanCount = spanCount;
    }

    private Handler handler = new Handler();

    private Context mContext;
    private String search;
    private String isCharge;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private boolean mIsRefreshing = false;

    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();
    CourseCardAdapter courseCardAdapter;

    // 分页信息
    private Paginator paginator;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        // 从 activity 中接受参数
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            search = bundle.getString("search");
            isCharge = bundle.getString("isCharge");
        }

        // 注册事件监听
        registerListener();
    }

    private void registerListener () {
        // refreshLayout 设置刷新监听
        refreshLayout.setOnRefreshListener(() -> {
            if (canRefresh()) {
                refreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                initData();
            }
        });
    }

    private boolean canRefresh () {
        return mIsRefreshing == false;
    }

    protected void finishRefreshing() {
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    }

    // 初始化并绑定 adapter
    private void initAndBindRecyclerViewAdapter() {
        courseCardAdapter = new CourseCardAdapter(mContext, courseMetaList);
        // 是否自动加载下一页（默认为true）
        courseCardAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        courseCardAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        courseCardAdapter.addChildClickViewIds(R.id.courseImage);
        courseCardAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.courseImage) {
                UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", courseMetaList.get(position).getId());
                    return intent;
                });
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(mContext,spanCount > 0 ? spanCount : 2));
        recyclerView.setAdapter(courseCardAdapter);
    }

    @Override
    protected void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData(int current_page, int pageSize) {
        // adapter 只初始化一次, 而且只在发送请求前初始化
        if (courseCardAdapter == null) {
            initAndBindRecyclerViewAdapter();
        }

        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        courseCardAdapter.getLoadMoreModule().loadMoreToLoading();

        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }
    }

    private void executeLoadPageData(int current_page, int pageSize) {
        LinkKnownApiFactory.getLinkKnownApi().searchCourseList(search, isCharge, current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            // 有数据
                            if (CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    courseMetaList.clear();
                                }
                                // 加载成功后才设置头部
                                if (!courseCardAdapter.hasHeaderLayout()) {
                                    View headerView = getLayoutInflater().inflate(R.layout.layout_region_recommend_hot_head, recyclerView, false);
                                    // 指定添加位置
                                    courseCardAdapter.addHeaderView(headerView, 1);
                                }
                                courseMetaList.addAll(courseMetaResponse.getCourses());
                                // setNewInstance: 设置新的数据实例，替换原有内存引用
                                // setList: 不会替换原有的内存引用，只是替换内容
                                courseCardAdapter.setList(courseMetaList);
                                // 当前这次数据加载完毕，调用此方法
                                courseCardAdapter.getLoadMoreModule().loadMoreComplete();

                            } else {
                                if (current_page == 1) {
                                    courseMetaList.clear();
                                    courseCardAdapter.setList(courseMetaList);
                                    courseCardAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                }
                                courseCardAdapter.getLoadMoreModule().loadMoreComplete();
                                courseCardAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                            paginator = courseMetaResponse.getPaginator();

                            // 最后一页
                            if (isLastPage()) {
                                courseCardAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                        } else {
                            courseCardAdapter.getLoadMoreModule().loadMoreFail();
                        }
                        finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("searchCourseList error", e.getMessage());
                        finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        courseCardAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

    private boolean isLastPage () {
        return paginator != null && paginator.getCurrpage() == paginator.getTotalpages();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_filter;
    }

    public void refreshFragment (String search, String isCharge) {
        this.search = search;
        this.isCharge = isCharge;
        this.initData();
    }
}