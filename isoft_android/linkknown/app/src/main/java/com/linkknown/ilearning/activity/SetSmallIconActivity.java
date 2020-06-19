package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetSmallIconActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.small_icon)
    public ImageView small_icon;

    @BindView(R.id.setSmallIconBtn)
    public Button setSmallIconBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_small_icon);
        mContext = this;
        ButterKnife.bind(this);

        initView();
    }


    public void initView(){
        initToolBar(toolbar,true,"设置我的头像");
        //获取传过来的参数，展示当前头像
        UIUtils.setImage(mContext, small_icon, LoginUtil.getHeaderIcon(mContext));
    };


    @Override
    public void onClick(View v) {

    }





}
