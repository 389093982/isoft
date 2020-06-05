package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseCardAdapter extends BaseQuickAdapter<CourseMetaResponse.CourseMeta, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CourseCardAdapter(Context mContext, List<CourseMetaResponse.CourseMeta> courseMetas) {
        super(R.layout.layout_region_recommend_card_item, courseMetas);
        this.mContext = mContext;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @NotNull CourseMetaResponse.CourseMeta courseMeta) {
        viewHolder.setText(R.id.courseName, courseMeta.getCourse_name());
        viewHolder.setText(R.id.watchNumberText, courseMeta.getWatch_number() + "");
        viewHolder.setText(R.id.courseNumberText, courseMeta.getCourse_number() + "");

        UIUtils.setImage(mContext,  viewHolder.findView(R.id.courseImage), courseMeta.getSmall_image());
//
//        viewHolder.courseImage.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
//            intent.putExtra("course_id", courseMeta.getId());
//            return intent;
//        }));

        if (StringUtils.isNotEmpty(courseMeta.getIsCharge())) {
            viewHolder.setVisible(R.id.isCharge, true);
            viewHolder.setText(R.id.isCharge, StringUtils.equals(courseMeta.getIsCharge(), "charge") ? "付费课程" : "免费");
        } else {
            viewHolder.setVisible(R.id.isCharge, false);
        }

    }
}