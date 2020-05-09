package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.service.CommentService;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import butterknife.ButterKnife;

public class CourseCommentFragment extends Fragment {

    private Context mContext;

    private RecyclerView commentRecyclerView;

    private int course_id;

    // 评论列表适配器
    MultiTypeAdapter multiTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_comment, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        commentRecyclerView = rootView.findViewById(R.id.comment_recycleview).findViewById(R.id.recyclerView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            course_id = bundle.getInt("course_id");
        }

        // 初始化组件
        init();

        // 加载数据
        initData();
        // 绑定数据
        bindData();

        return rootView;
    }

    private void initData () {
        CommentService.filterCommentFirstLevel(course_id, "course_theme_type", "comment", 1, 10);
    }

    private void bindData () {
        LiveEventBus.get(CommentService.getKey(course_id,"course_theme_type", "comment"), CommentResponse.class).observeSticky(this, new Observer<CommentResponse>() {
            @Override
            public void onChanged(CommentResponse commentResponse) {
                multiTypeAdapter.setItems(commentResponse.getComments());
                multiTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void init () {
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(CommentResponse.Comment.class, new MultiItemView<CommentResponse.Comment>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_course_comment;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CommentResponse.Comment comment, int i) {
                viewHolder.setText(R.id.commentText, "你好卑鄙啊~~~~~");
            }
        });
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        commentRecyclerView.setAdapter(multiTypeAdapter);
    }
}
