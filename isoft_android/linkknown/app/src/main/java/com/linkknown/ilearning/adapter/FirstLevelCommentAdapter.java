package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.FirstLevelCommentResponse;
import com.linkknown.ilearning.popup.SecondLevelCommentPopView;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FirstLevelCommentAdapter extends BaseQuickAdapter<FirstLevelCommentResponse.Comment, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public FirstLevelCommentAdapter(Context mContext, List<FirstLevelCommentResponse.Comment> comments) {
        super(R.layout.item_course_comment_first_level, comments);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, FirstLevelCommentResponse.Comment first_level_comment) {
        //头像
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), first_level_comment.getSmall_icon());
        //昵称
        viewHolder.setText(R.id.nickNameText, first_level_comment.getNick_name());
        // 设置评论内容
        viewHolder.setText(R.id.commentContentText, first_level_comment.getContent());
        //评论时间
        viewHolder.setText(R.id.comment_time, DateUtil.formatDate_StandardForm(first_level_comment.getCreated_time()));
        //回复    eg: 188回复
        viewHolder.setText(R.id.comment_reply, first_level_comment.getSub_amount()==0?"回复":first_level_comment.getSub_amount()+"回复");
        //设置回复点击事件
        viewHolder.findView(R.id.comment_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .hasShadowBg(true)
                        .asCustom(new SecondLevelCommentPopView(mContext,first_level_comment)).show();
            }
        });


        viewHolder.setVisible(R.id.deleteIcon,LoginUtil.isLoginUserName(mContext, first_level_comment.getUser_name()));
    }
}
