package com.linkknown.ilearning;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.linkknown.ilearning.adapter.MainActivityFragmentAdapter;
import com.linkknown.ilearning.fragment.ClassifyFragment;
import com.linkknown.ilearning.fragment.HomeFragment;
import com.linkknown.ilearning.fragment.MineFragment;
import com.linkknown.ilearning.viewpage.MainActivityViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigationView)
    public NavigationView navigationView;

    // 用户登录之后显示用户头像及昵称
    private LinearLayout userHeaderInfoLayout;

    // 未登录时显示提示信息
    private RelativeLayout unLoginLayout;

    // 登陆后在头像下方显示的用户名
    private TextView mHeaderUserNameText;

    // 存储首页对应的三个片段
    private List<Fragment> mFragments;

    @BindView(R.id.mViewPage)
    public MainActivityViewPager mViewPage;
    // 底部按钮组
    @BindView(R.id.radioGroup)
    public RadioGroup radioGroup;
    @BindView(R.id.imageButton0)
    public RadioButton rb_home;
    @BindView(R.id.imageButton1)
    public RadioButton rb_category;
    @BindView(R.id.imageButton2)
    public RadioButton rb_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 绑定控件
        this.init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton0:
                mViewPage.setCurrentItem(0);
                break;
            case R.id.imageButton1:
                mViewPage.setCurrentItem(1);
                break;
            case R.id.imageButton2:
                mViewPage.setCurrentItem(2);
                break;
            default:
                break;
        }
        // 设置按钮组样式
        this.setRadioGroupStatus();
    }

    //设置选中和未选择的状态
    private void setRadioGroupStatus() {
        this.setRadioButtonStatus(this.rb_home);
        this.setRadioButtonStatus(this.rb_category);
        this.setRadioButtonStatus(this.rb_mine);
    }

    private void setRadioButtonStatus(RadioButton rb) {
        if (rb.isChecked()) {
            rb.setTextColor(ContextCompat.getColor(this, R.color.button_press));
        } else {
            rb.setTextColor(ContextCompat.getColor(this, R.color.button_normal));
        }
    }

    private void init () {
        initFragment();
        initNavigation();
    }

    private void initNavigation () {
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userHeaderInfoLayout = headerView.findViewById(R.id.user_header_info);
        unLoginLayout = headerView.findViewById(R.id.un_login_dead);
        mHeaderUserNameText = headerView.findViewById(R.id.user_name_header);
    }

    private void initFragment () {
        mFragments = new ArrayList<>();
        // 创建 3 个片段
        mFragments.add(new HomeFragment());
        mFragments.add(new ClassifyFragment());
        mFragments.add(new MineFragment());

        MainActivityFragmentAdapter fragmentAdapter = new MainActivityFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPage.setAdapter(fragmentAdapter);
        //预渲染页面数量
        mViewPage.setOffscreenPageLimit(2);
        //禁用滑动
        mViewPage.setOnTouchListener((v, event) -> true);

        radioGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray2));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.imageButton0:
                    // 获取颜色使用 ContextCompat.getColor 方法
                    radioGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray2));
                    mViewPage.setCurrentItem(0);
                    break;
                case R.id.imageButton1:
                    radioGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    mViewPage.setCurrentItem(1);
                    break;
                case R.id.imageButton2:
                    radioGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    mViewPage.setCurrentItem(2);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
