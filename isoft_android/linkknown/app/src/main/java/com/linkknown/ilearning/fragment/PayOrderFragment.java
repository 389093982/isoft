package com.linkknown.ilearning.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.activity.PayOrderDetailActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderFragment extends BaseLazyLoadFragment{

    private BaseQuickAdapter baseQuickAdapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //展示范围scope
    private String scope;

    //获取订单数据
    private List<PayOrderResponse.PayOrder> orderList = new ArrayList<PayOrderResponse.PayOrder>();

    @Override
    protected void initView(View mRootView) {
        // 接收传递过来的参数
        scope = getArguments().getString("scope", "ALL");
        ButterKnife.bind(this, mRootView);
        swipeRefreshLayoutHelper.bind(getContext(), refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        baseQuickAdapter = new BaseQuickAdapter<PayOrderResponse.PayOrder, BaseViewHolder>(R.layout.item_pay_order_list,orderList) {
            @Override
            protected void convert(@NotNull BaseViewHolder viewHolder, PayOrderResponse.PayOrder payOrder) {
                UIUtils.setImage(getContext(),viewHolder.findView(R.id.goodsImg),payOrder.getGoods_img());
                viewHolder.setText(R.id.goodsDesc,payOrder.getGoods_desc());
                viewHolder.setText(R.id.paidAmount,"￥"+payOrder.getPaid_amount()+"");
                String payResult = payOrder.getPay_result();

                //成功和失败展示交易时间， 待支付和被取消展示下单时间
                if ("SUCCESS".equals(payResult) || "FAIL".equals(payResult)) {
                    String transTime = payOrder.getTrans_time();
                    viewHolder.setText(R.id.transTime,DateUtil.formatDate_StandardForm(transTime));
                    viewHolder.setGone(R.id.orderTime,true);
                    viewHolder.setVisible(R.id.transTime,true);
                }else if ("CANCELLED".equals(payResult) || "".equals(payResult.trim())){
                    String orderTime = payOrder.getOrder_time();
                    viewHolder.setText(R.id.orderTime,DateUtil.formatDate_StandardForm(orderTime));
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
                }
            }

            @Override
            public int getItemCount() {
                return orderList.size();
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(baseQuickAdapter);
    }

    @Override
    protected void initData() {
        //网络请求
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData(int current_page, int pageSize) {
        executeLoadPageData(current_page, pageSize, LoginUtil.getLoginUserName(getContext()));
    };

    //加载数据
    private void executeLoadPageData(int current_page, int pageSize,String user_name) {
        LinkKnownApiFactory.getLinkKnownApi().queryPayOrderList(current_page, pageSize,user_name,scope)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<PayOrderResponse>() {
                    @Override
                    public void onNext(PayOrderResponse payOrderResponse) {
                        if (payOrderResponse.isSuccess()) {
                            // 有数据
                            if (CollectionUtils.isNotEmpty(payOrderResponse.getOrders())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    orderList.clear();
                                }
                                orderList.addAll(payOrderResponse.getOrders());
                            }else{
                                orderList.clear();
                                ToastUtil.showText(getContext(),"未查到数据！");
                            }

                        } else {
                            orderList.clear();
                            ToastUtil.showText(getContext(),"未查到数据！");
                        }
                        baseQuickAdapter.notifyDataSetChanged();
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("searchPayOrders error", e.getMessage());
                        swipeRefreshLayoutHelper.finishRefreshing();

                        ToastUtil.showText(getContext(),"查询失败！");
                    }
                });
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_pay_order;
    }


}
