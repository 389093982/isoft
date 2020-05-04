package com.linkknown.ilearning;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.adapter.MainActivityFragmentAdapter;
import com.linkknown.ilearning.fragment.ClassifyFragment;
import com.linkknown.ilearning.fragment.HomeFragment;
import com.linkknown.ilearning.fragment.MineFragment;
import com.linkknown.ilearning.manager.UserServiceManager;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.linkknown.ilearning.viewpage.MainActivityViewPager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigationView)
    public NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    // 用户登录之后显示用户头像及昵称
    private LinearLayout userHeaderInfoLayout;

    // 登陆后在头像下方显示的用户名
    private TextView mHeaderUserNameText;

    // 未登录时显示提示信息的布局
    private RelativeLayout unLoginLayout;

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
        mHeaderUserNameText = headerView.findViewById(R.id.user_name_header);
        ImageView headerIconView = headerView.findViewById(R.id.headerIconView);

        unLoginLayout = headerView.findViewById(R.id.un_login_dead);

        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observe(this, loginUserResponse -> {
            if (loginUserResponse != null){
                if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                    // 登录成功，显示登录布局
                    userHeaderInfoLayout.setVisibility(View.VISIBLE);

                    unLoginLayout.setVisibility(View.GONE);

                    // 设置登录信息
                    // 异步加载图片,使用 Glide 第三方库
                    Glide.with(this)
                            .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                            .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                            .into(headerIconView);
                    mHeaderUserNameText.setText(loginUserResponse.getUserName());
                } else {
                    // 登录失败
                    unLoginLayout.setVisibility(View.VISIBLE);
                    userHeaderInfoLayout.setVisibility(View.GONE);
                }
            }
        });
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

    // navigation view 点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shouye) {
            // Handle the camera action
            mViewPage.setCurrentItem(0);
            //底部ImageView点击之后变色
            rb_home.setChecked(true);
            setRadioGroupStatus();
        } else if (id == R.id.fenlei) {
            mViewPage.setCurrentItem(1);
            //底部ImageView点击之后变色
            rb_category.setChecked(true);
            setRadioGroupStatus();
        } else if (id == R.id.wode) {
            mViewPage.setCurrentItem(2);
            //底部ImageView点击之后变色
            rb_mine.setChecked(true);
            setRadioGroupStatus();
        } else if (id == R.id.denglu) {
//            //intent跳转
//            if (LoginCheckUtil.isLogin(this)) {
//                Toast.makeText(this, "您已登录过了，请先注销", Toast.LENGTH_SHORT).show();
//            } else {
//                UIUtils.gotoActivity(this, LoginActivity.class);
//            }
            UIUtils.gotoActivity(this, LoginActivity.class);
        } else if (id == R.id.zhuce) {
            //intent跳转
//            if (LoginCheckUtil.isLogin(this)) {
//                Toast.makeText(this, "您已登录过了，请先注销", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
//                startActivity(intent);
//            }

        } else if (id == R.id.zhuxiao) {
            //intent跳转
//            if (LoginCheckUtil.isLogin(this)) {
//                SharedPreferences.Editor editor = getSharedPreferences("userInfo", 0).edit();
//                editor.clear();
//                editor.commit();
//                Toast.makeText(this, "您已注销", Toast.LENGTH_SHORT).show();
//
//                //刷新UI显示
//                refreshUI(LoginCheckUtil.isLogin(this));
//            } else {
//                Toast.makeText(this, "您未登录不用注销", Toast.LENGTH_SHORT).show();
//            }
            UserServiceManager.logout(this);
        }

        // 抽屉关闭
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
