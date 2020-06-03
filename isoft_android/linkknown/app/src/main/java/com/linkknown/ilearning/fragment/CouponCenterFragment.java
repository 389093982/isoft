package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CouponCenterFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    private List<CouponListResponse.Coupon> couponList = new ArrayList<>();
    private BaseQuickAdapter baseQuickAdapter;

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

        baseQuickAdapter =  new BaseQuickAdapter<CouponListResponse.Coupon, BaseViewHolder>(R.layout.item_coupon, couponList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, CouponListResponse.Coupon coupon) {
                TextView youhuiTextView = viewHolder.findView(R.id.youhuiTextView);
                // 减免券
                TextView jianmianTextView = viewHolder.findView(R.id.jianmianTextView);
                if (StringUtils.equalsIgnoreCase(coupon.getYouhui_type(), "reduce")) {
                    youhuiTextView.setText(String.format("￥%s", coupon.getCoupon_amount()));
                    jianmianTextView.setVisibility(View.VISIBLE);
                    jianmianTextView.setText(String.format("满 %s 元减 %s 元", coupon.getGoods_min_amount(), coupon.getCoupon_amount()));
                } else {
                    youhuiTextView.setText(String.format("%s折", coupon.getDiscount_rate()));
                    jianmianTextView.setVisibility(View.GONE);
                }

                // 设置优惠券适用场景
                TextView couponSceneView = viewHolder.findView(R.id.couponSceneView);
                if (StringUtils.equalsIgnoreCase(coupon.getCoupon_type(), "general")) {
                    couponSceneView.setText("适用场景：链知 app 和 web 多端通用，全部课程");
                } else {
                    couponSceneView.setText("适用场景：只适用于部分场景奥~~");
                }
                // 设置有效期
                TextView endTimeView = viewHolder.findView(R.id.endTimeView);
                endTimeView.setText(String.format("有效期：至%s", DateUtil.formateYYYYMMDDToDate(coupon.getEnd_date())));

                // 设置右边按钮
                ColorMatrix matrix = new ColorMatrix();

                TextView submitBtnView = viewHolder.findView(R.id.submitBtnView);
                ImageView submitBgView = viewHolder.findView(R.id.submitBgView);
                if (StringUtils.equalsIgnoreCase(coupon.getCoupon_state(), "used")) {
                    submitBtnView.setText("已使用");
                    matrix.setSaturation(0);
                } else if (DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
                    submitBtnView.setText("已领取");
                    matrix.setSaturation(1);
                } else if (!DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
                    submitBtnView.setText("已过期");
                    matrix.setSaturation(0);
                }
                submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));


            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(baseQuickAdapter);
    }

    @Override
    protected void initData() {
        LinkKnownApiFactory.getLinkKnownApi().queryPersonalCouponList(isExpired,isUsed,1, Constants.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CouponListResponse>() {
                    @Override
                    public void onNext(CouponListResponse couponListResponse) {
                        if (couponListResponse.isSuccess()){
                            couponList.addAll(couponListResponse.getCoupons());
                            baseQuickAdapter.setList(couponList);
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }
                });
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_coupon_center;
    }
}
