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
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CouponGoodAdapter extends BaseQuickAdapter<CouponCourseResponse.Course, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public CouponGoodAdapter(Context mContext, List<CouponCourseResponse.Course> data) {
        super(R.layout.item_goods_card_tag_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, CouponCourseResponse.Course course) {
//       设置课程信息图片名称和描述等
        UIUtils.setImage(mContext, viewHolder.findView(R.id.goodsImg), course.getSmall_image());
//      商品名称
        viewHolder.setText(R.id.goodsName, course.getCourse_name());
//        商品描述
        viewHolder.setText(R.id.goodsDesc, course.getCourse_short_desc());
//        观看次数
        viewHolder.setText(R.id.watchNumberText, course.getWatch_number()+"");
//        集数
        viewHolder.setText(R.id.courseNumberText, course.getCourse_number()+"");
//        价格
        viewHolder.setText(R.id.price, Constants.RMB + course.getPrice()+"");

//        划线价
        if ("Y".equals(course.getIs_show_old_price())){
            viewHolder.findView(R.id.old_price).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.old_price,Constants.RMB+course.getOld_price());
            ((TextView)viewHolder.findView(R.id.old_price)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
        }else{
            viewHolder.findView(R.id.old_price).setVisibility(View.GONE);
        }
//        点击卡片去课程详情界面
        viewHolder.findView(R.id.item_goods_card_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.gotoActivity(mContext, CourseDetailActivity.class,intent -> {
                    intent.putExtra("course_id",course.getId());
                    return intent;
                });
            }
        });
    }
}
