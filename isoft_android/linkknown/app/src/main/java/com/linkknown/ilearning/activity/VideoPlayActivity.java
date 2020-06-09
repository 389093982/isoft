package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseVideoAdapter;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends AppCompatActivity {

    private int itemType = CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_GRID;

    //栅格布局
    private GridLayoutManager mGridLayoutManager;
    //列表布局
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.video_player)
    public JzvdStd player;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.showListView)
    public ImageView showListView;
    @BindView(R.id.showGrdView)
    public ImageView showGrdView;

    private List<CourseDetailResponse.MultiItemTypeCVideo> cVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        CourseDetailResponse courseDetailResponse = (CourseDetailResponse)intent.getExtras().getSerializable("courseDetailResponse");
        List<CourseDetailResponse.CVideo> cvs = courseDetailResponse.getCVideos();
        cVideos = CourseDetailResponse.MultiItemTypeCVideo.setItemType(cvs, CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_GRID);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        initVideoInfo();
        initVideoPlayer();
    }

    private void initVideoInfo() {
        BaseQuickAdapter baseQuickAdapter = new CourseVideoAdapter(this, cVideos);
        baseQuickAdapter.addChildClickViewIds(R.id.cVideoName);
        baseQuickAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CourseDetailResponse.CVideo cVideo = cVideos.get(position).getCVideo();
            player.setUp(UIUtils.replaceMediaUrl(cVideo.getFirst_play()), cVideo.getVideo_name());
            // 自动播放
            player.startVideo();
        });
        recyclerView.setLayoutManager(mGridLayoutManager);
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

    private void initVideoPlayer() {
        CourseDetailResponse.CVideo cVideo = cVideos.get(0).getCVideo();
        player.setUp(UIUtils.replaceMediaUrl(cVideo.getFirst_play()), cVideo.getVideo_name());
        // 自动播放
        player.startVideo();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
