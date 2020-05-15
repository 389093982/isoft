package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;

import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        userName = getIntent().getStringExtra(Constants.USER_NAME);

        ButterKnife.bind(this);
    }


}
