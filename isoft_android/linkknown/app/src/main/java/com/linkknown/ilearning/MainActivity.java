package com.linkknown.ilearning;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.activity.CourseListActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnToLogin;
    private Button btnToCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnToLogin = findViewById(R.id.btnToLogin);
        btnToCourseList = findViewById(R.id.btnToCourseList);

        btnToLogin.setOnClickListener(this);
        btnToCourseList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToLogin:
                UIUtils.gotoActivity(this, LoginActivity.class);
                break;
            case R.id.btnToCourseList:
                UIUtils.gotoActivity(this, CourseListActivity.class);
                break;
            default:
                break;
        }
    }
}
