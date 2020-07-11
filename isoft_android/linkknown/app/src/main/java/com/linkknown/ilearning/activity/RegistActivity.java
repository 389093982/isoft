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
                    ToastUtil.showText(mContext, "请使用 手机/邮箱 进行注册！");
                    createVerifyCodeTip.setEnabled(false);
                }
            }
        });
        nickName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "昵称不能为空！");
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
                if (StringUtils.isEmpty(textView.getText().toString())) {
                    ToastUtil.showText(mContext, "密码不能为空！");
                }
            }
        });
        rePasswd.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(textView.getText().toString())) {
                    ToastUtil.showText(mContext, "确认密码不能为空！");
                }
            }
        });
    }


    /**
     * 1.只要有用户名、密码、确认密码、验证码、昵称，一个发生改变就触发
     * 2.性别 radio 发生变化单独调了该方法
     * 3.界面参数全部非空，才显示 "注册" 按钮
     */
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


    //发送验证码
    private void handleCreateVerifyCode() {
        //发送前再做一道校验
        if (UserService.isUserNameValid(StringUtils.trim(userName.getText().toString()))) {
            UserService.createVerifyCode(StringUtils.trim(userName.getText().toString()));
            // 30s 倒计时,一次一秒
            new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // 禁用
                    createVerifyCodeTip.setEnabled(false);
                    // 倒计时秒数
                    long second = millisUntilFinished / 1000;
                    if (second > 58) {
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
        } else {
            ToastUtil.showText(mContext, "请使用 手机/邮箱 进行注册！");
        }
    }

    //点击注册按钮
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
        if (validateParams(_nickName,_userName,_verifyCode,_passwd,_rePasswd,_gender)){
            regist(_userName, _passwd, _nickName, _gender, _verifyCode, "linkknown");
        }
    }


    //统一校验参数
    private boolean validateParams(String _nickName, String _userName, String _verifyCode, String _passwd, String _rePasswd, String _gender) {
        //校验昵称
        if (StringUtils.isEmpty(_nickName)){
            ToastUtil.showText(mContext,"昵称不能为空");
            return false;
        }
        if (_nickName.length()>10){
            ToastUtil.showText(mContext,"昵称长度不能超过10个字符");
            return false;
        }
        //校验用户名
        if (StringUtils.isEmpty(_userName)) {
            ToastUtil.showText(mContext,"请填写账号");
            return false;
        }
        if (!CheckParamUtil.checkRegex(_userName, CheckParamUtil.REGEX_EMAIL) && !CheckParamUtil.checkRegex(_userName, CheckParamUtil.REGEX_PHONE)) {
            ToastUtil.showText(mContext,"请使用手机号或邮箱注册！");
            return false;
        }
        //验证码
        if (StringUtils.isEmpty(_verifyCode) || _verifyCode.length()!=6) {
            ToastUtil.showText(mContext,"请填写6位验证码");
            return false;
        }
        //密码
        if (StringUtils.isEmpty(_passwd)) {
            ToastUtil.showText(mContext,"请填写密码");
            return false;
        }
        if (!CheckParamUtil.checkRegex(_passwd, CheckParamUtil.REGEX_PASSWD)){
            ToastUtil.showText(mContext,"密码必须由数字或字母组合，长度 6-20");
            return false;
        }
        //确认密码
        if (StringUtils.isEmpty(_rePasswd)) {
            ToastUtil.showText(mContext,"请填写确认密码");
            return false;
        }
        if (!_passwd.equals(_rePasswd)) {
            ToastUtil.showText(mContext,"两次密码输入不一致");
            return false;
        }
        //性别
        if (StringUtils.isEmpty(gender_text) || StringUtils.isEmpty(_gender)) {
            ToastUtil.showText(mContext,"请选择性别");
            return false;
        }
        return true;
    }


    //注册请求
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




}
