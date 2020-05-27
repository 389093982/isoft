package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.video_player)
    public JzvdStd player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String course_name = intent.getStringExtra("course_name");
        String video_name = intent.getStringExtra("video_name");
        int course_id = intent.getIntExtra("course_id", -1);
        int video_id = intent.getIntExtra("video_id", -1);
        String first_play = intent.getStringExtra("first_play");

        player.setUp(UIUtils.replaceMediaUrl(first_play), video_name);
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
