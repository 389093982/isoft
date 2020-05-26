package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponCenterFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private List<Coupon> couponList = new ArrayList<>();
    private BaseQuickAdapter baseQuickAdapter;

    @Override
    protected void initView(View mRootView) {
        mContext = getContext();
        ButterKnife.bind(this, mRootView);
        baseQuickAdapter =  new BaseQuickAdapter<Coupon, BaseViewHolder>(R.layout.item_coupon, couponList) {

            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, Coupon coupon) {
//                ImageView couponBgView = viewHolder.findView(R.id.couponBgView);
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
        Coupon coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        coupon = new Coupon();
        couponList.add(coupon);
        baseQuickAdapter.setList(couponList);
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.layout_recycleview;
    }

    static class Coupon {

    }
}
