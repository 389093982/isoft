package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.RegistResponse;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.util.CheckParamUtil;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.link_login)
    public TextView link_login;
    @BindView(R.id.back)
    public ImageView back;
    @BindView(R.id.registBtn)
    public Button registBtn;

    @BindView(R.id.userName)
    public TextView userName;
    // 密码和确认密码
    @BindView(R.id.passwd)
    public TextView passwd;
    @BindView(R.id.rePasswd)
    public TextView rePasswd;
    // 验证码输入框
    @BindView(R.id.verifyCode)
    public TextView verifyCode;
    // 生产验证码 tip
    @BindView(R.id.createVerifyCodeTip)
    public TextView createVerifyCodeTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mContext = this;
        ButterKnife.bind(this);

        init();

        bindData();
    }

    private void bindData () {
        LiveEventBus.get("registResponse", RegistResponse.class).observe(this, new Observer<RegistResponse>() {
            @Override
            public void onChanged(RegistResponse registResponse) {
                if (registResponse.getErrorMsg() != null) {
                    ToastUtil.showText(getApplicationContext(), registResponse.getErrorMsg());
                    return;
                }
//                if (StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
//                    // 登录成功后记录登录账号,供下次登录自动填充表单,不用再次输入
//                    memoryAccount(usernameEditText, passwordEditText);
//
//                    updateUiWithUser(loginUserResponse);
//
//                    setResult(Activity.RESULT_OK);
//                    finish();
//                }
            }
        });
    }

    private void init() {
        // 禁用生成验证码
        createVerifyCodeTip.setEnabled(false);
        back.setOnClickListener(this);
        registBtn.setOnClickListener(this);
        createVerifyCodeTip.setOnClickListener(this);
        link_login.setOnClickListener(this);

        userName.setOnFocusChangeListener((v, hasFocus) -> {
            TextView textView = (TextView)v;
            // 失去焦点事件
            if (!hasFocus) {
                if (UserService.isUserNameValid(StringUtils.trim(textView.getText().toString()))) {
                    // 验证码倒计时不应该设置为 true
                    createVerifyCodeTip.setEnabled(true);
                } else {
                    ToastUtil.showText(mContext, "请使用邮箱进行注册！");
                    createVerifyCodeTip.setEnabled(false);
                }
            }
        });
        passwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (!UserService.isPasswordValid(StringUtils.trim(textView.getText().toString()))) {
                    ToastUtil.showText(mContext, "密码长度必须大于 5 位字符！");
                }
            }
        });
        rePasswd.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (!UserService.isPasswordValid(StringUtils.trim(textView.getText().toString()))) {
                    ToastUtil.showText(mContext, "确认密码长度必须大于 5 位字符！");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.link_login:
                UIUtils.gotoActivity(this, LoginActivity.class);
                // Android中不同Activity之间的切换是不可避免的事情，那么怎么才能让Acitivity的切换更优雅呢，
                // Android中提供了一个方法来解决这个问题，即overridePendingTransition(A，B)函数
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.registBtn:
                regist();
                break;
            case R.id.createVerifyCodeTip:
                // 30s 倒计时,一次一秒
                new CountDownTimer(30 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // 禁用
                        createVerifyCodeTip.setEnabled(false);
                        // 倒计时秒数
                        long second = millisUntilFinished / 1000;
                        if (second > 28) {
                            createVerifyCodeTip.setText("发送中...");
                        } else {
                            createVerifyCodeTip.setText(second + "s后重新获取");
                        }
                    }
                    @Override
                    public void onFinish() {
                        createVerifyCodeTip.setEnabled(true);
                        createVerifyCodeTip.setText("重新获取验证码");

                    }
                }.start();
                break;
            default:
                break;
        }
    }

    private void regist() {
        String _userName = userName.getText().toString();
        String _passwd = passwd.getText().toString();
        String _rePasswd = rePasswd.getText().toString();

        if (!validate(_userName)) {
            onSignupFailed("注册失败！");
            return;
        }
        if (!_passwd.equals(_rePasswd)) {
            rePasswd.setError("两次密码输入不一致！");
        } else {
            UserService.regist(this, _userName, _passwd, "小猫小狗", "123456", "linkknown");
        }
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        registBtn.setEnabled(true);
    }

    private boolean validate(String username) {
        if (CheckParamUtil.checkRegex(username, CheckParamUtil.REGEX_EMAIL)) {
            return true;
        }
        return false;
    }

}
