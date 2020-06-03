package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.api.LinkKnownApi;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CouponCenterFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private List<CouponListResponse.Coupon> couponList = new ArrayList<>();
    private BaseQuickAdapter baseQuickAdapter;

    private String isExpired;
    private String isUsed;

    @Override
    protected void initView(View mRootView) {
        mContext = getContext();
        ButterKnife.bind(this, mRootView);

        // 接收传递过来的参数
        isExpired = getArguments().getString("isExpired", "");
        isUsed = getArguments().getString("isUsed", "");

        baseQuickAdapter =  new BaseQuickAdapter<CouponListResponse.Coupon, BaseViewHolder>(R.layout.item_coupon, couponList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, CouponListResponse.Coupon coupon) {
                TextView submitBtnView = viewHolder.findView(R.id.submitBtnView);
                if (StringUtils.equalsIgnoreCase(coupon.getCoupon_state(), "used")){
                    submitBtnView.setText("已使用");
                } else if (DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
                    submitBtnView.setText("已领取");
                } else if (!DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
                    submitBtnView.setText("已过期");
                }

//                ColorMatrix matrix = new ColorMatrix();
//                matrix.setSaturation(0);
//                couponBgView.setColorFilter(new ColorMatrixColorFilter(matrix));
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
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.layout_recycleview;
    }
}
