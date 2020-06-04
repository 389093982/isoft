package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        transTime_textView.setText(UIUtils.formatDate_StandardForm(payOrder.getTrans_time()));
        goodsDesc_textView.setText(payOrder.getGoods_desc());
        paidAmount_textView.setText(payOrder.getPaid_amount()+"元");
        goodsOriginalPrice_textView.setText(payOrder.getGoods_original_price()+"元");

        if (payOrder.getActivity_type().equals("coupon")){
            activityType_textView.setText("优惠券");
        }

        if (payOrder.getPay_result().equals("SUCCESS")){
            payResult_textView.setText("支付成功");
        }else{
            payResult_textView.setText("支付失败");
        }


    }
}
