package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolBar(toolbar, true, "商业合作");
    }

}
