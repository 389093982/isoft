package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.NewChannelActivity;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

// RecyclerView 数据刷新的几种方式
// 刷新全部可见的item，notifyDataSetChanged()
// 刷新指定item，notifyItemChanged(int)
// 从指定位置开始刷新指定个item，notifyItemRangeChanged(int,int)
// 插入、移动一个并自动刷新，notifyItemInserted(int)、notifyItemMoved(int)、notifyItemRemoved(int)
// 局部刷新，notifyItemChanged(int, Object)
public class ChannelOtherSection extends Section {

    private Context mContext;
    private List<NewChannelActivity.ChannelBean> mineChannelBeans;
    private List<NewChannelActivity.ChannelBean> otherChannelBeans;
    private SectionedRecyclerViewAdapter adapter;

    public ChannelOtherSection(Context mContext, List<NewChannelActivity.ChannelBean> mineChannelBeans,
                               List<NewChannelActivity.ChannelBean> otherChannelBeans,
                               SectionedRecyclerViewAdapter adapter) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.layout_channel_other_hedaer)
                .itemResourceId(R.layout.layout_channel_other_content)
                .build());
        this.mContext = mContext;
        this.mineChannelBeans = mineChannelBeans;
        this.otherChannelBeans = otherChannelBeans;
        this.adapter = adapter;
    }

    @Override
    public int getContentItemsTotal() {
        return otherChannelBeans.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewChannelActivity.ChannelBean channelBean = otherChannelBeans.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.channelNameText.setText(channelBean.getChannelName());
        itemViewHolder.channelNameText.setOnClickListener(v -> moveOtherToMine(itemViewHolder));
    }

    /**
     * 移动其他频道数据到我的频道
     *
     * @param itemViewHolder
     */
    private void moveOtherToMine(ItemViewHolder itemViewHolder) {
        // 1、内存数据移动
        // getAdapterPosition 返回数据在 Adapter 中的位置(也许位置的变化还未来得及刷新到布局中),
        // 当使用 Adapter 的时候(例如调用 Adapter 的 notify 的刷新相关方法时)考虑使用
        int position = itemViewHolder.getAdapterPosition();
        // -1 -1 是去除两个 header 位置
        int startPosition = position - mineChannelBeans.size() - 1 -1;

        NewChannelActivity.ChannelBean item = otherChannelBeans.get(startPosition);
        otherChannelBeans.remove(startPosition);
        mineChannelBeans.add(item);
        // 2、界面移动
        // 通知位置移动 notifyItemMoved(int fromPosition, int toPosition)
        adapter.notifyItemMoved(position, mineChannelBeans.size() - 1 + 1);     // -1 + 1 索引 -1 加个 header
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
