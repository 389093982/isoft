package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.PayOrderFragment;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Context mContext;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //获取购物车数据
    private List<PayShoppinpCartResponse.ShoppingCart> shoppingCartList = new ArrayList<PayShoppinpCartResponse.ShoppingCart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        mContext = this;
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());
        initView();
        initData();
    }


    //声明ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImg;
        TextView goodsDesc;
        TextView price;
        TextView youhuiTip;
        TextView addTime;
        TextView toPayBtn;
        TextView deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImg = itemView.findViewById(R.id.goodsImg);
            goodsDesc = itemView.findViewById(R.id.goodsDesc);
            price = itemView.findViewById(R.id.price);
            youhuiTip = itemView.findViewById(R.id.youhuiTip);
            addTime = itemView.findViewById(R.id.addTime);
            toPayBtn = itemView.findViewById(R.id.toPayBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }


    //初始化视图
    private void initView(){
        initToolBar(toolbar, true, "购物车");
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(mContext).inflate(R.layout.item_pay_shopping_cart_list, parent ,false);
                return new ShoppingCartActivity.ViewHolder(item);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder viewHolder = (ViewHolder) holder;
                UIUtils.setImage(mContext,viewHolder.goodsImg,shoppingCartList.get(position).getSmall_image());
                viewHolder.goodsDesc.setText(shoppingCartList.get(position).getCourse_name());
                //商品价格
                BigDecimal price = shoppingCartList.get(position).getPrice();
                viewHolder.price.setText("￥"+price);
                //加入购物车时候的价格
                BigDecimal goods_price_on_add = shoppingCartList.get(position).getGoods_price_on_add();
                viewHolder.youhuiTip.setVisibility(View.GONE);
                if (goods_price_on_add.compareTo(price)>0){
                    viewHolder.youhuiTip.setText("比加入时降:"+(goods_price_on_add.subtract(price))+"元");
                    viewHolder.youhuiTip.setVisibility(View.VISIBLE);
                }
                String addTime = DateUtil.formatDate_StandardForm(shoppingCartList.get(position).getAdd_time());
                viewHolder.addTime.setText(addTime.substring(0,10));
                //删除按钮
                viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goodsType = shoppingCartList.get(position).getGoods_type();
                        String goods_id = shoppingCartList.get(position).getGoods_id();
                        deleteFromShoppingCart(goodsType,goods_id);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return shoppingCartList.size();
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
        recyclerView.setAdapter(adapter);
    }

    //给视图加载接口数据
    private void initData(){
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    };

    private void loadPageData(int current_page, int pageSize) {
        executeLoadPageData(current_page, pageSize);
    };

    //加载数据
    private void executeLoadPageData(int current_page, int pageSize) {
        LinkKnownApiFactory.getLinkKnownApi().queryPayShoppingCartList(current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<PayShoppinpCartResponse>() {
                    @Override
                    public void onNext(PayShoppinpCartResponse payShoppinpCartResponse) {
                        if (payShoppinpCartResponse.isSuccess()) {
                            // 有数据
                            if (CollectionUtils.isNotEmpty(payShoppinpCartResponse.getGoodsData())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    shoppingCartList.clear();
                                }
                                shoppingCartList.addAll(payShoppinpCartResponse.getGoodsData());
                            }else{
                                shoppingCartList.clear();
                                ToastUtil.showText(mContext,"查不到数据！");
                            }

                        } else {
                            shoppingCartList.clear();
                            ToastUtil.showText(mContext,"查不到数据！");
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayoutHelper.finishRefreshing();
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ShoppingCartList error", e.getMessage());
                        swipeRefreshLayoutHelper.finishRefreshing();
                        ToastUtil.showText(mContext,"查询失败！");
                    }
                });
    }

    /**
     * 从购物车里删除
     * @param goodsType
     * @param goodsId
     */
    public void deleteFromShoppingCart(String goodsType, String goodsId){
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
