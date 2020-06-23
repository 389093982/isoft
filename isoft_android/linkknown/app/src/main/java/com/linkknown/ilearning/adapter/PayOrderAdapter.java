package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.activity.PayOrderDetailActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderAdapter extends BaseQuickAdapter<PayOrderResponse.PayOrder, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    public OnClickCancle onClickCancle;

    public PayOrderAdapter(Context mContext, List<PayOrderResponse.PayOrder> data) {
        super(R.layout.item_pay_order_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, PayOrderResponse.PayOrder payOrder) {
        UIUtils.setImage(getContext(),viewHolder.findView(R.id.goodsImg),payOrder.getGoods_img());
        viewHolder.setText(R.id.goodsDesc,payOrder.getGoods_desc());
        viewHolder.setText(R.id.paidAmount,Constants.RMB + payOrder.getPaid_amount()+"");
        String payResult = payOrder.getPay_result();

        //成功和失败展示交易时间， 待支付和被取消展示下单时间
        if ("SUCCESS".equals(payResult) || "FAIL".equals(payResult)) {
            String transTime = payOrder.getTrans_time();
            viewHolder.setText(R.id.transTime, DateUtil.formatDate_StandardForm(transTime).substring(0,10));
            viewHolder.setGone(R.id.orderTime,true);
            viewHolder.setVisible(R.id.transTime,true);
        }else if ("CANCELLED".equals(payResult) || "".equals(payResult.trim())){
            String orderTime = payOrder.getOrder_time();
            viewHolder.setText(R.id.orderTime,DateUtil.formatDate_StandardForm(orderTime).substring(0,10));
            viewHolder.setGone(R.id.transTime,true);
            viewHolder.setVisible(R.id.orderTime,true);
        }

        //只有交易成功和失败可以看到 "订单详情" 按钮
        if ("SUCCESS".equals(payResult) || "FAIL".equals(payResult)){
            viewHolder.setVisible(R.id.queryDeatilBtn,true);
            viewHolder.findView(R.id.queryDeatilBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.gotoActivity(getContext(), PayOrderDetailActivity.class, new UIUtils.IntentParamWrapper() {
                        @Override
                        public Intent wrapper(Intent intent) {
                            Gson gson = new Gson();
                            intent.putExtra("payOrderDetail",payOrder);
                            return intent;
                        }
                    });
                }
            });
        }else{
            viewHolder.setGone(R.id.queryDeatilBtn,true);
        }

        //只有待支付状态，才能看到 "前去支付" 按钮
        if ("".equals(payResult)){
            viewHolder.setVisible(R.id.toPayBtn,true);
            viewHolder.findView(R.id.toPayBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //这里就查询一个课程。
                    List<String> ids = new ArrayList<>();
                    ids.add(payOrder.getGoods_id());
                    LinkKnownApiFactory.getLinkKnownApi().getCourseListByIds(StringUtils.join(ids.toArray(new String[]{}), ","))
                            .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                            .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                                @Override
                                public void onNext(CourseMetaResponse courseMetaResponse) {
                                    if (courseMetaResponse.isSuccess()){
                                        //1.去结算页面
                                        UIUtils.gotoActivity(getContext(), PayOrderCommitActivity.class, intent -> {
                                            intent.putExtra("goodsType",payOrder.getGoods_type());
                                            intent.putExtra("goodsId",payOrder.getGoods_id());
                                            intent.putExtra("goodsImg",courseMetaResponse.getCourses().get(0).getSmall_image());
                                            intent.putExtra("goodsDesc",courseMetaResponse.getCourses().get(0).getCourse_name());
                                            intent.putExtra("price",""+courseMetaResponse.getCourses().get(0).getPrice());
                                            return intent;
                                        });
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtil.showText(getContext(),"查询失败！");
                                }
                            });

                }
            });
        }else{
            viewHolder.setGone(R.id.toPayBtn,true);
        }


        //只有待付款状态的订单才能看到 "取消订单" 按钮
        if ("".equals(payResult)){
            viewHolder.setVisible(R.id.cancelOrder,true);
            viewHolder.findView(R.id.cancelOrder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCancle.cancleOrder(payOrder.getOrder_id());
                }
            });

        }else{
            viewHolder.setGone(R.id.cancelOrder,true);
        }


        //设置支付结果图标
        if("SUCCESS".equals(payResult)){
            viewHolder.setImageResource(R.id.payResultIcon,R.drawable.ic_pay_result_success);
        }else if ("FAIL".equals(payResult)){
            viewHolder.setImageResource(R.id.payResultIcon,R.drawable.ic_pay_result_fail);
        }else if ("".equals(payResult)){
            viewHolder.setImageResource(R.id.payResultIcon,R.drawable.ic_pay_result_wait_for_pay);
        }else if ("CANCELLED".equals(payResult)){
            viewHolder.setImageResource(R.id.payResultIcon,R.drawable.ic_pay_result_cancel);
        }

        //设置课程图片点击事件，跳往课程详情界面
        viewHolder.findView(R.id.goodsImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(getContext(), CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", Integer.valueOf(payOrder.getGoods_id()));
                    return intent;
                });
            }
        });
    }


    public interface OnClickCancle{
        public void cancleOrder(String order_id);
    }

    public void setOnClickCancle(OnClickCancle onClickCancle) {
        this.onClickCancle = onClickCancle;
    }
}
