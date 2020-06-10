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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    //用户名
    @BindView(R.id.userName)
    public TextView userName;

    //昵称
    @BindView(R.id.nickName)
    public TextView nickName;

    //性别radio  && 文本
    @BindView(R.id.gender)
    public RadioGroup gender;
    public String gender_text;

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
        nickName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.trim(textView.getText().toString()).length()>10) {
                    ToastUtil.showText(mContext, "昵称? 请不要超过10个字符哦");
                }
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // 参数1：你点击的RadioButton的组 参数2：是你选中的控件的id
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                gender_text = rb.getText().toString();
                onTextChanged();
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


    // 只要有用户名、密码、确认密码、验证码、昵称，一个发生改变就触发
    // 校验成功则输入框可点击
    @OnTextChanged({R.id.userName, R.id.verifyCode,R.id.nickName,R.id.passwd, R.id.rePasswd})
    public void onTextChanged() {
        String _userName = StringUtils.trim(userName.getText().toString());
        String _verifyCode = StringUtils.trim(verifyCode.getText().toString());
        String _nickName = StringUtils.trim(nickName.getText().toString());
        String _passwd = StringUtils.trim(passwd.getText().toString());
        String _rePasswd = StringUtils.trim(rePasswd.getText().toString());

        if (StringUtilEx.isAllNotEmpty(_userName,_nickName,gender_text, _passwd, _rePasswd, _verifyCode)) {
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

    //前去注册
    private void handleRegist() {
        String _nickName = StringUtils.trim(nickName.getText().toString());
        String _userName = StringUtils.trim(userName.getText().toString());
        String _verifyCode = StringUtils.trim(verifyCode.getText().toString());
        String _passwd = StringUtils.trim(passwd.getText().toString());
        String _rePasswd = StringUtils.trim(rePasswd.getText().toString());
        String _gender = "";
        if ("男".equals(gender_text.trim())){
            _gender = "male";
        }else{
            _gender = "female";
        }

        if (StringUtils.isEmpty(_nickName)) {
            onSignupFailed("请填写昵称！");
            return;
        }
        if (!validate(_userName)) {   //验证账号，使用了单独方法
            onSignupFailed("请填写账号！");
            return;
        }
        if (StringUtils.isEmpty(_verifyCode)) {
            onSignupFailed("请填写验证码！");
            return;
        }
        if (StringUtils.isEmpty(_passwd)) {
            onSignupFailed("请填写验证码！");
            return;
        }
        if (StringUtils.isEmpty(_rePasswd)) {
            onSignupFailed("请填写验证码！");
            return;
        }
        if (!_passwd.equals(_rePasswd)) {
            onSignupFailed("两次密码输入不一致！");
            return;
        }
        if (StringUtils.isEmpty(gender_text) || StringUtils.isEmpty(_gender)) {
            onSignupFailed("请选择性别！");
            return;
        }
        regist(_userName, _passwd, _nickName, _gender, _verifyCode, "linkknown");
    }

    public void regist (String username, String passwd, String nickname, String gender, String verifyCode, String third_user_type) {
        LinkKnownApiFactory.getLinkKnownApi().regist(username, passwd, nickname, gender, verifyCode, third_user_type)
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
