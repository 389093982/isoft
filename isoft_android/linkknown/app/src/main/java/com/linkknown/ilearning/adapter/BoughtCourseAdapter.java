package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoughtCourseAdapter extends BaseQuickAdapter<PayOrderResponse.PayOrder, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public BoughtCourseAdapter(Context mContext, List<PayOrderResponse.PayOrder> data) {
        super(R.layout.item_bought_course_card_tag_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, PayOrderResponse.PayOrder payOrder) {
//       购买时候的图片
        UIUtils.setImage(mContext, viewHolder.findView(R.id.courseImg_boughtDate), payOrder.getGoods_img());
//       课程名称
        viewHolder.setText(R.id.courseName, payOrder.getGoods_desc());
//       课程描述
        viewHolder.setText(R.id.courseDesc, payOrder.getGoods_desc());
//       支付价格
        viewHolder.setText(R.id.paidAmount, Constants.RMB + payOrder.getPaid_amount());
//       交易时间(显示日期即可)
        viewHolder.setText(R.id.transTime, DateUtil.formatDate_StandardForm(payOrder.getTrans_time()).substring(0,10));

//       点击卡片去课程详情界面
        viewHolder.findView(R.id.courseImg_boughtDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(mContext, CourseDetailActivity.class,intent -> {
                    intent.putExtra("course_id",Integer.valueOf(payOrder.getGoods_id()));
                    return intent;
                });
            }
        });
    }
}
