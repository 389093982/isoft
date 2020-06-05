package com.linkknown.ilearning.activity;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 意见或建议页面
 */
public class AdviseActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise);

        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "反馈意见");
    }
}
