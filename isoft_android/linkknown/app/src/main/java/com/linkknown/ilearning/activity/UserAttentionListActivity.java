package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.UserAttentionListAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.model.UserAttentionListResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserAttentionListActivity extends BaseActivity {

    private Handler handler = new Handler();

    // 关注类型
    public static final String ATTENTION_TYPE = "ATTENTION_TYPE";
    public static final String ATTENTION = "Attention";
    public static final String FEN_SI = "Fensi";

    private Context mContext;
    private String attentionType;
    private String attentionTypeName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    // 分页对象
    private Paginator paginator;

    private UserAttentionListAdapter baseQuickAdapter;
    private List<UserAttentionListResponse.QueryData> queryDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attention_list);

        mContext = this;
        ButterKnife.bind(this);

        attentionType = getIntent().getStringExtra(ATTENTION_TYPE);
        attentionTypeName = StringUtils.equals(attentionType, ATTENTION) ? "关注" : "粉丝";

        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());
        
        initView();
        initData();
    }

    private void initData() {
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
        executeLoadPageData(current_page, pageSize);
    }

    private void executeLoadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        baseQuickAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().queryAttentionOrFensi("user", attentionType, current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserAttentionListResponse>() {
                    @Override
                    public void onNext(UserAttentionListResponse o) {
                        if (o.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(o.getQueryDatas())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    queryDatas.clear();
                                }
                                queryDatas.addAll(o.getQueryDatas());
                                baseQuickAdapter.setList(queryDatas);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            } else {
                                if (current_page == 1) {
                                    queryDatas.clear();
                                    baseQuickAdapter.setList(queryDatas);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无" + attentionTypeName + "信息");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                            paginator = o.getPaginator();

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

    private void initView() {
        initToolBar(toolbar, true, "我的" + attentionTypeName);

        baseQuickAdapter = new UserAttentionListAdapter(mContext, queryDatas, attentionType);
        baseQuickAdapter.setListener(queryData -> {
            LinkKnownApiFactory.getLinkKnownApi().doAttention("user", queryData.getUser_name(),"off")
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse o) {
                            if (o.isSuccess()) {
                                // 刷新页面
                                initData();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showText(mContext,"系统异常");
                        }
                    });
        });
        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE2);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);
    }
}
