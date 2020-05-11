package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;

import com.linkknown.ilearning.R;

import butterknife.ButterKnife;

public class NewChannelActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        mContext = this;
        ButterKnife.bind(this);

        init();
    }

    private void init () {
        toolbar = findViewById(R.id.toolbar);
        initToolBar(toolbar, true, "拖拽排序");
    }
}
