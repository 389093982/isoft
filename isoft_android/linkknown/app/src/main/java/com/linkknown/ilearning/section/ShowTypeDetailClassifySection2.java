package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.ShowTypeDetailService;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class ShowTypeDetailClassifySection2 extends Section {

    private List<ShowTypeDetailService.HotClassify2> itemList;
    private Context mContext;

    public ShowTypeDetailClassifySection2(Context mContext, List<ShowTypeDetailService.HotClassify2> itemList) {
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
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.item_img.setImageResource(itemList.get(position).getClassifyImage());
        itemHolder.item_title.setText(itemList.get(position).getClassifyName());
        itemHolder.item_play.setText("888");
        itemHolder.item_review.setText("888");
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView item_img;
        private final TextView item_title;
        private final TextView item_review;
        private final TextView item_play;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_play = (TextView) itemView.findViewById(R.id.item_play);
            item_review = (TextView) itemView.findViewById(R.id.item_review);
        }
    }
}
