package com.linkknown.ilearning.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseCustomTagAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseCustomTagListActivity extends BaseActivity {

    private int itemType = CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_GRID;

    private Context mContext;
    private String custom_tag;
    private String custom_tag_name;
    private Handler handler = new Handler();

    CourseCustomTagAdapter baseQuickAdapter;
    // 课程数据数据
    private List<CourseMetaResponse.MultiItemTypeCourseMeta> courseMetaList = new ArrayList<>();
    // 分页对象
    private Paginator paginator;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //栅格布局
    private GridLayoutManager mGridLayoutManager;
    //列表布局
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_custom_tag_list);

        custom_tag = getIntent().getStringExtra("custom_tag");
        custom_tag_name = getIntent().getStringExtra("custom_tag_name");

        mContext = this;
        ButterKnife.bind(this);

        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        initView();
        initData();
    }

    private void initView() {
        initToolBar(toolbar, true, custom_tag_name);

        baseQuickAdapter = new CourseCustomTagAdapter(mContext, courseMetaList);
        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE2);
            }
        });
        // 先注册需要点击的子控件id
        baseQuickAdapter.addChildClickViewIds(R.id.courseImageView);
        baseQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.courseImageView) {
                // 调往课程详情页面
                UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", courseMetaList.get(position).getCourseMeta().getId());
                    return intent;
                });
            }
        });

        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(baseQuickAdapter);
    }

    private void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE2);
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

    private void executeLoadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        baseQuickAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().queryCustomTagCourse(custom_tag, current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {

                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    courseMetaList.clear();
                                }
                                courseMetaList.addAll(CourseMetaResponse.MultiItemTypeCourseMeta.setItemType(courseMetaResponse.getCourses(), itemType));
                                baseQuickAdapter.setList(courseMetaList);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            } else {
                                if (current_page == 1) {
                                    courseMetaList.clear();
                                    baseQuickAdapter.setList(courseMetaList);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无匹配的课程信息");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                            paginator = courseMetaResponse.getPaginator();

                            // 最后一页
                            if (CommonUtil.isLastPage(paginator)) {
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                        } else {
                            baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_grid_list, menu);
        MenuItem grid = menu.findItem(R.id.menu_grid);
        MenuItem list = menu.findItem(R.id.menu_list);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            grid.setVisible(false);
            list.setVisible(true);
        } else {
            grid.setVisible(true);
            list.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        if (id == R.id.menu_grid) {
            itemType = CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_GRID;
            recyclerView.setLayoutManager(mGridLayoutManager);
            baseQuickAdapter.setList(CourseMetaResponse.MultiItemTypeCourseMeta.modifyItemType(courseMetaList, itemType));

            baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
            if (CommonUtil.isLastPage(paginator)) {
                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
            }
            invalidateOptionsMenu();
            return true;
        }
        if (id == R.id.menu_list) {
            itemType = CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_LIST;
            recyclerView.setLayoutManager(mLinearLayoutManager);
            baseQuickAdapter.setList(CourseMetaResponse.MultiItemTypeCourseMeta.modifyItemType(courseMetaList, itemType));

            baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
            if (CommonUtil.isLastPage(paginator)) {
                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
            }
            invalidateOptionsMenu();
            return true;
        }
        return true;
    }
}
