package com.linkknown.ilearning.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.MainActivity;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.userName)
    EditText usernameEditText;
    @BindView(R.id.passwd)
    EditText passwordEditText;
    @BindView(R.id.loginBtn)
    Button loginButton;
    @BindView(R.id.loading)
    ProgressBar loadingProgressBar;
    @BindView(R.id.link_regist)
    TextView link_regist;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ButterKnife.bind(this);

        fillAccountFromMemory(usernameEditText, passwordEditText, loginButton);

        LiveEventBus.get("loginFormState", UserService.LoginFormState.class).observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observe(this, loginUserResponse -> {
            if (loginUserResponse != null) {
                loadingProgressBar.setVisibility(View.GONE);
                if (loginUserResponse.getErrorMsg() != null) {
                    ToastUtil.showText(this, loginUserResponse.getErrorMsg());
                    return;
                }
                if (StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                    // 登录成功后记录登录账号,供下次登录自动填充表单,不用再次输入
                    memoryAccount(usernameEditText, passwordEditText);
                    Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG).show();
                    UIUtils.gotoActivity(mContext, MainActivity.class);
                    finish();
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 校验输入框
                UserService.validateLoginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        // 文本框设置 listener
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        // setOnEditorActionListener 编辑完之后点击软键盘上的各种键才会触发
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                UserService.login(this, usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });
        loginButton.setOnClickListener(this);
        link_regist.setOnClickListener(this);
    }

    // 记录上次登录参数
    private void memoryAccount(TextView usernameEditText, TextView passwordEditText) {
        SharedPreferences preferences = this.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_SHARED_PREFERENCES_USER_NAME, usernameEditText.getText().toString());
        editor.putString(Constants.USER_SHARED_PREFERENCES_PASSWD, passwordEditText.getText().toString());
        editor.apply();
    }

    // 自动填充登录表单
    private void fillAccountFromMemory(TextView usernameEditText, TextView passwordEditText, Button loginButton) {
        SharedPreferences preferences = this.getSharedPreferences(Constants.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String username = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
        String passwd = preferences.getString(Constants.USER_SHARED_PREFERENCES_PASSWD, "");
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(passwd)) {
            usernameEditText.setText(username);
            passwordEditText.setText(passwd);
            loginButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.link_regist:
                UIUtils.gotoActivity(this, RegistActivity.class);
                // Android中不同Activity之间的切换是不可避免的事情，那么怎么才能让Acitivity的切换更优雅呢，
                // Android中提供了一个方法来解决这个问题，即overridePendingTransition(A，B)函数
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.loginBtn:
                // 显示登录进度 loading
                loadingProgressBar.setVisibility(View.VISIBLE);
                // 调用登录接口
                UserService.login(this, usernameEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            default:
                break;
        }
    }
}
