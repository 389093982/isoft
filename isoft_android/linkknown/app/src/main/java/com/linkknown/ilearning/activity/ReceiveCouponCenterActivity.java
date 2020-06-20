package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CouponReceiveCenterAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.dialog.WaitingDialog;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReceiveCouponCenterActivity extends BaseActivity {

    private WaitingDialog waitingDialog;

   private Context mContext;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper =  new SwipeRefreshLayoutHelper();

    // 分页信息
    private Paginator paginator;

    private List<CouponListResponse.Coupon> couponList = new ArrayList<>();
    private BaseQuickAdapter baseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_receive_center);

        mContext = this;
        ButterKnife.bind(this);

        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        waitingDialog = new WaitingDialog();
        initView();

        initData();
    }

    private void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void initView() {
        initToolBar(toolbar, true, "领券中心");

        baseQuickAdapter =  new CouponReceiveCenterAdapter(mContext, couponList, activity_id -> receiveCoupon(activity_id));

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);
    }

    private void receiveCoupon(String activity_id) {
        waitingDialog.showDialog(this);

        LinkKnownApiFactory.getLinkKnownApi().receiveCoupon(activity_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtil.showText(mContext, "领取成功！");
                        } else {
                            ToastUtil.showText(mContext, baseResponse.getErrorMsg());
                        }
                        waitingDialog.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (LoginUtil.isNotUnauthorized(e)) {
                            ToastUtil.showText(mContext, "领取失败！");
                        }
                        waitingDialog.dismissDialog();
                    }
                });
    }

    private void loadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        baseQuickAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().queryCouponCenterList(current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CouponListResponse>() {

                    @Override
                    public void onNext(CouponListResponse couponListResponse) {
                        if (couponListResponse.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(couponListResponse.getCoupons())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    couponList.clear();
                                }
                                couponList.addAll(couponListResponse.getCoupons());
                                baseQuickAdapter.setList(couponList);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            } else {
                                if (current_page == 1) {
                                    couponList.clear();
                                    baseQuickAdapter.setList(couponList);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("没有匹配的优惠券");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                            paginator = couponListResponse.getPaginator();

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

}
