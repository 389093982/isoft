package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SetUserInfoActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    //昵称
    @BindView(R.id.nick_name)
    public TextView nick_name;

    //性别
    @BindView(R.id.gender)
    public RadioGroup gender;
    public String gender_text;

    //生日
    @BindView(R.id.birthday)
    public TextView birthday;

    //现居住地址
    @BindView(R.id.current_residence)
    public TextView current_residence;

    //家乡
    @BindView(R.id.hometown)
    public TextView hometown;

    //提交按钮
    @BindView(R.id.submitBtn)
    public Button submitBtn;

    //这一块是设置界面传过来的参数
    private String nick_name_param;
    private String gender_param;
    private String birthday_param;
    private String current_residence_param;
    private String hometown_param;
    private String hat_param;
    private String hat_in_use_param;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        mContext = this;
        ButterKnife.bind(this);

        initView();
    }


    public void initView(){
        initToolBar(toolbar,true,"更新用户基本信息");
        //获取参数
        nick_name_param = getIntent().getStringExtra("nick_name");
        gender_param = getIntent().getStringExtra("gender");
        birthday_param = getIntent().getStringExtra("birthday");
        current_residence_param = getIntent().getStringExtra("current_residence");
        hometown_param = getIntent().getStringExtra("hometown");
        hat_param = getIntent().getStringExtra("hat");
        hat_in_use_param = getIntent().getStringExtra("hat_in_use");

        //给用户基本信息界面设置这些参数值
        nick_name.setText(nick_name_param);
        birthday.setText(birthday_param);
        current_residence.setText(current_residence_param);
        hometown.setText(hometown_param);
        if ("male".equals(gender_param.trim())){
            RadioButton radioButton = findViewById(R.id.male);
            gender_text = "male";
            radioButton.setChecked(true);
        }else if ("female".equals(gender_param.trim())){
            RadioButton radioButton = findViewById(R.id.female);
            gender_text = "female";
            radioButton.setChecked(true);
        }

        //给提交按钮设置点击事件
        submitBtn.setOnClickListener(this);

        //设置输入框的触碰监听
        nick_name.setOnFocusChangeListener((v, hasFocus) -> {
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

        birthday.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "请填写生日！");
                }
            }
        });

        current_residence.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "现居住地址不能为空！");
                }
            }
        });

        hometown.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView textView = (TextView)v;
                if (StringUtils.isEmpty(StringUtils.trim(textView.getText().toString()))){
                    ToastUtil.showText(mContext, "家乡不能为空！");
                }
            }
        });

    };


    //设置点击事件
    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.submitBtn:
                updateUserInfo();
                break;
            default:
                break;
        }
    }


    //如果个性签名内容发生变化，自动调用
    @OnTextChanged({R.id.nick_name,R.id.birthday,R.id.current_residence,R.id.hometown})
    public void onTextChanged() {
        String _nick_name = StringUtils.trim(nick_name.getText().toString());
        String _birthday = StringUtils.trim(birthday.getText().toString());
        String _current_residence = StringUtils.trim(current_residence.getText().toString());
        String _hometown = StringUtils.trim(hometown.getText().toString());

        if (StringUtilEx.isAllNotEmpty(_nick_name,_birthday,_current_residence,_hometown,gender_text)) {
            submitBtn.setEnabled(true);
        } else {
            submitBtn.setEnabled(false);
        }
    }


    //更新用户信息
    public void updateUserInfo(){
        String _nick_name = StringUtils.trim(nick_name.getText().toString());
        String _birthday = StringUtils.trim(birthday.getText().toString());
        String _current_residence = StringUtils.trim(current_residence.getText().toString());
        String _hometown = StringUtils.trim(hometown.getText().toString());
        String _gender = "";
        if ("男".equals(gender_text.trim())){
            _gender = "male";
        }else{
            _gender = "female";
        }

        if (validator(_nick_name,_birthday)){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putSerializable("nick_name",_nick_name);
            bundle.putSerializable("gender",_gender);
            bundle.putSerializable("birthday",_birthday);
            bundle.putSerializable("current_residence",_current_residence);
            bundle.putSerializable("hometown",_hometown);
            bundle.putSerializable("hat",hat_param);
            bundle.putSerializable("hat_in_use",hat_in_use_param);

            intent.putExtra("bundle", bundle);
            setResult(200,intent);
            finish();
        }
    };

    //验证参数
    public Boolean validator(String _nick_name, String _birthday){
        if (_nick_name.length()>10){
            ToastUtil.showText(mContext,"昵称不能超过10个字符");
            return false;
        }

        if (_birthday.length()!=8){
            ToastUtil.showText(mContext,"生日格式不对");
            return false;
        }

        return true;
    };


}
