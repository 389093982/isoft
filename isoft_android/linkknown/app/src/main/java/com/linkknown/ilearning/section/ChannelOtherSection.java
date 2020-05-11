package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class ChannelOtherSection extends Section {

    private Context mContext;

    public ChannelOtherSection(Context mContext) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.layout_channel_other_hedaer)
                .itemResourceId(R.layout.layout_channel_other_content)
                .build());
        this.mContext = mContext;
    }

    @Override
    public int getContentItemsTotal() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new ChannelMineSection.HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final ChannelMineSection.HeaderViewHolder headerHolder = (ChannelMineSection.HeaderViewHolder) holder;

//        headerHolder.tvTitle.setText(title);
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

//        final TextView tvTitle;

        HeaderViewHolder(@NonNull View view) {
            super(view);

//            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }
}
