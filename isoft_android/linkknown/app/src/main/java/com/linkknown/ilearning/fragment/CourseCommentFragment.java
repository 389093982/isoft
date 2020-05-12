package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.listener.OnLoadMoreListener;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.section.CourseCommentSection;
import com.linkknown.ilearning.service.CommentService;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CourseCommentFragment extends Fragment {

    private Context mContext;

    private RecyclerView commentRecyclerView;

    private int course_id;
    // 当前评论页面评论的分页信息
    private CommentResponse.Paginator paginator;
    // 当前评论页面显示的评论数据
    private List<CommentResponse.Comment> displayComments = new ArrayList<>();

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    // 评论列表适配器
    SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_comment, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        ToastUtil.showText(mContext, "初始化啦~~~~~~~~~~~~~~");

        commentRecyclerView = rootView.findViewById(R.id.comment_recycleview).findViewById(R.id.recyclerView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            course_id = bundle.getInt("course_id");
        }

        // 初始化组件
        init();

        // 绑定数据
        bindPageData(1);
        // 加载数据
        initData();

        return rootView;
    }

    private void initData () {
        // 加载第一页要先清空
        displayComments.clear();
        loadNextPageData(1);
    }

    private void loadNextPageData(int current_page) {
        ToastUtil.showText(mContext, displayComments.size() + "aaaaaaaa");
        bindPageData(current_page);
        CommentService.filterCommentFirstLevel(course_id, "course_theme_type", "comment", current_page, 10);
    }

    private void bindPageData (int current_page) {
        LiveEventBus.get(CommentService.getKey(course_id,"course_theme_type", "comment", current_page), CommentResponse.class).observe(this, commentResponse -> {
            if (commentResponse.isSuccess()) {
                ToastUtil.showText(mContext, displayComments.size() + "bbbbbbbb" + current_page + "~~~~~~~~" + commentResponse.getComments().size());
                // 合并数据，当数据量过大时,需要先进行 clear 一部分
                displayComments.addAll(commentResponse.getComments());
                paginator = commentResponse.getPaginator();

                sectionedRecyclerViewAdapter.notifyDataSetChanged();
            }
            // 有数据回来则取消刷新
            refreshLayout.setRefreshing(false);
        });
    }

    private void init () {
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        sectionedRecyclerViewAdapter.addSection(new CourseCommentSection(mContext, displayComments));

//        multiTypeAdapter = new MultiTypeAdapter();
//        multiTypeAdapter.register(CommentResponse.Comment.class, new MultiItemView<CommentResponse.Comment>() {
//            @NonNull
//            @Override
//            public int getLayoutId() {
//                return R.layout.item_course_comment;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CommentResponse.Comment comment, int position) {
//                // 设置评论内容
//                viewHolder.setText(R.id.commentContentText, comment.getContent());
//                viewHolder.setText(R.id.nickNameText, comment.getNick_name());
//                UIUtils.setImage(mContext, viewHolder.getConvertView().findViewById(R.id.headerIcon), comment.getSmall_icon());
//            }
//        });
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        commentRecyclerView.setAdapter(sectionedRecyclerViewAdapter);

        refreshLayout.setOnRefreshListener(() -> {
            // 属性中
            refreshLayout.setRefreshing(true);
            // 重新加载数据
            initData();
        });

        // setOnScrollListener 由于可能出现空指针的风险,已经过时.建议用addOnScrollListener
        commentRecyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 还有下一页数据则加载下一页数据
                if (paginator != null && paginator.getCurrpage() < paginator.getTotalpages() - 1) {
                    loadNextPageData(paginator.getCurrpage() + 1);
                } else {
                    // 显示我是有底线的
                }
            }
        });
    }
}

