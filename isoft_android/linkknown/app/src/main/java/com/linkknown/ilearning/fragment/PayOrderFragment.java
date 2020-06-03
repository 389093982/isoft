package com.linkknown.ilearning.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

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

    public boolean mIsRefreshing;

    //获取订单数据
    private List<PayOrderResponse.PayOrder> orderList = new ArrayList<PayOrderResponse.PayOrder>();

    @Override
    protected void initView(View mRootView) {
        ButterKnife.bind(this, mRootView);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mIsRefreshing==false){
                    initData();
                }
            }
        });

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_course_order , parent ,false);
                return new ViewHolder(item);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.order_id.setText(orderList.get(position).getOrder_id());
//                viewHolder.goodsType.setText(orderList.get(position).getGoodsType());
//                viewHolder.goodsPrice.setText(orderList.get(position).getGoodsPrice()+"");
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
        mIsRefreshing = true;
        //网络请求
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData(int current_page, int pageSize) {
        executeLoadPageData(current_page, pageSize, LoginUtil.getLoginUserName(getContext()));
    };

    //加载数据
    private void executeLoadPageData(int current_page, int pageSize,String user_name) {
        LinkKnownApiFactory.getLinkKnownApi().queryPayOrderList(current_page, pageSize,user_name)
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
                                ToastUtil.showText(getContext(),"刷新成功！");
                            }

                        } else {
                            orderList.clear();
                            ToastUtil.showText(getContext(),"查不到数据！");
                        }
                        adapter.notifyDataSetChanged();
                        finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("searchPayOrders error", e.getMessage());
                        finishRefreshing();
                        ToastUtil.showText(getContext(),"查询失败！");
                    }
                });
    }

    public void finishRefreshing(){
        mIsRefreshing = false;
        refreshLayout.setRefreshing(false);
    };

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_pay_order;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id;
        TextView transTime;
        TextView userName;
        TextView goodsType;
        TextView goodsId;
        TextView goodsDesc;
        TextView goodsPrice;
        TextView goodsImg;
        TextView payResult;
        TextView goodsOriginalPrice;
        TextView activityType;
        TextView activityTypeBindId;

        public ViewHolder(View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
//            transTime = itemView.findViewById(R.id.transTime);
//            userName = itemView.findViewById(R.id.userName);
//            goodsType = itemView.findViewById(R.id.goodsType);
//            goodsId = itemView.findViewById(R.id.goodsId);
//            goodsDesc = itemView.findViewById(R.id.goodsDesc);
//            goodsPrice = itemView.findViewById(R.id.goodsPrice);
//            goodsImg = itemView.findViewById(R.id.goodsImg);
//            payResult = itemView.findViewById(R.id.payResult);
//            goodsOriginalPrice = itemView.findViewById(R.id.goodsOriginalPrice);
//            activityType = itemView.findViewById(R.id.activityType);
//            activityTypeBindId = itemView.findViewById(R.id.activityTypeBindId);
        }
    }


}
