package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.MessageListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<MessageListResponse.Message, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public MessageAdapter(Context mContext, List<MessageListResponse.Message> messages) {
        super(R.layout.item_message_info, messages);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, MessageListResponse.Message message) {
        TextView messageTextView = viewHolder.findView(R.id.messageTextView);
        messageTextView.setText(message.getMessage_text());
    }
}
