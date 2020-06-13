package com.linkknown.ilearning.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;

import butterknife.BindView;

public class HuodongActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong);
    }
}

