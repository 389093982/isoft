package com.linkknown.ilearning.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseVideoAdapter extends BaseMultiItemQuickAdapter<CourseDetailResponse.MultiItemTypeCVideo, BaseViewHolder> {

    public static final int TYPE_LIST = R.layout.item_cvideo_list;
    public static final int TYPE_GRID = R.layout.item_cvideo_grd;

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
        // 根据返回的 type 分别设置数据
        switch (viewHolder.getItemViewType()) {
            // 列表类型展示
            case CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST:
                // 设置课程信息图片名称和描述等
                        viewHolder.setText(R.id.cVideoIndex, viewHolder.getAdapterPosition() + "");
                viewHolder.setText(R.id.cVideoName, multiItemTypeCVideo.getCVideo().getVideo_name());
                viewHolder.setText(R.id.cVideoDuration, multiItemTypeCVideo.getCVideo().getDuration() + "");
                break;
            // 网格类型展示
            case CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_GRID:
                // 设置课程信息
                viewHolder.setText(R.id.cVideoIndex, viewHolder.getAdapterPosition() + 1 + "");
                break;
            default:
                break;
        }
    }

}