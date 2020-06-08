package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseCustomTagAdapter extends BaseMultiItemQuickAdapter<CourseMetaResponse.MultiItemTypeCourseMeta, BaseViewHolder> implements LoadMoreModule {

    public static final int TYPE_LIST = R.layout.item_course_custom_tag_list;
    public static final int TYPE_GRID = R.layout.item_course_custom_tag_grid;

    private Context mContext;

    /**
     * 多布局 adapter
     * @param mContext
     * @param multiItemTypeCourseMetaList
     */
    public CourseCustomTagAdapter(Context mContext, List<CourseMetaResponse.MultiItemTypeCourseMeta> multiItemTypeCourseMetaList) {
        super(multiItemTypeCourseMetaList);
        // 绑定 layout 对应的 type
        addItemType(CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_LIST, TYPE_LIST);
        addItemType(CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_GRID, TYPE_GRID);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, CourseMetaResponse.MultiItemTypeCourseMeta multiItemTypeCourseMeta) {
        Log.e("图片==》", viewHolder.getAdapterPosition() + "==>" + multiItemTypeCourseMeta.getCourseMeta().getCourse_name() + "~~~~~" +  multiItemTypeCourseMeta.getCourseMeta().getSmall_image());
        // 根据返回的 type 分别设置数据
        switch (viewHolder.getItemViewType()) {
            case CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_LIST:
                // 设置课程信息图片名称和描述等
                UIUtils.setImage(mContext, viewHolder.findView(R.id.courseImageView), multiItemTypeCourseMeta.getCourseMeta().getSmall_image());
                viewHolder.setText(R.id.courseNameView, multiItemTypeCourseMeta.getCourseMeta().getCourse_name());
                viewHolder.setText(R.id.courseShortDescText, multiItemTypeCourseMeta.getCourseMeta().getCourse_short_desc());
                break;
            case CourseMetaResponse.MultiItemTypeCourseMeta.ITEM_TYPE_GRID:
                // 设置课程信息
                UIUtils.setImage(mContext, viewHolder.findView(R.id.courseImageView), multiItemTypeCourseMeta.getCourseMeta().getSmall_image(), 20);
                viewHolder.setText(R.id.courseNameView, multiItemTypeCourseMeta.getCourseMeta().getCourse_name());
                viewHolder.setText(R.id.userNameText, multiItemTypeCourseMeta.getCourseMeta().getCourse_author());
                break;
            default:
                break;
        }
    }
}
