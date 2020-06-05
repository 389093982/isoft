package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.RegistResponse;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.util.CheckParamUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    // 生成验证码 tip
    @BindView(R.id.createVerifyCodeTip)
    public TextView createVerifyCodeTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
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

    // 只要有用户名、密码、确认密码、验证码一个发生改变就触发
    // 校验成功则输入框可点击
    @OnTextChanged({R.id.userName, R.id.passwd, R.id.rePasswd, R.id.verifyCode})
    public void onTextChanged() {
        String _userName = StringUtils.trim(userName.getText().toString());
        String _verifyCode = StringUtils.trim(verifyCode.getText().toString());
        String _passwd = StringUtils.trim(passwd.getText().toString());
        String _rePasswd = StringUtils.trim(rePasswd.getText().toString());

        // 简单的条件判断: 用户名和密码不能为空.
        if (StringUtilEx.isAllNotEmpty(_userName, _passwd, _rePasswd, _verifyCode) && StringUtils.equals(_passwd, _rePasswd)) {
            registBtn.setEnabled(true);
        } else {
            registBtn.setEnabled(false);
        }
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
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.registBtn:
                handleRegist();
                break;
            case R.id.createVerifyCodeTip:
                handleCreateVerifyCode();
                break;
            default:
                break;
        }
    }

    private void handleCreateVerifyCode() {
        UserService.createVerifyCode(StringUtils.trim(userName.getText().toString()));
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
    }

    private void handleRegist() {
        String _userName = StringUtils.trim(userName.getText().toString());
        String _verifyCode = StringUtils.trim(verifyCode.getText().toString());
        String _passwd = StringUtils.trim(passwd.getText().toString());
        String _rePasswd = StringUtils.trim(rePasswd.getText().toString());

        if (!validate(_userName)) {
            onSignupFailed("注册失败！");
            return;
        }
        if (!_passwd.equals(_rePasswd)) {
            rePasswd.setError("两次密码输入不一致！");
        } else {
            regist(_userName, _passwd, "小猫小狗", _verifyCode, "linkknown");
        }
    }

    public void regist (String username, String passwd, String nickname, String verifyCode, String third_user_type) {
        LinkKnownApiFactory.getLinkKnownApi().regist(username, passwd, nickname, verifyCode, third_user_type)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<RegistResponse>() {
                    @Override
                    public void onNext(RegistResponse registResponse) {
                        if (registResponse.isSuccess()) {
                            ToastUtil.showText(mContext, "注册成功！");
                            // 记住账号
                            LoginUtil.memoryAccount(mContext, userName.getText().toString(), passwd, null);
                            // 调往登录页面
                            UIUtils.gotoActivity(mContext, LoginActivity.class);
                        } else {
                            Log.e("onNext =>", "regist failed！" + registResponse.getErrorMsg());
                            ToastUtil.showText(mContext, registResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, mContext.getResources().getString(R.string.regist_failed));
                    }
                });
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
