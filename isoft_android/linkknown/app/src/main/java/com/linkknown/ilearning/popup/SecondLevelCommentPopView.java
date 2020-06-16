package com.linkknown.ilearning.popup;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.SecondLevelCommentAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.FirstLevelCommentResponse;
import com.linkknown.ilearning.model.SecondLevelCommentResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SecondLevelCommentPopView extends BottomPopupView {

    private Context mContext;
    private FirstLevelCommentResponse.Comment first_level_comment;
    private BaseQuickAdapter baseQuickAdapter;
    private List<SecondLevelCommentResponse.Comment> secondLevelComments;
    private RecyclerView commentRecyclerView;

    public SecondLevelCommentPopView(@NonNull Context context, FirstLevelCommentResponse.Comment first_level_comment) {
        super(context);
        this.mContext = context;
        this.first_level_comment = first_level_comment;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.fragment_course_comment_second_level;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        //1.设置一级评论
        //头像
        UIUtils.setImage(mContext, findViewById(R.id.headerIcon), first_level_comment.getSmall_icon());
        //昵称
        ((TextView)findViewById(R.id.nickNameText)).setText(first_level_comment.getNick_name());
        // 设置评论内容
        ((TextView)findViewById(R.id.commentContentText)).setText(first_level_comment.getContent());
        //评论时间
        ((TextView)findViewById(R.id.comment_time)).setText(DateUtil.formatDate_StandardForm(first_level_comment.getCreated_time()));
        //全部回复
        ((TextView)findViewById(R.id.allReply)).setText(first_level_comment.getSub_amount()==0?"暂无回复":"全部回复("+first_level_comment.getSub_amount()+")");

        //2.查询一级评论对应的二级评论
        Integer theme_pk = first_level_comment.getTheme_pk();
        String theme_type = first_level_comment.getTheme_type();
        Integer org_parent_id = first_level_comment.getId();
        LinkKnownApiFactory.getLinkKnownApi().filterCommentSecondLevel(theme_pk, theme_type, org_parent_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<SecondLevelCommentResponse>() {
                    @Override
                    public void onNext(SecondLevelCommentResponse commentResponse) {
                        if (commentResponse.isSuccess()){
                            secondLevelComments = commentResponse.getComments();
                            //2.设置二级评论的展示
                            baseQuickAdapter = new SecondLevelCommentAdapter(mContext,secondLevelComments);
                            commentRecyclerView = findViewById(R.id.second_level_comment_recycleview).findViewById(R.id.recyclerView);
                            commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            commentRecyclerView.setAdapter(baseQuickAdapter);
                        }else{
                            ToastUtil.showText(mContext,"查询二级评论失败..");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.75f);
    }

}
