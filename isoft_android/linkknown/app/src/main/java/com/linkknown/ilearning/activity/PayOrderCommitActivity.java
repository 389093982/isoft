package com.linkknown.ilearning.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.base.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderCommitActivity extends BaseActivity{

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    //付费商品基本信息
    private String goodsType;
    private String goodsId;
    private String goodsImg;
    private String goodsDesc;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order_commit);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        initView();
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

    };


    //初始化数据
    public void initData(){

    };



    /**
     * 前去支付结算
     * @param goodsType
     * @param goodsId
     */
    public void toPayFromShoppingCart(String goodsType, String goodsId){
        LinkKnownApiFactory.getLinkKnownApi().deleteFromShoppingCart(goodsType, goodsId)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if ("SUCCESS".equals(baseResponse.getStatus())) {
                            ToastUtil.showText(mContext,"删除成功！");
                        }
                        initData();
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("deleteShoppCart error", e.getMessage());
                        ToastUtil.showText(mContext,"删除失败！");
                    }
                });
    };



}
