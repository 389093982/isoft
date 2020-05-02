package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    // 返回按钮
    @BindView(R.id.btnToBack)
    public ImageView btnToBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        ButterKnife.bind(this);

        btnToBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToBack:
                finish();
                break;
            default:
                break;
        }
    }
}
