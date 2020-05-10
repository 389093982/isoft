package com.linkknown.ilearning.activity;

import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.link_login)
    public TextView link_login;
    @BindView(R.id.back)
    public ImageView back;
    @BindView(R.id.registBtn)
    public Button registBtn;

    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.passwd)
    public TextView passwd;
    @BindView(R.id.rePasswd)
    public TextView rePasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        registBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        link_login.setOnClickListener(this);

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
