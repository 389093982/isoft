package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.FirstLevelCommentAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.model.FirstLevelCommentResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.popup.BottomQuickEidtDialog;
import com.linkknown.ilearning.popup.SecondLevelCommentPopView;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.lxj.xpopup.XPopup;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FirstLevelCommentFragment extends BaseLazyLoadFragment implements View.OnClickListener {

    //构造器需要的参数
    private int theme_pk;
    private String theme_type;
    private String comment_type;
    private String refer_user_name;
    private String comments;

    //fragment参数
    private Context mContext;
    private Handler handler = new Handler();
    private BottomQuickEidtDialog editCommentDialog;
    private RecyclerView commentRecyclerView;

    // 当前评论页面评论的分页信息
    private Paginator paginator;

    // 当前评论页面显示的评论数据
    private List<FirstLevelCommentResponse.Comment> firstLevelComments = new ArrayList<>();

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    @BindView(R.id.addComment)
    public TextView addComment;

    @BindView(R.id.allComments)
    public TextView allComments;

    // 评论列表适配器
    FirstLevelCommentAdapter baseQuickAdapter;

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_first_level_comment;
    }

    //构造器
    public FirstLevelCommentFragment(int theme_pk,String theme_type,String comment_type,String refer_user_name,String comments){
        this.theme_pk = theme_pk;
        this.theme_type = theme_type;
        this.comment_type = comment_type;
        this.refer_user_name = refer_user_name;
        this.comments = comments;
    };


    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());
        commentRecyclerView = mRootView.findViewById(R.id.first_level_comment_recycleview).findViewById(R.id.recyclerView);
        allComments.setText(Integer.valueOf(comments)==0?"全部评论":"全部评论("+comments+")");
        // 初始化组件
        initCommentView();
    }


    //左上角添加一级评论按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addComment:
                int theme_pk = this.theme_pk;
                String theme_type = this.theme_type;
                String comment_type = this.comment_type;
                int parent_id = 0;                          // 一级评论
                int org_parent_id = 0;
                String refer_user_name = this.refer_user_name;     // 被评论人
                showEditCommentDialog(theme_pk,theme_type,comment_type,org_parent_id,parent_id,refer_user_name);
                break;
            default:
                break;
        }
    }

    private void showEditCommentDialog (int theme_pk, String theme_type, String comment_type, int org_parent_id, int parent_id, String refer_user_name) {
        editCommentDialog = new BottomQuickEidtDialog(mContext, text -> {
            handleSubmitComment(theme_pk,theme_type,comment_type,text,org_parent_id,parent_id,refer_user_name);
        });
    }

    private void handleSubmitComment (int theme_pk, String theme_type, String comment_type, String content, int org_parent_id, int parent_id, String refer_user_name) {
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
                            initData();
                        } else {
                            Log.e("onNext =>", "添加评论失败~");
                            ToastUtil.showText(mContext, "添加评论失败~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                    }
                });

    }


    @Override
    protected void initData() {
        // 加载数据
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE);
    }

    private void loadPageData (int current_page, int pageSize) {
        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }

    }

    private void executeLoadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        baseQuickAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().filterCommentFirstLevel(this.theme_pk, this.theme_type, this.comment_type, current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<FirstLevelCommentResponse>() {
                    @Override
                    public void onNext(FirstLevelCommentResponse commentResponse) {
                        if (commentResponse.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(commentResponse.getComments())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    firstLevelComments.clear();
                                }
                                firstLevelComments.addAll(commentResponse.getComments());
                                baseQuickAdapter.setList(firstLevelComments);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            } else {
                                if (current_page == 1) {
                                    firstLevelComments.clear();
                                    baseQuickAdapter.setList(firstLevelComments);
                                    baseQuickAdapter.setEmptyView(R.layout.layout_region_recommend_empty);
                                    TextView emptyTipText = baseQuickAdapter.getEmptyLayout().findViewById(R.id.emptyTipText);
                                    emptyTipText.setText("暂无评论");
                                }
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }

                            paginator = commentResponse.getPaginator();

                            // 最后一页
                            if (CommonUtil.isLastPage(paginator)) {
                                baseQuickAdapter.getLoadMoreModule().loadMoreEnd();
                            }
                        } else {
                            baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayoutHelper.finishRefreshing();
                        // 当前这次数据加载错误，调用此方法
                        baseQuickAdapter.getLoadMoreModule().loadMoreFail();
                    }
                });
    }

    private void initCommentView () {
        addComment.setOnClickListener(this);

        baseQuickAdapter = new FirstLevelCommentAdapter(mContext, firstLevelComments);
        // 是否自动加载下一页（默认为true）
        baseQuickAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 设置加载更多监听事件
        baseQuickAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                loadPageData(paginator.getCurrpage() + 1, Constants.DEFAULT_PAGE_SIZE);
            }
        });
        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        baseQuickAdapter.addChildClickViewIds(R.id.deleteIcon);

        baseQuickAdapter.setReplyCommentListener(first_level_comment -> {
            //一级评论的回复--弹框
            showPop(first_level_comment);
        });

        baseQuickAdapter.setDeleteListener(first_level_comment -> {
            //删除评论
            deleteComment(first_level_comment);
        });

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        commentRecyclerView.setAdapter(baseQuickAdapter);
    }


    //弹框显示
    public void showPop(FirstLevelCommentResponse.Comment first_level_comment){
        new XPopup.Builder(getContext())
                .hasShadowBg(true)
                .asCustom(new SecondLevelCommentPopView(mContext, first_level_comment)).show();
    };


    //删除评论
    public void deleteComment(FirstLevelCommentResponse.Comment comment) {
        int level = comment.getParent_id() > 0 ? 2 : 1;     // 有父评论就是二级评论，否则就是一级评论
        int id = comment.getId();                           // 评论 id
        int org_parent_id = comment.getOrg_parent_id();     // 父评论 id
        int theme_pk = this.theme_pk;                           // 课程 id
        String theme_type = this.theme_type;
        LinkKnownApiFactory.getLinkKnownApi().deleteComment(level, id, theme_pk, theme_type, org_parent_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            // 局部刷新
                            ToastUtil.showText(mContext, "删除成功！");
                            initData();
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


