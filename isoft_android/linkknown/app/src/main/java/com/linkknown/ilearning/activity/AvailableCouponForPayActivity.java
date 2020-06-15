package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.AvailableCouponForPayAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AvailableCouponForPayActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Context mContext;
    public AvailableCouponForPayAdapter quickAdapter;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    //查询可用优惠券的条件信息
    private String userName;
    private String target_type;
    private String target_id;
    private String paid_amount;
    private String today;

    //查询可用优惠券
    private List<SearchCouponForPayResponse.Coupon> coupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_coupon_for_pay);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }


    public void initView(){
        initToolBar(toolbar,true,"可用优惠券");
        //准备查询参数
        userName = getIntent().getStringExtra("userName");
        target_type = getIntent().getStringExtra("target_type");
        target_id = getIntent().getStringExtra("target_id");
        paid_amount = getIntent().getStringExtra("paid_amount");
        today = getIntent().getStringExtra("today");

        LinkKnownApiFactory.getLinkKnownApi().SearchCouponForPay(userName,target_type, target_id,paid_amount, today)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<SearchCouponForPayResponse>() {

                    @Override
                    public void onNext(SearchCouponForPayResponse o) {
                        if (o.isSuccess()){
                            coupons = o.getCoupons();
                            if (CollectionUtils.isNotEmpty(coupons)){
                                //查询到可用优惠券之后，这里使用quickAdapter
                                quickAdapter = new AvailableCouponForPayAdapter(mContext,coupons);
                                quickAdapter.setListener(coupon -> {
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("coupon",coupon);
                                    intent.putExtra("bundle", bundle);
                                    setResult(200,intent);
                                    finish();
                                });

                                recyclerView.setAdapter(quickAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }else{
                                ToastUtil.showText(mContext,"没有可用优惠券");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("查可用优惠券失败 error", e.getMessage());
                        ToastUtil.showText(mContext,"查可用优惠券失败！");
                    }
                });

    };

    public void initData(){

    };

}
