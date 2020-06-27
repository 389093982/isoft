package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CloudBlogAdapter;
import com.linkknown.ilearning.adapter.CourseCardAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.BlogListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudBlogFragment extends BaseLazyLoadFragment{

    public static final int SCOP_ALL = 0;
    public static final int SCOP_MYSELF = 1;
    private int scop;
    private List<BlogListResponse.BlogArticle> blogs = new ArrayList<>();

    private Context mContext;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private CloudBlogAdapter baseQuickAdapter;
    private Handler handler = new Handler();
    private Paginator paginator;

    // 下拉全局刷新组件
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //博客查询的条件
    private String search_type = "";
    private String search_data = "";
    private String search_user_name = "";

    public CloudBlogFragment (int scop) {
        this.scop = scop;
    }

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        baseQuickAdapter = new CloudBlogAdapter(mContext, blogs);

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
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


    //查询博客 -- 网站里的博客可以根据很多条件去查询，search_type, 但app为了极简，暂时先查全部、内容匹配、自己
    private void executeLoadPageData(int current_page, int pageSize) {
        if (scop==SCOP_MYSELF){
            search_user_name = LoginUtil.getLoginUserName(mContext);
        }else if (scop == SCOP_ALL){
            search_type = "_all";
        }

        LinkKnownApiFactory.getLinkKnownApi().queryPageBlog(search_type,search_data,search_user_name,current_page,pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BlogListResponse>() {
                    @Override
                    public void onNext(BlogListResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getBlogs())){
                                if (current_page == 1) {
                                    // 先清空再添加
                                    blogs.clear();
                                }
                                blogs.addAll(o.getBlogs());
                                baseQuickAdapter.setList(blogs);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            }else{
                                if (current_page == 1) {
                                    blogs.clear();
                                    baseQuickAdapter.setList(blogs);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无记录");
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
                        Log.e("queryPageBlog error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }


    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_cloud_blog;
    }

    //查询博客
    public void doSearch(String search_data){
        this.search_data = search_data;
        initData();
    }

}
