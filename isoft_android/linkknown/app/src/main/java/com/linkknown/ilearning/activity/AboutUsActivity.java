package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.userAgreementView)
    public TextView userAgreementView;
    @BindView(R.id.businessView)
    public TextView businessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        mContext = this;

        ButterKnife.bind(this);

        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "关于链知");

        userAgreementView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, UserAgreementActivity.class));
        businessView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, BusinessActivity.class));
    }
}
