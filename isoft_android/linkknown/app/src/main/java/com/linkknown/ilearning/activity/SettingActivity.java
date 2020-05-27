package com.linkknown.ilearning.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

// 设置页面
public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "设置");

        initFragment();
    }

    private void initFragment () {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new SettingFragment())
                .commit();
    }
}
