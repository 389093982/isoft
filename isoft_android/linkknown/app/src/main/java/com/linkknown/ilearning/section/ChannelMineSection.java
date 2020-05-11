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
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ChannelMineSection extends Section {

    private Context mContext;
    private List<NewChannelActivity.ChannelBean> mineChannelBeans;
    List<NewChannelActivity.ChannelBean> otherChannelBeans;
    private SectionedRecyclerViewAdapter adapter;

    // 是否为 编辑 模式
    private boolean isEditMode;

    public ChannelMineSection(Context mContext, List<NewChannelActivity.ChannelBean> mineChannelBeans,
                              List<NewChannelActivity.ChannelBean> otherChannelBeans,
                              SectionedRecyclerViewAdapter adapter) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.layout_channel_mine_hedaer)
                .itemResourceId(R.layout.layout_channel_mine_content)
                .build());
        this.mContext = mContext;
        this.mineChannelBeans = mineChannelBeans;
        this.otherChannelBeans = otherChannelBeans;
        this.adapter = adapter;
    }

    @Override
    public int getContentItemsTotal() {
        return mineChannelBeans.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewChannelActivity.ChannelBean channelBean = mineChannelBeans.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.channelNameText.setText(channelBean.getChannelName());
        itemViewHolder.channelNameText.setOnClickListener(v -> moveMineToOther(itemViewHolder));
    }

    /**
     * 移动我的频道数据到其他频道
     *
     * @param itemViewHolder
     */
    private void moveMineToOther(ItemViewHolder itemViewHolder) {
        // 1、内存数据移动
        // getAdapterPosition 返回数据在 Adapter 中的位置(也许位置的变化还未来得及刷新到布局中),
        // 当使用 Adapter 的时候(例如调用 Adapter 的 notify 的刷新相关方法时)考虑使用
        int position = itemViewHolder.getAdapterPosition();
        // -1 是去掉 header 位置
        int startPosition = position - 1;
        NewChannelActivity.ChannelBean item = mineChannelBeans.get(startPosition);
        mineChannelBeans.remove(startPosition);
        otherChannelBeans.add(0, item);
        // 2、界面移动
        // 通知位置移动 notifyItemMoved(int fromPosition, int toPosition)
        adapter.notifyItemMoved(position, mineChannelBeans.size() -1 + 1 + 2);      // 索引 -1 + 被移走的一个 加 2个 header
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.mineChannelBtnEdit.setOnClickListener(v -> {
            if (!isEditMode) {
                headerHolder.mineChannelBtnEdit.setText("完成");
            } else {
                headerHolder.mineChannelBtnEdit.setText("编辑");
            }
        });
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView mineChannelBtnEdit;

        HeaderViewHolder(@NonNull View view) {
            super(view);

            mineChannelBtnEdit = view.findViewById(R.id.mineChannelBtnEdit);
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
