package com.linkknown.ilearning.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.ui.login.LoginViewModel;
import com.linkknown.ilearning.util.ui.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 获取 viewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        LiveEventBus.get("loginFormState", LoginViewModel.LoginFormState.class).observe(this, loginFormState -> {
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

        LiveEventBus.get("loginResult", LoginViewModel.LoginResult.class).observe(this, loginResult -> {
            if (loginResult != null) {
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getErrorMsg() != null) {
                    ToastUtil.showText(this, loginResult.getErrorMsg());
                }
                if (loginResult.getLoggedInUser() != null) {
                    updateUiWithUser(loginResult.getLoggedInUser());

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
                loginViewModel.validateLoginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        // 文本框设置 listener
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        // setOnEditorActionListener 编辑完之后点击软键盘上的各种键才会触发
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(this, usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            // 显示登录进度 loading
            loadingProgressBar.setVisibility(View.VISIBLE);
            // 调用登录接口
            loginViewModel.login(this, usernameEditText.getText().toString(), passwordEditText.getText().toString());
        });
    }

    private void updateUiWithUser(LoginViewModel.LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getUserName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

}
