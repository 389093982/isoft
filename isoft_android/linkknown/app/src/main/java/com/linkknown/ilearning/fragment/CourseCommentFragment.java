package com.linkknown.ilearning.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.diff.CommonDiffCallback;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.listener.OnLoadMoreListener;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.EditCommentResponse;
import com.linkknown.ilearning.section.CourseCommentSection;
import com.linkknown.ilearning.service.CommentService;
import com.linkknown.ilearning.util.DisplayUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Query;

public class CourseCommentFragment extends BaseLazyLoadFragment implements View.OnClickListener,CourseCommentSection.ClickListener {

    private Context mContext;

    private RecyclerView commentRecyclerView;

    private int course_id;
    private String course_author;
    // 当前评论页面评论的分页信息
    private CommentResponse.Paginator paginator;
    // 当前评论页面显示的评论数据
    private List<CommentResponse.Comment> displayComments = new ArrayList<>();

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.addComment)
    public TextView addComment;
    @BindView(R.id.moreOperate)
    public TextView moreOperate;

    // 评论列表适配器
    SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    CourseCommentSection courseCommentSection;

    // 评论列表订阅数据, key 为分页信息, value 为当前页的订阅信息
    private Map<Integer, Observer> observerMap = new HashMap<>();

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

        commentRecyclerView = mRootView.findViewById(R.id.comment_recycleview).findViewById(R.id.recyclerView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            course_id = bundle.getInt("course_id");
            course_author = bundle.getString("course_author");
        }
        // 初始化组件
        init();
    }

    @Override
    protected void initData() {
        // 绑定数据
        bindPageData(1);
        // 加载数据
        loadData();
    }

    // 建议使用 DiffUtil 进行局部刷新
//    // 加载第一页要先清空
//    displayComments.clear();
//    // list 清空同时也要刷新 adapter
//    sectionedRecyclerViewAdapter.notifyDataSetChanged();
    private void loadData () {
        // 加载第一页要先清空
        List<CommentResponse.Comment> oldList = new ArrayList(displayComments);
        displayComments.clear();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommonDiffCallback(oldList, displayComments), true);
        diffResult.dispatchUpdatesTo(sectionedRecyclerViewAdapter);

        loadNextPageData(1);
    }

    private void loadNextPageData(int current_page) {
        bindPageData(current_page);
        CommentService.filterCommentFirstLevel(course_id, "course_theme_type", "comment", current_page, 10);
    }

    // 绑定分页数据
    private void bindPageData (int current_page) {
        // 防止同一页重复订阅
        if (!observerMap.containsKey(current_page)) {
            Observer<CommentResponse> observer = commentResponse -> {
                SectionAdapter sectionAdapter = sectionedRecyclerViewAdapter.getAdapterForSection(courseCommentSection);
                if (commentResponse.isSuccess()) {
                    // 合并数据，当数据量过大时,需要先进行 clear 一部分
                    displayComments.addAll(commentResponse.getComments());
                    paginator = commentResponse.getPaginator();
                    // 设置内容状态
                    courseCommentSection.setState(Section.State.LOADED);
                    // footer 设置加载完成，到最后一页了显示加载到底
                    sectionAdapter.notifyFooterChanged(
                            // 是最后一页了
                            paginator.getCurrpage() >= paginator.getTotalpages()
                                    ? CourseCommentSection.PAYLOAD_FOOTER_LOADED_NO_MORE
                                    : CourseCommentSection.PAYLOAD_FOOTER_LOADED);
                } else {
                    // 设置内容状态
                    courseCommentSection.setState(Section.State.FAILED);
                    sectionAdapter.notifyFooterChanged(CourseCommentSection.PAYLOAD_FOOTER_LOADED);
                }
                sectionAdapter.notifyAllItemsChanged();
                // 有数据回来则取消刷新
                refreshLayout.setRefreshing(false);
            };

            observerMap.put(current_page, observer);
            LiveEventBus.get(CommentService.getKey(course_id,"course_theme_type", "comment", current_page), CommentResponse.class).observe(this, observer);
        }
    }

    private void init () {
        addComment.setOnClickListener(this);
        moreOperate.setOnClickListener(this);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        courseCommentSection = new CourseCommentSection(mContext, displayComments, this);
        sectionedRecyclerViewAdapter.addSection(courseCommentSection);

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        commentRecyclerView.setAdapter(sectionedRecyclerViewAdapter);

        refreshLayout.setOnRefreshListener(() -> {
            // 属性中
            refreshLayout.setRefreshing(true);
            // 重新加载数据
            loadData();
        });

        // setOnScrollListener 由于可能出现空指针的风险,已经过时.建议用addOnScrollListener
        commentRecyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 还有下一页数据则加载下一页数据
                if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages()) {
                    SectionAdapter sectionAdapter = sectionedRecyclerViewAdapter.getAdapterForSection(courseCommentSection);
                    // footer 显示加载中
                    sectionAdapter.notifyFooterChanged(CourseCommentSection.PAYLOAD_FOOTER_LOADING);
                    loadNextPageData(paginator.getCurrpage() + 1);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addComment:
                showEditCommentDialog();
                break;
            case R.id.moreOperate:
                showMoreDialog();
                break;
            default:
                break;
        }
    }

    private void showEditCommentDialog () {
        Dialog bottomDialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_content_comment, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DisplayUtil.dp2px(mContext, 16f);
        params.bottomMargin = DisplayUtil.dp2px(mContext, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        bindEditCommentEvent(bottomDialog);
    }

    private void bindEditCommentEvent (Dialog dialog) {
        dialog.getWindow().findViewById(R.id.commentSubmitBtn).setOnClickListener(v -> {
            TextView textView = dialog.getWindow().findViewById(R.id.commentText);
            String content = textView.getText().toString();
            int theme_pk = course_id;
            String theme_type = "course_theme_type";
            String comment_type = "comment";
            int parent_id = 0;                          // 一级评论
            int org_parent_id = 0;
            String refer_user_name = course_author;     // 被评论人
            LinkKnownApiFactory.getLinkKnownApi().addComment(theme_pk, theme_type, comment_type, content, org_parent_id, parent_id, refer_user_name)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new io.reactivex.Observer<EditCommentResponse>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(EditCommentResponse editCommentResponse) {
                            if (editCommentResponse.isSuccess()) {
                                // 对话框隐藏
                                dialog.dismiss();
                                refreshLayout.setRefreshing(true);
                                // 重新加载数据
                                loadData();
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

                        @Override
                        public void onComplete() {

                        }
                    });

        });
    }

    private void showMoreDialog() {
        Dialog bottomDialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DisplayUtil.dp2px(mContext, 16f);
        params.bottomMargin = DisplayUtil.dp2px(mContext, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    @Override
    public void onItemRootViewClicked(@NonNull CourseCommentSection section, int itemAdapterPosition) {
        CommentResponse.Comment comment = displayComments.get(itemAdapterPosition);
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
                            displayComments.remove(itemAdapterPosition);
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommonDiffCallback(oldList, displayComments), true);
                            diffResult.dispatchUpdatesTo(sectionedRecyclerViewAdapter);
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


