package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.MessageListResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;

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
        // 设置日期
        TextView dateTimeView = viewHolder.findView(R.id.dateTimeView);
        dateTimeView.setText(DateUtil.formateDate(message.getLast_updated_time(), DateUtil.PATTERN3));
        // 设置消息内容
        TextView messageTextView = viewHolder.findView(R.id.messageTextView);
        messageTextView.setText(String.format("@%s: %s", LoginUtil.getLoginNickName(mContext), message.getMessage_text()));


    }
}
