package com.linkknown.ilearning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseVideoAdapter;
import com.linkknown.ilearning.model.CourseDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class CourseVideoView extends LinearLayout {

    // 默认以网格形式展示
    private int itemType = CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST;

    private ImageView showListView;
    private ImageView showGrdView;
    private RecyclerView recyclerView;

    //栅格布局
    private GridLayoutManager mGridLayoutManager;
    //列表布局
    private LinearLayoutManager mLinearLayoutManager;

    private CourseVideoAdapter baseQuickAdapter;

    private CallBackListener listener;

   // 存储数据
    private List<CourseDetailResponse.MultiItemTypeCVideo> cVideos = new ArrayList<>();

    private Context mContext;

    public CourseVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_cvideos, this);

        // 获取控件
        showListView = findViewById(R.id.showListView);
        showGrdView = findViewById(R.id.showGrdView);
        recyclerView = findViewById(R.id.recyclerView);

        // 创建 2 种布局
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mGridLayoutManager = new GridLayoutManager(mContext, 5);
        initView();
    }

    private void initView() {
        baseQuickAdapter = new CourseVideoAdapter(mContext, cVideos);
        baseQuickAdapter.addChildClickViewIds(R.id.cVideoName, R.id.cVideoIndex);
        baseQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (listener != null) {
                listener.onConfirm(position);
            }
        });
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(baseQuickAdapter);

        showListView.setOnClickListener(v -> {
            showListView.setVisibility(View.GONE);
            showGrdView.setVisibility(View.VISIBLE);

            itemType = CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_GRID;
            recyclerView.setLayoutManager(mGridLayoutManager);
            baseQuickAdapter.setList(CourseDetailResponse.MultiItemTypeCVideo.modifyItemType(cVideos, itemType));
        });
        showGrdView.setOnClickListener(v -> {
            showListView.setVisibility(View.VISIBLE);
            showGrdView.setVisibility(View.GONE);

            itemType = CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST;
            recyclerView.setLayoutManager(mLinearLayoutManager);
            baseQuickAdapter.setList(CourseDetailResponse.MultiItemTypeCVideo.modifyItemType(cVideos, itemType));
        });
    }

    public void setList(CourseDetailResponse courseDetailResponse, List<CourseDetailResponse.MultiItemTypeCVideo> cVideos) {
        this.cVideos.clear();
        this.cVideos.addAll(cVideos);
        baseQuickAdapter.setCourseDetailResponse(courseDetailResponse);
        baseQuickAdapter.notifyDataSetChanged();
    }

    public void setCallBackListener (CallBackListener listener) {
        this.listener = listener;
    }

    public static interface CallBackListener {
        void onConfirm(int position);
    }
}
