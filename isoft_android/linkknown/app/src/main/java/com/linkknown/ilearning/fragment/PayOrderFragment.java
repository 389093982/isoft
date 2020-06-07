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

import com.google.gson.Gson;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.PayOrderDetailActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayOrderFragment extends BaseLazyLoadFragment{

    private RecyclerView.Adapter adapter;

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

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_pay_order_list, parent ,false);
                return new ViewHolder(item);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder viewHolder = (ViewHolder) holder;
                UIUtils.setImage(getContext(),viewHolder.goodsImg,orderList.get(position).getGoods_img());
                viewHolder.goodsDesc.setText(orderList.get(position).getGoods_desc());
                viewHolder.paidAmount.setText("￥"+orderList.get(position).getPaid_amount()+"");
                String payResult = orderList.get(position).getPay_result();

                //成功和失败展示交易时间， 待支付和被取消展示下单时间
                if ("SUCCESS".equals(payResult) || "FAIL".equals(payResult)) {
                    String transTime = orderList.get(position).getTrans_time();
                    viewHolder.transTime.setText(DateUtil.formatDate_StandardForm(transTime));
                    viewHolder.orderTime.setVisibility(View.GONE);
                    viewHolder.transTime.setVisibility(View.VISIBLE);
                }else if ("CANCELLED".equals(payResult) || "".equals(payResult.trim())){
                    String orderTime = orderList.get(position).getOrder_time();
                    viewHolder.orderTime.setText(DateUtil.formatDate_StandardForm(orderTime));
                    viewHolder.transTime.setVisibility(View.GONE);
                    viewHolder.orderTime.setVisibility(View.VISIBLE);
                }

                //只有交易成功和失败可以看到 "订单详情" 按钮
                if ("SUCCESS".equals(payResult) || "FAIL".equals(payResult)){
                    viewHolder.queryDeatilBtn.setVisibility(View.VISIBLE);
                    viewHolder.queryDeatilBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIUtils.gotoActivity(getContext(), PayOrderDetailActivity.class, new UIUtils.IntentParamWrapper() {
                                @Override
                                public Intent wrapper(Intent intent) {
                                    Gson gson = new Gson();
                                    intent.putExtra("payOrderDetail",orderList.get(position));
                                    return intent;
                                }
                            });
                        }
                    });
                }else{
                    viewHolder.queryDeatilBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public int getItemCount() {
                return orderList.size();
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);
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
                                ToastUtil.showText(getContext(),"查不到数据！");
                            }

                        } else {
                            orderList.clear();
                            ToastUtil.showText(getContext(),"查不到数据！");
                        }
                        adapter.notifyDataSetChanged();
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImg;
        TextView goodsDesc;
        TextView paidAmount;
        TextView transTime;
        TextView orderTime;
        TextView queryDeatilBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImg = itemView.findViewById(R.id.goodsImg);
            goodsDesc = itemView.findViewById(R.id.goodsDesc);
            paidAmount = itemView.findViewById(R.id.paidAmount);
            transTime = itemView.findViewById(R.id.transTime);
            orderTime = itemView.findViewById(R.id.orderTime);
            queryDeatilBtn = itemView.findViewById(R.id.queryDeatilBtn);
        }
    }


}
