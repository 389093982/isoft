package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CourseCommentSection extends Section {

    private List<CommentResponse.Comment> itemList;
    private Context mContext;

    public CourseCommentSection(Context mContext, List<CommentResponse.Comment> itemList) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_course_comment)
                .footerResourceId(R.layout.layout_footerview)
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
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindFooterViewHolder(holder);
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

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
