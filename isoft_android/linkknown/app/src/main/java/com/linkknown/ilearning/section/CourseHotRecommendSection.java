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
import org.apache.commons.lang3.StringUtils;

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
                .emptyResourceId(R.layout.layout_region_recommend_empty)
                .loadingResourceId(R.layout.layout_region_recommend_loading)
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

            if (StringUtils.isNotEmpty(courseMeta.getIsCharge())) {
                viewHolder.isCharge.setVisibility(View.VISIBLE);
                viewHolder.isCharge.setText(StringUtils.equals(courseMeta.getIsCharge(), "charge") ? "付费课程" : "免费");
            } else {
                viewHolder.isCharge.setVisibility(View.GONE);
            }
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

    @Override
    public RecyclerView.ViewHolder getEmptyViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindEmptyViewHolder(RecyclerView.ViewHolder holder) {
        EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
        viewHolder.emptyTipText.setText("什么也没有搜到啊");
    }

    @Override
    public RecyclerView.ViewHolder getLoadingViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindLoadingViewHolder(holder);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView courseImage;
        private TextView courseName;
        private TextView watchNumberText;
        private TextView courseNumberText;
        private TextView isCharge;

        public ItemViewHolder(View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseName = itemView.findViewById(R.id.courseName);
            watchNumberText = itemView.findViewById(R.id.watchNumberText);
            courseNumberText = itemView.findViewById(R.id.courseNumberText);
            isCharge = itemView.findViewById(R.id.isCharge);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView item_type_operate_text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            item_type_operate_text = itemView.findViewById(R.id.item_type_operate_text);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        private TextView emptyTipText;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            emptyTipText = itemView.findViewById(R.id.emptyTipText);
        }
    }
}
