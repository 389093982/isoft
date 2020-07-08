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
import com.linkknown.ilearning.adapter.LinkknownWithMeAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.model.UserLinkAgentResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyCustomerFragment extends BaseLazyLoadFragment{

    private Context mContext;
    private LinkknownWithMeAdapter baseQuickAdapter;

    private Handler handler = new Handler();
    private Paginator paginator;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //获取我的客户数据
    private List<UserLinkAgentResponse.UserLinkAgent> userLinkAgentList = new ArrayList<>();



    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_my_customer;
    }

    @Override
    protected void initView(View mRootView) {
        ButterKnife.bind(this, mRootView);
        this.mContext = getActivity();

        swipeRefreshLayoutHelper.bind(getContext(), refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        baseQuickAdapter = new LinkknownWithMeAdapter(mContext,userLinkAgentList);

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(baseQuickAdapter);

    }

    @Override
    protected void initData() {
        //网络请求
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
    };

    //加载数据
    private void executeLoadPageData(int current_page, int pageSize) {
        LinkKnownApiFactory.getLinkKnownApi().QueryUserLinkAgent(current_page,pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserLinkAgentResponse>() {
                    @Override
                    public void onNext(UserLinkAgentResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getUserLinkAgentList())){
                                if (current_page == 1) {
                                    // 先清空再添加
                                    userLinkAgentList.clear();
                                }
                                userLinkAgentList.addAll(o.getUserLinkAgentList());
                                baseQuickAdapter.setList(userLinkAgentList);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            }else{
                                if (current_page == 1) {
                                    userLinkAgentList.clear();
                                    baseQuickAdapter.setList(userLinkAgentList);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无客户");
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
                        Log.e("QueryUserLinkAgent:", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }


}
