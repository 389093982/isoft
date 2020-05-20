package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class ShowAndCloseMoreSection extends Section {

    private Context mContext;
    private ClickListener listener;

    public ShowAndCloseMoreSection(Context mContext, ClickListener listener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.show_close_more)
                .build());
        this.mContext = mContext;
        this.listener = listener;
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
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        // 点击后互换显示
        viewHolder.showMore.setOnClickListener(v -> {
            viewHolder.showMore.setVisibility(View.GONE);
            viewHolder.closeMore.setVisibility(View.VISIBLE);
            // 向上通知
            listener.showMore();
        });
        viewHolder.closeMore.setOnClickListener(v -> {
            viewHolder.showMore.setVisibility(View.VISIBLE);
            viewHolder.closeMore.setVisibility(View.GONE);
            listener.closeMore();
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView showMore;
        private TextView closeMore;

        public ItemViewHolder(View itemView) {
            super(itemView);
            showMore = itemView.findViewById(R.id.showMore);
            closeMore = itemView.findViewById(R.id.closeMore);
        }
    }

    public static interface ClickListener {
        void showMore();
        void closeMore();
    }
}
