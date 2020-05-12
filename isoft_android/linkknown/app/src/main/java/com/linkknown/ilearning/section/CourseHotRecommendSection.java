package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class CourseHotRecommendSection extends Section {

    private List<CourseMetaResponse.CourseMeta> itemList;
    private Context mContext;

    public CourseHotRecommendSection(Context mContext, List<CourseMetaResponse.CourseMeta> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_recycleview)
                .headerResourceId(R.layout.layout_region_recommend_hot_head)
                .build());
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(CourseMetaResponse.CourseMeta.class, new MultiItemView<CourseMetaResponse.CourseMeta>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.layout_region_recommend_card_item;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CourseMetaResponse.CourseMeta courseMeta, int i) {
                ImageView courseImageView = viewHolder.getConvertView().findViewById(R.id.courseImage);
                UIUtils.setImage(mContext, courseImageView, courseMeta.getSmall_image());
                viewHolder.setText(R.id.courseName, courseMeta.getCourse_name());
                viewHolder.setText(R.id.watchNumberText, courseMeta.getWatch_number() + "");
                viewHolder.setText(R.id.courseNumberText, courseMeta.getCourse_number() + "");

                courseImageView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                    intent.putExtra("course_id", courseMeta.getId());
                    return intent;
                }));
            }
        });
        multiTypeAdapter.setItems(itemList);
        itemHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        itemHolder.recyclerView.setAdapter(multiTypeAdapter);
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

        private RecyclerView recyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
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
