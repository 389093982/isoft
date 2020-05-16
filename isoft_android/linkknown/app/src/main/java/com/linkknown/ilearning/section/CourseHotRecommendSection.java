package com.linkknown.ilearning.section;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CourseHotRecommendSection extends Section {

    private List<CourseMetaResponse.CourseMeta> itemList;
    private Context mContext;

    public CourseHotRecommendSection(Context mContext, List<CourseMetaResponse.CourseMeta> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_region_recommend_card_item)
                .headerResourceId(R.layout.layout_region_recommend_hot_head)
                .build());
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public int getContentItemsTotal() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        Log.i("getItemViewHolder", "execute method: CourseHotRecommendSection getItemViewHolder start");
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (CollectionUtils.isNotEmpty(itemList)) {
            Log.i("onBindItemViewHolder", "execute method: CourseHotRecommendSection onBindItemViewHolder start");
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            CourseMetaResponse.CourseMeta courseMeta = itemList.get(position);

            UIUtils.setImage(mContext,  viewHolder.courseImage, courseMeta.getSmall_image());
            viewHolder.courseName.setText(courseMeta.getCourse_name());
            viewHolder.watchNumberText.setText(courseMeta.getWatch_number() + "");
            viewHolder.courseNumberText.setText(courseMeta.getCourse_number() + "");
            viewHolder.courseImage.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                intent.putExtra("course_id", courseMeta.getId());
                return intent;
            }));
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        UIUtils.setTextViewDrawbleImg(mContext, viewHolder.item_type_operate_text, R.drawable.ic_header_indicator_rank, 0, 0, 40, 40);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView courseImage;
        private TextView courseName;
        private TextView watchNumberText;
        private TextView courseNumberText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseName = itemView.findViewById(R.id.courseName);
            watchNumberText = itemView.findViewById(R.id.watchNumberText);
            courseNumberText = itemView.findViewById(R.id.courseNumberText);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView item_type_operate_text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            item_type_operate_text = itemView.findViewById(R.id.item_type_operate_text);
        }
    }

}
