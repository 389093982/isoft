package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.UserAttentionListActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.model.UserAttentionListResponse;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAttentionListAdapter extends BaseQuickAdapter<UserAttentionListResponse.QueryData, BaseViewHolder> implements LoadMoreModule {

    private CallbackListener listener;

    private Context mContext;
    private String attentionType;

    public UserAttentionListAdapter(Context mContext, List<UserAttentionListResponse.QueryData> queryDatas, String attentionType) {
        super(R.layout.item_user_attention, queryDatas);
        this.mContext = mContext;
        this.attentionType = attentionType;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, UserAttentionListResponse.QueryData queryData) {
        // 设置头像
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), queryData.getSmall_icon());
        // 设置昵称
        viewHolder.setText(R.id.userNameText, queryData.getNick_name());
        viewHolder.setText(R.id.attention_counts, "关注：" + queryData.getAttention_counts());
        viewHolder.setText(R.id.fensi_counts, "粉丝：" + queryData.getFensi_counts());
        viewHolder.setText(R.id.userSignature, StringUtilEx.getFirstNotEmptyStr(queryData.getUser_signature(), Constants.DEFAULT_USER_SIGNATURE));

        if (StringUtils.equals(attentionType, UserAttentionListActivity.ATTENTION)) {
            viewHolder.setGone(R.id.cancel_attention, false);
        } else {
            viewHolder.setGone(R.id.cancel_attention, true);
        }

        viewHolder.findView(R.id.headerIcon).setOnClickListener(v -> UIUtils.gotoActivity(mContext, UserDetailActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME, queryData.getUser_name());
            return intent;
        }));

        viewHolder.findView(R.id.cancel_attention).setOnClickListener(v -> listener.cancelAttention(queryData));
    }

    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    public interface CallbackListener {
        void cancelAttention (UserAttentionListResponse.QueryData queryData);
    }
}
