package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CouponReceiveCenterAdapter extends BaseQuickAdapter<CouponListResponse.Coupon, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public CouponReceiveCenterAdapter(Context mContext, List<CouponListResponse.Coupon> couponList) {
        super(R.layout.item_coupon, couponList);
        this.mContext = mContext;
    }

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
        submitBtnView.setText("立即领取");
        matrix.setSaturation(1);
        submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }
};