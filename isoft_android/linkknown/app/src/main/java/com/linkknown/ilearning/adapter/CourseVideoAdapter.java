package com.linkknown.ilearning.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseVideoAdapter extends BaseMultiItemQuickAdapter<CourseDetailResponse.MultiItemTypeCVideo, BaseViewHolder> {

    public static final int TYPE_LIST = R.layout.item_cvideo_list;
    public static final int TYPE_GRID = R.layout.item_cvideo_grd;

    // adapter 中改参数作用：里面含有 payOrder 订单信息，视频是否收费信息，必传
    private CourseDetailResponse courseDetailResponse;

    public void setCourseDetailResponse(CourseDetailResponse courseDetailResponse) {
        this.courseDetailResponse = courseDetailResponse;
    }

    private Context mContext;

    /**
     * 多布局 adapter
     */
    public CourseVideoAdapter(Context mContext, List<CourseDetailResponse.MultiItemTypeCVideo> multiItemTypeCVideos) {
        super(multiItemTypeCVideos);
        // 绑定 layout 对应的 type
        addItemType(CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_LIST, TYPE_LIST);
        addItemType(CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_GRID, TYPE_GRID);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, CourseDetailResponse.MultiItemTypeCVideo multiItemTypeCVideo) {
        // 是否购买标识
        boolean isBuy = checkBuy();
        // 前多少集免费
        int preListFree = courseDetailResponse.getCourse().getPreListFree();

        // 根据返回的 type 分别设置数据
        switch (viewHolder.getItemViewType()) {
            // 列表类型展示
            case CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST:
                // 设置课程信息图片名称和描述等
                viewHolder.setText(R.id.cVideoIndex, viewHolder.getAdapterPosition() + 1 + "");
                viewHolder.setText(R.id.cVideoName, multiItemTypeCVideo.getCVideo().getVideo_name());
                viewHolder.setText(R.id.cVideoDuration, multiItemTypeCVideo.getCVideo().getDuration() + "");

                if (isBuy) {
                    // 已购买隐藏付费标志
                    viewHolder.setVisible(R.id.isFree, false);
                } else {
                    // 未购买显示付费标志
                    viewHolder.setVisible(R.id.isFree, true);
                    if (viewHolder.getAdapterPosition() + 1 <= preListFree) {
                        viewHolder.setText(R.id.isFree, "免");
                        viewHolder.setBackgroundColor(R.id.isFree, UIUtils.getResourceColor(mContext, R.color.green));
                    } else {
                        viewHolder.setText(R.id.isFree, "付");
                        viewHolder.setBackgroundColor(R.id.isFree, UIUtils.getResourceColor(mContext, R.color.red));
                    }
                }
                break;

            // 网格类型展示
            case CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_GRID:
                // 设置课程信息
                viewHolder.setText(R.id.cVideoIndex, viewHolder.getAdapterPosition() + 1 + "");
                if (isBuy) {
                    // 已购买隐藏付费标志
                    viewHolder.setVisible(R.id.isFree, false);
                } else {
                    // 未购买显示付费标志
                    viewHolder.setVisible(R.id.isFree, true);
                    if (viewHolder.getAdapterPosition() + 1 <= preListFree) {
                        viewHolder.setText(R.id.isFree, "免");
                        viewHolder.setBackgroundColor(R.id.isFree, UIUtils.getResourceColor(mContext, R.color.green));
                    } else {
                        viewHolder.setText(R.id.isFree, "付");
                        viewHolder.setBackgroundColor(R.id.isFree, UIUtils.getResourceColor(mContext, R.color.red));
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean checkBuy() {
        PayOrderResponse.PayOrder payOrder = courseDetailResponse.getPayOrder();
        if (payOrder != null && StringUtils.equalsIgnoreCase(payOrder.getPay_result(), "SUCCESS")) {
            return true;
        }
        return false;
    }
}