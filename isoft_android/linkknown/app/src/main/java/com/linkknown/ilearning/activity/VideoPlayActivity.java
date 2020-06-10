package com.linkknown.ilearning.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        CourseDetailResponse courseDetailResponse = (CourseDetailResponse)intent.getExtras().getSerializable("courseDetailResponse");
        int position = intent.getExtras().getInt("position");
        cVideos = courseDetailResponse.getCVideos();

        initVideoInfo();
        initVideoPlayer(position);

        // 图片也占用了状态栏,故需要使用沉浸式状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initVideoInfo() {
        courseVideoView.setCallBackListener(position -> {
            CourseDetailResponse.CVideo cVideo = cVideos.get(position);
            player.setUp(UIUtils.replaceMediaUrl(cVideo.getFirst_play()), cVideo.getVideo_name());
            // 自动播放
            player.startVideo();
        });
        courseVideoView.setList(CourseDetailResponse.MultiItemTypeCVideo.setItemType(cVideos, CourseDetailResponse.MultiItemTypeCVideo.ITEM_TYPE_LIST));
    }

    private void initVideoPlayer(int position) {
        CourseDetailResponse.CVideo cVideo = cVideos.get(position);
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
