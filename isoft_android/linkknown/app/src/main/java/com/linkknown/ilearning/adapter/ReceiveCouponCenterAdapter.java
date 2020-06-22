package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class ReceiveCouponCenterAdapter extends BaseQuickAdapter<CouponListResponse.Coupon, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    private ListenerCallback listener;

    public ReceiveCouponCenterAdapter(Context mContext, List<CouponListResponse.Coupon> couponList, ListenerCallback listener) {
        super(R.layout.item_coupon, couponList);
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, CouponListResponse.Coupon coupon) {
        TextView youhuiTextView = viewHolder.findView(R.id.youhuiTextView);
        // 减免券
        TextView jianmianTextView = viewHolder.findView(R.id.jianmianTextView);
        if (StringUtils.equalsIgnoreCase(coupon.getYouhui_type(), "reduce")) {
            youhuiTextView.setText(String.format(Constants.RMB + "%s", coupon.getCoupon_amount()));
            jianmianTextView.setVisibility(View.VISIBLE);
            jianmianTextView.setText(String.format("满 %s 元减 %s 元", coupon.getGoods_min_amount(), coupon.getCoupon_amount()));
        } else {
            BigDecimal res = new BigDecimal(coupon.getDiscount_rate()).multiply(new BigDecimal("10")).setScale(1,BigDecimal.ROUND_HALF_UP);
            youhuiTextView.setText(""+res+"折");
            jianmianTextView.setVisibility(View.GONE);
        }

        // 券类型
        TextView couponType = viewHolder.findView(R.id.couponType);
        if (StringUtils.equalsIgnoreCase(coupon.getCoupon_type(), "general")) {
            couponType.setText("通用券");
        } else {
            couponType.setText("指定券");
        }

        // 适用于： 描述
        TextView targetName = viewHolder.findView(R.id.targetName);
        if (StringUtils.equalsIgnoreCase(coupon.getCoupon_type(), "general")) {
            targetName.setText("所有付费课程");
            targetName.setTextColor(Color.RED);
        } else {
            targetName.setText("牛人视频");
            targetName.setTextColor(Color.BLUE);
        }

        // 设置有效期
        TextView endTimeView = viewHolder.findView(R.id.endTimeView);
        endTimeView.setText(String.format("有效期：至%s", DateUtil.formateYYYYMMDDToDate(coupon.getEnd_date())));

        // 设置右边按钮
        ColorMatrix matrix = new ColorMatrix();

        TextView submitBtnView = viewHolder.findView(R.id.submitBtnView);
        ImageView submitBgView = viewHolder.findView(R.id.submitBgView);
        submitBtnView.setText("去领取");
        matrix.setSaturation(1);
        submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));

        // 注册点击事件
        submitBgView.setOnClickListener(v -> listener.onClickReciveCoupon(coupon.getActivity_id()));
    }

    public static interface ListenerCallback {
        void onClickReciveCoupon(String activity_id);
    }
};