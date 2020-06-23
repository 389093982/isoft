package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.activity.PayOrderCommitActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShoppingCartAdapter extends BaseQuickAdapter<PayShoppinpCartResponse.ShoppingCart, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public OnClickDelete onClickDelete;

    public ShoppingCartAdapter(Context mContext, List<PayShoppinpCartResponse.ShoppingCart> data) {
        super(R.layout.item_pay_shopping_cart_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, PayShoppinpCartResponse.ShoppingCart shoppingCart) {
        UIUtils.setImage(mContext,viewHolder.findView(R.id.goodsImg),shoppingCart.getSmall_image());
        viewHolder.setText(R.id.goodsDesc,shoppingCart.getCourse_name());
        //商品价格
        BigDecimal price = shoppingCart.getPrice();
        viewHolder.setText(R.id.price,Constants.RMB + price);
        //加入购物车时候的价格
        BigDecimal goods_price_on_add = shoppingCart.getGoods_price_on_add();
        viewHolder.setGone(R.id.youhuiTip,true);
        if (goods_price_on_add.compareTo(price)>0){
            viewHolder.setText(R.id.youhuiTip,"比加入时降:"+(goods_price_on_add.subtract(price))+"元");
            viewHolder.setVisible(R.id.youhuiTip,true);
        }
        String addTime = DateUtil.formatDate_StandardForm(shoppingCart.getAdd_time());
        viewHolder.setText(R.id.addTime,addTime.substring(0,10));
        //前去支付按钮
        viewHolder.findView(R.id.toPayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodsType = shoppingCart.getGoods_type();
                if ("course_theme_type".equals(goodsType)){
                    String goodsId = shoppingCart.getGoods_id();
                    String goodsImg = shoppingCart.getSmall_image(); //因为是课程，这里直接从 course表里获取图片
                    String courseName = shoppingCart.getCourse_name();
                    //1.去结算页面
                    UIUtils.gotoActivity(mContext, PayOrderCommitActivity.class, intent -> {
                        intent.putExtra("goodsType",goodsType);
                        intent.putExtra("goodsId",goodsId);
                        intent.putExtra("goodsImg",goodsImg);
                        intent.putExtra("goodsDesc",courseName);
                        intent.putExtra("price",""+price);
                        return intent;
                    });
                }
            }
        });
        //删除按钮
        viewHolder.findView(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodsType = shoppingCart.getGoods_type();
                String goodsId = shoppingCart.getGoods_id();
                onClickDelete.deleteGoods(goodsType,goodsId);
            }
        });

        //设置课程图片点击事件，跳往课程详情界面
        viewHolder.findView(R.id.goodsImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(getContext(), CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", Integer.valueOf(shoppingCart.getGoods_id()));
                    return intent;
                });
            }
        });
    }


    public interface OnClickDelete{
        public void deleteGoods(String goodsType,String goodsId);
    };

    public void setDeleteFromShoppingCart(OnClickDelete onClickDelete) {
        this.onClickDelete = onClickDelete;
    }
}
