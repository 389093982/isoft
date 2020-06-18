package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SetUserSignatureActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //个性签名
    @BindView(R.id.userSignature)
    TextView userSignature;
    String signature_text_old = "";

    //提交按钮
    @BindView(R.id.setSignatureBtn)
    Button setSignatureBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_signature);
        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    //初始化界面
    public void initView(){
        initToolBar(toolbar,true,"设置我的个性签名");
        signature_text_old = getIntent().getStringExtra("signature_text_old");
        userSignature.setText(signature_text_old);
        setSignatureBtn.setOnClickListener(this);
    };

    //设置点击事件
    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.setSignatureBtn:
                setSignature();
                break;
            default:
                break;
        }
    }

    //如果个性签名内容发生变化，自动调用
    @OnTextChanged({R.id.userSignature})
    public void onTextChanged() {
        String _userSignature = StringUtils.trim(userSignature.getText().toString());
        if (StringUtilEx.isAllNotEmpty(_userSignature)) {
            setSignatureBtn.setEnabled(true);
        } else {
            setSignatureBtn.setEnabled(false);
        }
    }

    //提交个性签名
    public void setSignature(){
        if (userSignature.getText().toString().length()<=30){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("signature_text",userSignature.getText().toString());
            intent.putExtra("bundle", bundle);
            setResult(200,intent);
            finish();
        }else{
            ToastUtil.showText(mContext,"不能超过30个字符！");
        }

    };

}
