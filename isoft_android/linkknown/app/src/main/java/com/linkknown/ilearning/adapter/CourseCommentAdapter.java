package com.linkknown.ilearning.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseCommentAdapter extends BaseQuickAdapter<CommentResponse.Comment, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CourseCommentAdapter(Context mContext, List<CommentResponse.Comment> comments) {
        super(R.layout.item_course_comment, comments);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, CommentResponse.Comment comment) {
        // 设置评论内容
        viewHolder.setText(R.id.commentContentText, comment.getContent());
        viewHolder.setText(R.id.nickNameText, comment.getNick_name());
        UIUtils.setImage(mContext, viewHolder.findView(R.id.headerIcon), comment.getSmall_icon());
        viewHolder.setVisible(R.id.deleteIcon,LoginUtil.isLoginUserName(mContext, comment.getUser_name()));
    }
}
