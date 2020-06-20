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
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CouponGoodActivity;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyCouponAdapter extends BaseQuickAdapter<CouponListResponse.Coupon, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public MyCouponAdapter(Context mContext, List<CouponListResponse.Coupon> couponList) {
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
            youhuiTextView.setText(""+(new Float(coupon.getDiscount_rate())*10)+"折");
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
        endTimeView.setText("活动日期："+coupon.getStart_date() + " - " +coupon.getEnd_date());

        // 设置右边按钮
        ColorMatrix matrix = new ColorMatrix();

        TextView submitBtnView = viewHolder.findView(R.id.submitBtnView);
        ImageView submitBgView = viewHolder.findView(R.id.submitBgView);
        if (StringUtils.equalsIgnoreCase(coupon.getCoupon_state(), "used")) {
            submitBtnView.setText("已使用");
            matrix.setSaturation(0);
        } else if (DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
            submitBtnView.setText("去使用");
            matrix.setSaturation(1);
            viewHolder.findView(R.id.submitBgView).setOnClickListener(v -> UIUtils.gotoActivity(mContext, CouponGoodActivity.class));
        } else if (!DateUtil.isNowTimeBetween(coupon.getStart_date(), coupon.getEnd_date(), DateUtil.PATTERN2)) {
            submitBtnView.setText("已过期");
            matrix.setSaturation(0);
        }
        submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }
};