package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.NewChannelActivity;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class ChannelOtherSection extends Section {

    private Context mContext;
    private List<NewChannelActivity.ChannelBean> channelBeans;

    public ChannelOtherSection(Context mContext, List<NewChannelActivity.ChannelBean> channelBeans) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.layout_channel_other_hedaer)
                .itemResourceId(R.layout.layout_channel_other_content)
                .build());
        this.mContext = mContext;
        this.channelBeans = channelBeans;
    }

    @Override
    public int getContentItemsTotal() {
        return channelBeans.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewChannelActivity.ChannelBean channelBean = channelBeans.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.channelNameText.setText(channelBean.getChannelName());
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView channelNameText;

        ItemViewHolder(@NonNull View view) {
            super(view);

            channelNameText = view.findViewById(R.id.channelNameText);
        }
    }
}
