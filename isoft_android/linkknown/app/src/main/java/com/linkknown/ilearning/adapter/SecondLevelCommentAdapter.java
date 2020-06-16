package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.FirstLevelCommentResponse;
import com.linkknown.ilearning.model.SecondLevelCommentResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecondLevelCommentAdapter extends BaseQuickAdapter<SecondLevelCommentResponse.Comment, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public SecondLevelCommentAdapter(Context mContext, List<SecondLevelCommentResponse.Comment> comments) {
        super(R.layout.item_course_comment_second_level, comments);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, SecondLevelCommentResponse.Comment second_level_comment) {
        //头像
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), second_level_comment.getCreated_user_small_icon());
        //昵称
        viewHolder.setText(R.id.nickNameText, second_level_comment.getCreated_user_nick_name());
        // 设置评论内容
        viewHolder.setText(R.id.commentContentText, second_level_comment.getContent());
        //评论时间
        viewHolder.setText(R.id.comment_time, DateUtil.formatDate_StandardForm(second_level_comment.getCreated_time()));
        //回复    eg: 188回复
        viewHolder.setText(R.id.comment_reply, second_level_comment.getSub_amount()==0?"回复":second_level_comment.getSub_amount()+"回复");
        //设置回复点击事件
        viewHolder.findView(R.id.comment_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showText(mContext,"ok");
//                new XPopup.Builder(getContext())
//                        .hasShadowBg(true)
//                        .asCustom(new SecondLevelCommentPopView(mContext,second_level_comment)).show();
            }
        });


        viewHolder.setVisible(R.id.deleteIcon,LoginUtil.isLoginUserName(mContext, second_level_comment.getCreated_user_account()));
    }
}
