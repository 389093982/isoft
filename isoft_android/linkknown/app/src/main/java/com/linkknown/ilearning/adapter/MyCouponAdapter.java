package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CouponGoodActivity;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

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
            CourseMetaResponse.CourseMeta courseMeta = (CourseMetaResponse.CourseMeta) coupon.getGood();
            // 防止课程无效为空
            if (courseMeta != null) {
                targetName.setText(courseMeta.getCourse_name());
                targetName.setTextColor(Color.BLUE);
            }
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
        } else if (Integer.valueOf(DateUtil.Today_yyyyMMdd())>=Integer.valueOf(coupon.getStart_date())
                && Integer.valueOf(DateUtil.Today_yyyyMMdd())<=Integer.valueOf(coupon.getEnd_date())) {
            submitBtnView.setText("去使用");
            matrix.setSaturation(1);
            if ("general".equals(coupon.getCoupon_type())){
                //指定券跳往课程列表
                viewHolder.findView(R.id.submitBgView).setOnClickListener(v -> UIUtils.gotoActivity(mContext, CouponGoodActivity.class,intent -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("coupon",coupon);
                    intent.putExtra("bundle",bundle);
                    return intent;
                }));
            }else if ("designated".equals(coupon.getCoupon_type()) && "course".equals(coupon.getTarget_type())){
                viewHolder.findView(R.id.submitBgView).setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id",Integer.valueOf(coupon.getTarget_id()));
                    return intent;
                }));
            }

        } else if (Integer.valueOf(DateUtil.Today_yyyyMMdd())>Integer.valueOf(coupon.getEnd_date())) {
            submitBtnView.setText("已过期");
            matrix.setSaturation(0);
        }
        submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }
};