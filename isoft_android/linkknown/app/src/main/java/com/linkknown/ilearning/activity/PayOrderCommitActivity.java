package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.SearchCouponForPayResponse;
import com.linkknown.ilearning.model.queryCouponByIdResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderCommitActivity extends BaseActivity{

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    //支付方式
    @BindView(R.id.payType)
    public TextView payType;
    //选择支付方式 图标
    @BindView(R.id.selectPayType)
    public ImageView selectPayType;
    //可用优惠券
    @BindView(R.id.availableCoupons)
    public TextView availableCoupons;
    //选择可用优惠券 图标
    @BindView(R.id.selectAvailableCoupons)
    public ImageView selectAvailableCoupons;

    //付费商品基本信息
    private String goodsType;
    private String goodsId;
    private String goodsImg;
    private String goodsDesc;
    private String price;

    //查询可用优惠券
    private List<SearchCouponForPayResponse.Coupon> coupons;
    //选择使用的优惠券
    private SearchCouponForPayResponse.Coupon coupon_onuse;


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
        ((TextView)findViewById(R.id.price)).setText(Constants.RMB + price);
        ((TextView)findViewById(R.id.paidAmount)).setText(Constants.RMB + price);
        ((TextView)findViewById(R.id.commitOrderBtn)).setText("微信支付");

        payType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showText(mContext,"仅支持微信支付");
            }
        });
        //点击查看支付方式
        selectPayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showText(mContext,"仅支持微信支付");
            }
        });

        //设置课程图片点击事件，跳往课程详情界面
        findViewById(R.id.goodsImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", Integer.valueOf(goodsId));
                    return intent;
                });
            }
        });

        //查询可用优惠券
        SearchCouponForPay();

        //支付
        findViewById(R.id.commitOrderBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.先查看课程是否已经买过
                // 2.如果存在未付款的订单，提示"您有一笔订单未付款，请前去付款，或取消订单"
                // 3.查看券是否可使用
                // 4.支付
                Validator_And_ToPay();
            }
        });

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
                                availableCoupons.setText("有优惠券可以使用");
                                //文字和图标都设置点击事件
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
                                //同上
                                selectAvailableCoupons.setOnClickListener(new View.OnClickListener() {
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
            coupon_onuse = (SearchCouponForPayResponse.Coupon) bundle.getSerializable("coupon");

            //拿到优惠券后 ,金额计算
            if ("reduce".equals(coupon_onuse.getYouhui_type())){
                availableCoupons.setText("-" + coupon_onuse.getCoupon_amount());
                BigDecimal amount = new BigDecimal(price).subtract(new BigDecimal(coupon_onuse.getCoupon_amount()));
                ((TextView)findViewById(R.id.paidAmount)).setText(Constants.RMB + amount.setScale(2, BigDecimal.ROUND_HALF_UP)+"");//保留两位小数
            }else if ("discount".equals(coupon_onuse.getYouhui_type())){
                BigDecimal res = new BigDecimal(coupon_onuse.getDiscount_rate()).multiply(new BigDecimal("10")).setScale(1,BigDecimal.ROUND_HALF_UP);
                availableCoupons.setText(""+res+"折");
                BigDecimal amount = new BigDecimal(price).multiply(new BigDecimal(coupon_onuse.getDiscount_rate()));
                ((TextView)findViewById(R.id.paidAmount)).setText(Constants.RMB + amount.setScale(2, BigDecimal.ROUND_HALF_UP)+"");//保留两位小数
            }
            //设置优惠为 红色
            availableCoupons.setTextColor(Color.RED);


        }
    }


    /**
     * 1.先查看课程是否已经买过
     * 2.如果存在未付款的订单，提示"您有一笔订单未付款，请前去付款，或取消订单"
     * 3.查看券是否可使用
     * 4.支付
     */
    public void Validator_And_ToPay(){
        //1.先查看课程是否已经买过
        String user_name = LoginUtil.getLoginUserName(mContext);
        String goods_type = goodsType;
        String goods_id = goodsId;
        String pay_result = "SUCCESS";
        Integer current_page = 1;
        Integer pageSize = 10;
        LinkKnownApiFactory.getLinkKnownApi().queryPayOrderList(current_page,pageSize,goods_type,goods_id,user_name,pay_result)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<PayOrderResponse>() {
                    @Override
                    public void onNext(PayOrderResponse o) {

                        if (o.isSuccess()){
                            if (o.getOrders().size()>0 && "SUCCESS".equals(o.getOrders().get(0).getPay_result())){
                                ToastUtil.showText(mContext,"该课程您已购买过，无需再次购买^_^");
                            }else{





                                //2.如果存在未付款的订单，提示"您有一笔订单未付款，请前去付款，或取消订单"
                                String user_name = LoginUtil.getLoginUserName(mContext);
                                String scope = "WAIT_FOR_PAY";
                                Integer current_page = 1;
                                Integer pageSize = 10;
                                LinkKnownApiFactory.getLinkKnownApi().queryPayOrderList(current_page,pageSize,user_name,scope)
                                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                                        .subscribe(new LinkKnownObserver<PayOrderResponse>() {
                                            @Override
                                            public void onNext(PayOrderResponse o) {

                                                if (o.isSuccess()){
                                                    if (o.getOrders().size()>0){
                                                        ToastUtil.showText(mContext,"您有待付款的订单，请先去处理！");
                                                    }else{




                                                        //3.查看券是否可使用
                                                        if (coupon_onuse!=null){
                                                            String coupon_id = coupon_onuse.getCoupon_id();
                                                            LinkKnownApiFactory.getLinkKnownApi().queryCouponById(coupon_id)
                                                                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                                                                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                                                                    .subscribe(new LinkKnownObserver<queryCouponByIdResponse>() {

                                                                        @Override
                                                                        public void onNext(queryCouponByIdResponse o) {
                                                                            if (o.isSuccess()){
                                                                                if ("used".equals(o.getCoupon().getCoupon_state())){
                                                                                    ToastUtil.showText(mContext,"当前券已被使用，请重新选择！");
                                                                                    initView();
                                                                                }else{

                                                                                    //4.微信支付
                                                                                    weChatPay();

                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onError(Throwable e) {
                                                                            Log.e("查看券是否可使用失败 error", e.getMessage());
                                                                            ToastUtil.showText(mContext,"查看券是否可使用失败！");
                                                                        }
                                                                    });
                                                        }else{
                                                            //微信支付
                                                            weChatPay();
                                                        }




                                                    }
                                                }
                                            }
                                            @Override
                                            public void onError(Throwable e) {
                                                Log.e("查询是否存在未付款订单 error", e.getMessage());
                                                ToastUtil.showText(mContext,"查询是否存在未付款订单失败！");
                                            }
                                        });





                            };
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("查询课程是否已购买 error", e.getMessage());
                        ToastUtil.showText(mContext,"查询课程是否已购买失败！");
                    }
                });
    };


    /**
     * 微信支付
     * 1.订单入库
     * 2.更新优惠券状态为已使用
     * 3.支付成功后更新订单状态
     * 4.再次更新优惠券状态为已使用
     */
    public void weChatPay(){
        //1.添加订单入pay_order
        String order_id = "支付系统生成订单号" + DateUtil.Today_yyyyMMdd();
        String user_name = LoginUtil.getLoginUserName(mContext);
        String goods_type = goodsType;
        String goods_id = goodsId;
        String goods_desc = goodsDesc;
        String paid_amount = ((TextView)findViewById(R.id.paidAmount)).getText().toString();
        String goods_original_price = price;

        String activity_type = "";
        String activity_type_bind_id = "";
        if (coupon_onuse!=null){
            activity_type = "coupon";
            activity_type_bind_id = coupon_onuse.getCoupon_id();
        }

        String goods_img = goodsImg;
        String pay_result = "Test_SUCCESS";
        String code_url = "no_code_url";
        //1.添加订单
        LinkKnownApiFactory.getLinkKnownApi().addPayOrder(order_id,user_name, goods_type,goods_id, goods_desc,paid_amount,goods_original_price,activity_type,activity_type_bind_id,goods_img,pay_result,code_url)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {

                    @Override
                    public void onNext(BaseResponse o) {

                        if (o.isSuccess()){
                            ToastUtil.showText(mContext,"订单添加成功！");

                            // 2.更新优惠券,这里是下单的时候需要更新一次。
                            if (coupon_onuse!=null) {
                                LinkKnownApiFactory.getLinkKnownApi().UpdateCouponIsUsed(LoginUtil.getLoginUserName(mContext),coupon_onuse.getCoupon_id())
                                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                                            @Override
                                            public void onNext(BaseResponse o) {
                                                if (o.isSuccess()){
                                                    ToastUtil.showText(mContext,"下单更新券为已使用！");
                                                }
                                            }
                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });
                            }

                            //3.调微信支付接口
                            ToastUtil.showText(mContext,"微信支付成功..");

                            //4.再次更新优惠券状态为已使用
                            //更新优惠券,这里是下单的时候需要更新一次。
                            if (coupon_onuse!=null) {
                                LinkKnownApiFactory.getLinkKnownApi().UpdateCouponIsUsed(LoginUtil.getLoginUserName(mContext),coupon_onuse.getCoupon_id())
                                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                                            @Override
                                            public void onNext(BaseResponse o) {
                                                if (o.isSuccess()){
                                                    ToastUtil.showText(mContext,"下单更新券为已使用！");
                                                }
                                            }
                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });
                            }


                        }else{
                            ToastUtil.showText(mContext,"添加订单失败..");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("添加订单失败 error", e.getMessage());
                        ToastUtil.showText(mContext,"添加订单失败！");
                    }
                });

    }

}
