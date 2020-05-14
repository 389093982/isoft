package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CourseCommentSection extends Section {

    // 用于区分 footer 显示的状态
    // 加载中,加载完成，加载完成后没有更多数据
    public static final int PAYLOAD_FOOTER_LOADING = 0;
    public static final int PAYLOAD_FOOTER_LOADED = 1;
    public static final int PAYLOAD_FOOTER_LOADED_NO_MORE = 3;

    private List<CommentResponse.Comment> itemList;
    private Context mContext;

    public CourseCommentSection(Context mContext, List<CommentResponse.Comment> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_course_comment)
                .footerResourceId(R.layout.layout_footerview)
                .failedResourceId(R.layout.layout_failedview)
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        CommentResponse.Comment comment = itemList.get(position);
        // 设置评论内容
        viewHolder.commentContentText.setText(comment.getContent());
        viewHolder.nickNameText.setText(comment.getNick_name());
        UIUtils.setImage(mContext, viewHolder.headerIconView, comment.getSmall_icon());
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, List<Object> payloads) {
        FooterViewHolder viewHolder = (FooterViewHolder) holder;
        // 初始状态全部隐藏
        int payload = (int) payloads.get(0);
        switch (payload) {
            case PAYLOAD_FOOTER_LOADING:
                this.setHasFooter(true);
                viewHolder.loadingDataLayout.setVisibility(View.VISIBLE);
                viewHolder.noMoreDataLayout.setVisibility(View.GONE);
                break;
            case PAYLOAD_FOOTER_LOADED:
                this.setHasFooter(false);
                break;
            case PAYLOAD_FOOTER_LOADED_NO_MORE:
                this.setHasFooter(true);
                viewHolder.loadingDataLayout.setVisibility(View.GONE);
                viewHolder.noMoreDataLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        if (payload == PAYLOAD_FOOTER_LOADING) {
            this.setHasFooter(true);
            viewHolder.loadingDataLayout.setVisibility(View.VISIBLE);
        } else if (payload == PAYLOAD_FOOTER_LOADED) {
            this.setHasFooter(false);
        } else if (payload == PAYLOAD_FOOTER_LOADED_NO_MORE) {
            this.setHasFooter(true);
            viewHolder.noMoreDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public RecyclerView.ViewHolder getFailedViewHolder(View view) {
        return new FailedViewHolder(view);
    }

    @Override
    public void onBindFailedViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindFailedViewHolder(holder);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView commentContentText;
        private TextView nickNameText;
        private ImageView headerIconView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            commentContentText = itemView.findViewById(R.id.commentContentText);
            nickNameText = itemView.findViewById(R.id.nickNameText);
            headerIconView = itemView.findViewById(R.id.headerIcon);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout loadingDataLayout;
        private LinearLayout noMoreDataLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            loadingDataLayout = itemView.findViewById(R.id.loadingDataLayout);
            noMoreDataLayout = itemView.findViewById(R.id.noMoreDataLayout);
        }
    }

    class FailedViewHolder extends RecyclerView.ViewHolder {

        public FailedViewHolder(View itemView) {
            super(itemView);
        }
    }
}