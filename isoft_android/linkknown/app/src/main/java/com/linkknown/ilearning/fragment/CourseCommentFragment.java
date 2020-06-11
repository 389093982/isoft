package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseCommentAdapter;
import com.linkknown.ilearning.common.CommonDiffCallback;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.model.Paginator;
import com.linkknown.ilearning.popup.BottomQuickEidtDialog;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseCommentFragment extends BaseLazyLoadFragment implements View.OnClickListener {

    private Context mContext;
    private Handler handler = new Handler();
    private BottomQuickEidtDialog editCommentDialog;

    private RecyclerView commentRecyclerView;

    private int course_id;
    private String course_author;
    // 当前评论页面评论的分页信息
    private Paginator paginator;
    // 当前评论页面显示的评论数据
    private List<CommentResponse.Comment> displayComments = new ArrayList<>();

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    @BindView(R.id.addComment)
    public TextView addComment;

    // 评论列表适配器
    BaseQuickAdapter baseQuickAdapter;

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_comment;
    }

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        commentRecyclerView = mRootView.findViewById(R.id.comment_recycleview).findViewById(R.id.recyclerView);

        initBundleParam();
        // 初始化组件
        initCommentView();
    }

    private void initBundleParam() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            course_id = bundle.getInt("course_id");
            course_author = bundle.getString("course_author");
        }
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
        executeLoadPageData(current_page, pageSize);

    }

    private void executeLoadPageData(int current_page, int pageSize) {
        // 状态手动置为"加载中"，并且会调用加载更多监听
        // 一般情况下，不需要自己设置'加载中'状态
        baseQuickAdapter.getLoadMoreModule().loadMoreToLoading();

        LinkKnownApiFactory.getLinkKnownApi().filterCommentFirstLevel(course_id, "course_theme_type", "comment", current_page, pageSize)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CommentResponse>() {
                    @Override
                    public void onNext(CommentResponse commentResponse) {
                        if (commentResponse.isSuccess()) {
                            if (CollectionUtils.isNotEmpty(commentResponse.getComments())) {
                                if (current_page == 1) {
                                    // 先清空再添加
                                    displayComments.clear();
                                }
                                displayComments.addAll(commentResponse.getComments());
                                baseQuickAdapter.setList(displayComments);

                                // 当前这次数据加载完毕，调用此方法
                                baseQuickAdapter.getLoadMoreModule().loadMoreComplete();
                            } else {
                                if (current_page == 1) {
                                    displayComments.clear();
                                    baseQuickAdapter.setList(displayComments);
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

        baseQuickAdapter = new CourseCommentAdapter(mContext, displayComments);
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
        baseQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.deleteIcon) {
                CommentResponse.Comment comment = displayComments.get(position);
                deleteComment(comment, position);
            }
        });

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        commentRecyclerView.setAdapter(baseQuickAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addComment:
                showEditCommentDialog();
                break;
            default:
                break;
        }
    }

    private void showEditCommentDialog () {
        editCommentDialog = new BottomQuickEidtDialog(mContext, text -> {
            handleSubmitComment(text);
        });
    }

    private void handleSubmitComment (String content) {
        int theme_pk = course_id;
        String theme_type = "course_theme_type";
        String comment_type = "comment";
        int parent_id = 0;                          // 一级评论
        int org_parent_id = 0;
        String refer_user_name = course_author;     // 被评论人
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
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                    }
                });

    }

    public void deleteComment(CommentResponse.Comment comment, int position) {
        int level = comment.getParent_id() > 0 ? 2 : 1;     // 有父评论就是二级评论，否则就是一级评论
        int id = comment.getId();                           // 评论 id
        int org_parent_id = comment.getOrg_parent_id();     // 父评论 id
        int theme_pk = course_id;                           // 课程 id
        String theme_type = "course_theme_type";
        LinkKnownApiFactory.getLinkKnownApi().deleteComment(level, id, theme_pk, theme_type, org_parent_id)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            // 局部刷新
                            List<CommentResponse.Comment> oldList = new ArrayList(displayComments);
                            displayComments.remove(position);
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommonDiffCallback(oldList, displayComments), true);
                            diffResult.dispatchUpdatesTo(baseQuickAdapter);
                            ToastUtil.showText(mContext, "删除成功！");
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


