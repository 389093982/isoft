package com.linkknown.ilearning.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.ShowAndCloseMoreSection;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HotSearchPopView extends BottomPopupView {

    private SectionedRecyclerViewAdapter tagRecyclerViewAdapter;
    List<String> showTagList = new ArrayList<>();
    List<String> hideTagList = new ArrayList<>();

    private CallbackListener listener;

    private Context mContext;
    public HotSearchPopView(@NonNull Context context, CallbackListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_hot_search;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        RecyclerView showTagView = findViewById(R.id.showTagView);

        tagRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        showTagList = new ArrayList<>(Arrays.asList("java1", "java2","java3", "java4","java5", "java6","java7","java8"));
        CommonTagSection showCommonTagSection = new CommonTagSection(mContext, showTagList);
        CommonTagSection hideCommonTagSection = new CommonTagSection(mContext, hideTagList);
        ShowAndCloseMoreSection showAndCloseMoreSection = new ShowAndCloseMoreSection(mContext, new ShowAndCloseMoreSection.ClickListener() {
            @Override
            public void showMore() {
                hideTagList.clear();
                hideTagList.addAll(new ArrayList<>(Arrays.asList("go1", "go2","go3", "go4","go5", "go6","go7", "go8")));
                tagRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void closeMore() {
                hideTagList.clear();
                tagRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        tagRecyclerViewAdapter.addSection(showCommonTagSection);
        tagRecyclerViewAdapter.addSection(hideCommonTagSection);
        tagRecyclerViewAdapter.addSection(showAndCloseMoreSection);
        showTagView.setLayoutManager(new LinearLayoutManager(mContext));
        showTagView.setAdapter(tagRecyclerViewAdapter);
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.65f);
    }

    public interface CallbackListener {
        void onConfirm (String text);
    }
}
