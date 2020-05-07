package com.linkknown.ilearning.section;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.ShowTypeDetailService;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class ShowTypeDetailClassifySection extends Section {

    private List<ShowTypeDetailService.HotClassify> itemList;

    public ShowTypeDetailClassifySection(List<ShowTypeDetailService.HotClassify> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_types_icon)
                .build());
        this.itemList = itemList;
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
        itemHolder.item_title.setText(itemList.get(position).getClassifyName());
        itemHolder.item_icon.setImageResource(itemList.get(position).getClassifyImage());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView item_icon;
        private final TextView item_title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_icon = (ImageView) itemView.findViewById(R.id.item_icon);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
