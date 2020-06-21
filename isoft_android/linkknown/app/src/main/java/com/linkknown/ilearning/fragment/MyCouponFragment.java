package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.MyCouponAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MyCouponFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    private List<CouponListResponse.Coupon> couponList = new ArrayList<>();
    private MyCouponAdapter myCouponAdapter;

    // 分页信息
    private Paginator paginator;
    private Map<Integer,String> courseNameMap;

    private String isExpired;
    private String isUsed;

    @Override
    protected void initView(View mRootView) {
        mContext = getContext();
        ButterKnife.bind(this, mRootView);

        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        // 接收传递过来的参数
        isExpired = getArguments().getString("isExpired", "");
        isUsed = getArguments().getString("isUsed", "");

        myCouponAdapter =  new MyCouponAdapter(mContext, couponList);

        // 是否自动加载下一页（默认为true）
        myCouponAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        myCouponAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(myCouponAdapter);
    }

    private void loadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        myCouponAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().queryPersonalCouponList(isExpired,isUsed,current_page, pageSize)
                .flatMap(new Function<CouponListResponse, ObservableSource<CouponListResponse>>() {
                    @Override
                    public ObservableSource<CouponListResponse> apply(CouponListResponse couponListResponse) throws Exception {
                        String ids = "";
                        for (CouponListResponse.Coupon coupon: couponListResponse.getCoupons()){
                            if (coupon.getTarget_id() != null && !"".equals(coupon.getTarget_id())) {
                                ids += coupon.getTarget_id()+",";
                            }
                        }
                        return Observable.zip(Observable.just(couponListResponse),
                                LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(ids),
                                new BiFunction<CouponListResponse, CourseMetaResponse, CouponListResponse>() {
                                    @Override
                                    public CouponListResponse apply(CouponListResponse couponListResponse, CourseMetaResponse courseMetaResponse) throws Exception {
                                        for (CouponListResponse.Coupon coupon : couponListResponse.getCoupons()){
                                            for (CourseMetaResponse.CourseMeta courseMeta : courseMetaResponse.getCourses()){
                                                if (StringUtils.equals(coupon.getTarget_id(), courseMeta.getId() + "")){
                                                    coupon.setGood(courseMeta);
                                                }
                                            }
                                        }
                                        return couponListResponse;
                                    }
                                });
                    }
                })
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
                                myCouponAdapter.setList(couponList);
                            } else {
                                if (current_page == 1) {
                                    couponList.clear();
                                    myCouponAdapter.setList(couponList);
                                    myCouponAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = myCouponAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("没有匹配的优惠券");
                                }
                                myCouponAdapter.getLoadMoreModule().loadMoreComplete();
                                myCouponAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                            paginator = couponListResponse.getPaginator();

                            // 最后一页
                            if (CommonUtil.isLastPage(paginator)) {
                                myCouponAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                        } else {
                            myCouponAdapter.getLoadMoreModule().loadMoreFail();
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        myCouponAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }


    @Override
    protected void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_my_coupon;
    }
}
