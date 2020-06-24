package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CouponGoodActivity;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public class AvailableCouponForPayAdapter extends BaseQuickAdapter<SearchCouponForPayResponse.Coupon,BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    private CallBackListener listener;


    public AvailableCouponForPayAdapter(Context mContext, List<SearchCouponForPayResponse.Coupon> coupons) {
        super(R.layout.item_coupon, coupons);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, SearchCouponForPayResponse.Coupon coupon) {
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
            targetName.setText(coupon.getTarget_name());
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
        } else if (Integer.valueOf(DateUtil.Today_yyyyMMdd())>=Integer.valueOf(coupon.getStart_date())
                && Integer.valueOf(DateUtil.Today_yyyyMMdd())<=Integer.valueOf(coupon.getEnd_date())) {
            submitBtnView.setText("已领取");
            matrix.setSaturation(1);

            //因为是下单界面过来的， 所有这里选择优惠券后，还是要回到下单界面，去做金额上的计算
            viewHolder.findView(R.id.couponLayout).setOnClickListener(v -> listener.onClick(coupon));

        } else if (Integer.valueOf(DateUtil.Today_yyyyMMdd())>Integer.valueOf(coupon.getEnd_date())) {
            submitBtnView.setText("已过期");
            matrix.setSaturation(0);
        }
        submitBgView.setColorFilter(new ColorMatrixColorFilter(matrix));

    }


    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    public interface CallBackListener{
        void onClick(SearchCouponForPayResponse.Coupon coupon);
    }

}
