package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        ButterKnife.bind(this);

        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "关于");
    }
}
