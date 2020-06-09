package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CouponDatasResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.linkknown.ilearning.common.BaseApplication.getContext;

public class PayOrderDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.orderId)
    public TextView orderId_TextView;

    @BindView(R.id.transTime)
    public TextView transTime_textView;

    @BindView(R.id.goodsDesc)
    public TextView goodsDesc_textView;

    @BindView(R.id.paidAmount)
    public TextView paidAmount_textView;

    @BindView(R.id.goodsOriginalPrice)
    public TextView goodsOriginalPrice_textView;

    @BindView(R.id.activityType)
    public TextView activityType_textView;

    @BindView(R.id.payResult)
    public TextView payResult_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_detail);
        ButterKnife.bind(this);
        PayOrderResponse.PayOrder payOrder = (PayOrderResponse.PayOrder) getIntent().getSerializableExtra("payOrderDetail");
        initView(payOrder);
    }

    private void initView(PayOrderResponse.PayOrder payOrder){
        initToolBar(toolbar, true, "订单详情");
        orderId_TextView.setText(payOrder.getOrder_id());
        transTime_textView.setText(DateUtil.formatDate_StandardForm(payOrder.getTrans_time()));
        goodsDesc_textView.setText(payOrder.getGoods_desc());
        paidAmount_textView.setText(payOrder.getPaid_amount()+"元");
        goodsOriginalPrice_textView.setText(payOrder.getGoods_original_price()+"元");

        if (payOrder.getActivity_type().equals("coupon")){
            activityType_textView.setText("优惠券");
            //根据优惠券ID查询优惠券
            queryCouponById(payOrder.getActivity_type_bind_id());
        }

        if (payOrder.getPay_result().equals("SUCCESS")){
            payResult_textView.setText("支付成功");
        }else{
            payResult_textView.setText("支付失败");
        }


    }

    //根据优惠券ID查询优惠券
    public void queryCouponById(String coupon_id){
        LinkKnownApiFactory.getLinkKnownApi().QueryPagePayCoupon(coupon_id,1,10)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CouponDatasResponse>() {
                    @Override
                    public void onNext(CouponDatasResponse couponDatasResponse) {
                        if (couponDatasResponse.isSuccess()) {
                            CouponDatasResponse.Coupon coupon = couponDatasResponse.getCouponDatas().get(0);
                            // 有数据
                            if (coupon!=null) {
                                showCoupon(coupon);
                            }else{
                                ToastUtil.showText(getContext(),"未查到优惠券！");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("queryCoupon error", e.getMessage());
                        ToastUtil.showText(getContext(),"查询优惠券失败！");
                    }
                });
    }


    /**
     * 展示优惠券
     * @param coupon
     */
    public void showCoupon(CouponDatasResponse.Coupon coupon){
        findViewById(R.id.order_with_coupon).setVisibility(View.VISIBLE);
        TextView youhuiTextView = findViewById(R.id.youhuiTextView);
        // 减免券
        TextView jianmianTextView = findViewById(R.id.jianmianTextView);
        if (StringUtils.equalsIgnoreCase(coupon.getYouhui_type(), "reduce")) {
            youhuiTextView.setText(String.format("￥%s", coupon.getCoupon_amount()));
            jianmianTextView.setVisibility(View.VISIBLE);
            jianmianTextView.setText(String.format("满 %s 元减 %s 元", coupon.getGoods_min_amount(), coupon.getCoupon_amount()));
        } else {
            youhuiTextView.setText(String.format("%s折", coupon.getDiscount_rate()));
            jianmianTextView.setVisibility(View.GONE);
        }

        // 设置优惠券适用场景
        TextView couponSceneView = findViewById(R.id.couponSceneView);
        if (StringUtils.equalsIgnoreCase(coupon.getCoupon_type(), "general")) {
            couponSceneView.setText("适用场景：链知 app 和 web 多端通用，全部课程");
        } else {
            couponSceneView.setText("适用场景：只适用于部分场景奥~~");
        }
        // 设置有效期
        TextView endTimeView = findViewById(R.id.endTimeView);
        endTimeView.setText(String.format("有效期：至%s", DateUtil.formateYYYYMMDDToDate(coupon.getEnd_date())));

        // 设置右边按钮
        ColorMatrix matrix = new ColorMatrix();

        TextView submitBtnView = findViewById(R.id.submitBtnView);
        ImageView submitBgView = findViewById(R.id.submitBgView);
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
    };



}
