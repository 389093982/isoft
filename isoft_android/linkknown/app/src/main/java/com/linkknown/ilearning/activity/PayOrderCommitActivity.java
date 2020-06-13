package com.linkknown.ilearning.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.base.ViewHolder;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderCommitActivity extends BaseActivity{

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.availableCoupons)
    public TextView availableCoupons;

    //付费商品基本信息
    private String goodsType;
    private String goodsId;
    private String goodsImg;
    private String goodsDesc;
    private String price;

    //查询可用优惠券
    private List<SearchCouponForPayResponse.Coupon> coupons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_commit);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }


    //初始化订单提交页面
    public void initView(){
        goodsType = getIntent().getStringExtra("goodsType");
        goodsId = getIntent().getStringExtra("goodsId");
        goodsImg = getIntent().getStringExtra("goodsImg");
        goodsDesc = getIntent().getStringExtra("goodsDesc");
        price = getIntent().getStringExtra("price");

        initToolBar(toolbar,true,"提交订单");
        UIUtils.setImage(mContext,findViewById(R.id.goodsImg),goodsImg);
        ((TextView)findViewById(R.id.goodsDesc)).setText(goodsDesc);
        ((TextView)findViewById(R.id.price)).setText(price);
        ((TextView)findViewById(R.id.paidAmount)).setText(price);

        //查询可用优惠券
        SearchCouponForPay();
    };


    //初始化数据
    public void initData(){

    };


    //调接口查可用优惠券
    public void SearchCouponForPay(){
        String userName = LoginUtil.getLoginUserName(mContext);
        String target_type = "course";
        String target_id = goodsId;
        String paid_amount = price;
        String today = DateUtil.Today_yyyyMMdd();

        LinkKnownApiFactory.getLinkKnownApi().SearchCouponForPay(userName,target_type, target_id,paid_amount, today)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<SearchCouponForPayResponse>() {

                    @Override
                    public void onNext(SearchCouponForPayResponse o) {
                        if (o.isSuccess()){
                            coupons = o.getCoupons();
                            if (CollectionUtils.isNotEmpty(coupons)){
                                availableCoupons.setText("点击选取可用优惠券...");
                                availableCoupons.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext,AvailableCouponForPayActivity.class);
                                        intent.putExtra("userName",userName);
                                        intent.putExtra("target_type",target_type);
                                        intent.putExtra("target_id",target_id);
                                        intent.putExtra("paid_amount",paid_amount);
                                        intent.putExtra("today",today);
                                        startActivityForResult(intent,199);
                                    }
                                });
                            }else{
                                availableCoupons.setText("无可用");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("查可用优惠券失败 error", e.getMessage());
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });

    };

    // 为了从 “选择优惠券界面” 回来 “本页面” 获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199 && resultCode == 200) {
            Bundle bundle = data.getBundleExtra("bundle");
            SearchCouponForPayResponse.Coupon coupon = (SearchCouponForPayResponse.Coupon) bundle.getSerializable("coupon");

            //拿到优惠券后 ,金额计算
            if ("reduce".equals(coupon.getYouhui_type())){
                ((TextView)findViewById(R.id.availableCoupons)).setText("-" + coupon.getCoupon_amount());
            }else if ("discount".equals(coupon.getYouhui_type())){
                ((TextView)findViewById(R.id.availableCoupons)).setText(""+(new Float(coupon.getDiscount_rate())*10)+"折");
            }
        }
    }

}
