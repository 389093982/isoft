package com.linkknown.ilearning.popup;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.SecondLevelCommentAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.model.FirstLevelCommentResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.model.SecondLevelCommentResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SecondLevelCommentPopView extends BottomPopupView {

    private BottomQuickEidtDialog editCommentDialog;
    private Context mContext;
    private Handler handler = new Handler();
    private Paginator paginator;

    @BindView(R.id.recyclerView)
    public RecyclerView second_level_comment_recycleview;

    private SecondLevelCommentAdapter baseQuickAdapter;
    private FirstLevelCommentResponse.Comment first_level_comment;
    private List<SecondLevelCommentResponse.Comment> secondLevelComments = new ArrayList<>();


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
        ButterKnife.bind(this);

        //初始化视图
        initView();

        //2.查询一级评论对应的二级评论
        initData();
    }


    public void initView(){
        //创建adapter
        baseQuickAdapter = new SecondLevelCommentAdapter(mContext,secondLevelComments);
        //一级评论弹框里回复的回复
        baseQuickAdapter.setReplyListener(second_level_comment -> { replyComment(second_level_comment); });
        //删除评论
        baseQuickAdapter.setDeleteListener(second_level_comment -> { deleteComment(second_level_comment); });

        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        second_level_comment_recycleview.setLayoutManager(new LinearLayoutManager(mContext));
        second_level_comment_recycleview.setAdapter(baseQuickAdapter);

        //1.设置一级评论
        //头像
        UIUtils.setImage(mContext, findViewById(R.id.headerIcon), first_level_comment.getSmall_icon());
        //昵称
        ((TextView)findViewById(R.id.nickNameText)).setText(first_level_comment.getNick_name());
        // 设置评论内容
        ((TextView)findViewById(R.id.commentContentText)).setText(first_level_comment.getContent());
        //评论时间
        ((TextView)findViewById(R.id.comment_time)).setText(DateUtil.formatDate_StandardForm(first_level_comment.getCreated_time()));
        //添加回复
        ((TextView)findViewById(R.id.addReply)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int theme_pk = first_level_comment.getTheme_pk();
                String theme_type = "course_theme_type";
                String comment_type = "comment";
                Integer org_parent_id = first_level_comment.getId();
                Integer parent_id = first_level_comment.getId();                          // 一级评论
                String refer_user_name = first_level_comment.getCreated_by();     // 被评论人
                showEditCommentDialog(theme_pk,theme_type,comment_type,org_parent_id,parent_id,refer_user_name);
            }
        });
        //全部回复
        ((TextView)findViewById(R.id.allReply)).setText(first_level_comment.getSub_amount()==0?"暂无回复":"全部回复("+first_level_comment.getSub_amount()+")");
    };


    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.75f);
    }


    public void initData(){
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    };


    private void loadPageData(int current_page, int pageSize) {
        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }
    }


    public void executeLoadPageData(int current_page,int offset){
        //2.查询一级评论对应的二级评论
        Integer theme_pk = first_level_comment.getTheme_pk();
        String theme_type = first_level_comment.getTheme_type();
        Integer org_parent_id = first_level_comment.getId();
        LinkKnownApiFactory.getLinkKnownApi().filterCommentSecondLevel(theme_pk, theme_type, org_parent_id,current_page,offset)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<SecondLevelCommentResponse>() {
                    @Override
                    public void onNext(SecondLevelCommentResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getComments())){
                                if (current_page == 1) {
                                    // 先清空再添加
                                    secondLevelComments.clear();
                                }
                                secondLevelComments.addAll(o.getComments());
                                baseQuickAdapter.setList(secondLevelComments);
                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();

                            }else{
                                if (current_page == 1) {
                                    secondLevelComments.clear();
                                    baseQuickAdapter.setList(secondLevelComments);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无回复");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                            paginator = o.getPaginator();
                            // 最后一页
                            if (CommonUtil.isLastPage(paginator)) {
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                            baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showText(mContext,"查询失败！");
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    };


    //弹框
    private void showEditCommentDialog (int theme_pk, String theme_type, String comment_type, int org_parent_id, int parent_id, String refer_user_name) {
        editCommentDialog = new BottomQuickEidtDialog(mContext, text -> {
            handleSubmitComment(theme_pk,theme_type,comment_type,text,org_parent_id,parent_id,refer_user_name);
        });
    }

    //提交回复
    private void handleSubmitComment(int theme_pk, String theme_type, String comment_type, String content, int org_parent_id, int parent_id, String refer_user_name) {
        LinkKnownApiFactory.getLinkKnownApi().addComment(theme_pk, theme_type, comment_type, content, org_parent_id, parent_id, refer_user_name)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<EditCommentResponse>() {

                    @Override
                    public void onNext(EditCommentResponse editCommentResponse) {
                        if (editCommentResponse.isSuccess()) {
                            // 对话框隐藏
                            editCommentDialog.dismiss();
                            // 重新加载数据
                            onCreate();
                        } else {
                            Log.e("onNext =>", "添加回复失败~");
                            ToastUtil.showText(mContext, "添加回复失败~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                    }
                });

    }


    //一级评论弹框里回复的回复
    public void replyComment(SecondLevelCommentResponse.Comment second_level_comment){
        int theme_pk = second_level_comment.getTheme_pk();
        String theme_type = "course_theme_type";
        String comment_type = "comment";
        int org_parent_id = second_level_comment.getOrg_parent_id();
        int parent_id = second_level_comment.getId();                          // 一级评论
        String refer_user_name = second_level_comment.getCreated_by();     // 被评论人
        showEditCommentDialog(theme_pk,theme_type,comment_type,org_parent_id,parent_id,refer_user_name);
    };



    //删除评论
    public void deleteComment(SecondLevelCommentResponse.Comment second_level_comment) {
        int level = second_level_comment.getParent_id() > 0 ? 2 : 1;     // 有父评论就是二级评论，否则就是一级评论
        int id = second_level_comment.getId();                           // 评论 id
        int org_parent_id = second_level_comment.getOrg_parent_id();     // 父评论 id
        int theme_pk = second_level_comment.getTheme_pk();                           // 课程 id
        String theme_type = "course_theme_type";
        LinkKnownApiFactory.getLinkKnownApi().deleteComment(level, id, theme_pk, theme_type, org_parent_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtil.showText(mContext, "删除成功！");
                            // 重新加载数据
                            onCreate();
                        } else {
                            ToastUtil.showText(mContext, "删除失败！");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, "删除失败！");
                    }
                });
    }


}
