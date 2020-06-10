package com.linkknown.ilearning.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.widget.CourseVideoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.video_player)
    public JzvdStd player;

    @BindView(R.id.courseVideoView)
    public CourseVideoView courseVideoView;

    private List<CourseDetailResponse.CVideo> cVideos;
    private CourseDetailResponse courseDetailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        courseDetailResponse = (CourseDetailResponse)intent.getExtras().getSerializable("courseDetailResponse");
        int position = intent.getExtras().getInt("position");
        cVideos = courseDetailResponse.getCVideos();

        initVideoInfo();
        initVideoPlayer(position);
    }

    private void initVideoInfo() {
        courseVideoView.setCallBackListener(position -> {
            initVideoPlayer(position);
        });
        courseVideoView.setList(CourseDetailResponse.MultiItemTypeCVideo.setItemType(cVideos, CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST));
    }

    private void initVideoPlayer(int position) {
        CourseDetailResponse.CVideo cVideo = cVideos.get(position);
        player.setUp(UIUtils.replaceMediaUrl(cVideo.getFirst_play()), StringUtilEx.getFileNameNoEx(cVideo.getVideo_name()));

        player.titleTextView.setTextSize(14);
        player.titleTextView.setTextColor(UIUtils.getResourceColor(this, R.color.white));
        UIUtils.setImage(this, player.posterImageView, courseDetailResponse.getCourse().getSmall_image());

        // 设置容器内播放器高,解决黑边（视频全屏）
        player.setVideoImageDisplayType(JzvdStd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);

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
