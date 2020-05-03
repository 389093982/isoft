package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.video_player)
    public JCVideoPlayerStandard player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int course_id = intent.getIntExtra("course_id", -1);
        int video_id = intent.getIntExtra("video_id", -1);
        String first_play = intent.getStringExtra("first_play");

        boolean setUp = player.setUp(UIUtils.replaceMediaUrl(first_play), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");

    }
}
