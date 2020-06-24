package com.linkknown.ilearning.activity.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.util.ui.UIUtils;

public class DialogUserLoginConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去除 title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //顶部设置为透明
        UIUtils.setTopTransparent(this);

        setContentView(R.layout.dialog_user_loginconfirm);

        initView();
    }

    private void initView() {

        View btnCancel = findViewById(R.id.btn_cancel);
        View btnSubmit = findViewById(R.id.btn_submit);

        // 点击关闭对话框
        btnCancel.setOnClickListener(v -> {
            finish();
        });
        btnSubmit.setOnClickListener(v -> {
            // 关闭对话框并前往登录页面
            UIUtils.gotoActivity(this, LoginActivity.class);
            finish();
        });
    }

    @Override
    public void finish() {
        super.finish();

        // activity 退出动画
        this.overridePendingTransition(0,0);
    }
}
