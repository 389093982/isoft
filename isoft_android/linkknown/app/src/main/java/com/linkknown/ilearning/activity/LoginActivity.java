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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

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

                    updateUiWithUser(loginUserResponse);

                    setResult(Activity.RESULT_OK);
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

        loginButton.setOnClickListener(v -> {
            // 显示登录进度 loading
            loadingProgressBar.setVisibility(View.VISIBLE);
            // 调用登录接口
            UserService.login(this, usernameEditText.getText().toString(), passwordEditText.getText().toString());
        });
    }

    // 记录上次登录参数
    private void memoryAccount(EditText usernameEditText, EditText passwordEditText) {
        SharedPreferences preferences = this.getSharedPreferences("UserSharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", usernameEditText.getText().toString());
        editor.putString("passwd", passwordEditText.getText().toString());
        editor.apply();
    }

    // 自动填充登录表单
    private void fillAccountFromMemory(EditText usernameEditText, EditText passwordEditText, Button loginButton) {
        SharedPreferences preferences = this.getSharedPreferences("UserSharedPreferences", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String passwd = preferences.getString("passwd", "");
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(passwd)) {
            usernameEditText.setText(username);
            passwordEditText.setText(passwd);
            loginButton.setEnabled(true);
        }
    }

    private void updateUiWithUser(LoginUserResponse loginUserResponse) {
        String welcome = getString(R.string.welcome) + loginUserResponse.getUserName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

}
