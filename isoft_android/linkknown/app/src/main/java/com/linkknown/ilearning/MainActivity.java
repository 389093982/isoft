package com.linkknown.ilearning;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.activity.HomeActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.RegistActivity;
import com.linkknown.ilearning.fragment.SpaceFragment;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.util.ui.UIUtils;

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

    // 存储首页对应的三个片段
    private List<Fragment> mFragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    // 用户登录之后显示用户头像及昵称
    private LinearLayout userHeaderInfoLayout;

    // 登陆后在头像下方显示的用户名
    private TextView mHeaderUserNameText;

    // 未登录时显示提示信息的布局
    private RelativeLayout unLoginLayout;

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

    }

    private void init () {
        initFragment();
        initNavigation();
    }

    private void initFragment () {
        // 创建 fragment
        SpaceFragment spaceFragment1 = new SpaceFragment();
        SpaceFragment spaceFragment2 = new SpaceFragment();
        SpaceFragment spaceFragment3 = new SpaceFragment();
        SpaceFragment spaceFragment4 = new SpaceFragment();
        SpaceFragment spaceFragment5 = new SpaceFragment();
        SpaceFragment spaceFragment6 = new SpaceFragment();
        SpaceFragment spaceFragment7 = new SpaceFragment();

        mFragments.add(spaceFragment1);
        mFragments.add(spaceFragment2);
        mFragments.add(spaceFragment3);
        mFragments.add(spaceFragment4);
        mFragments.add(spaceFragment5);
        mFragments.add(spaceFragment6);
        mFragments.add(spaceFragment7);

        // title 限制 2 个字
        titles.add("首页");
        titles.add("圈子");
        titles.add("热门");
        titles.add("分类");
        titles.add("关注");
        titles.add("发现");
        titles.add("推荐");

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
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

    // navigation view 点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shouye) {
            UIUtils.gotoActivity(this, HomeActivity.class);
            finish();
//            // Handle the camera action
//            mViewPage.setCurrentItem(0);
//            //底部ImageView点击之后变色
//            rb_home.setChecked(true);
//            setRadioGroupStatus();
        } else if (id == R.id.fenlei) {
//            mViewPage.setCurrentItem(1);
//            //底部ImageView点击之后变色
//            rb_category.setChecked(true);
//            setRadioGroupStatus();
        } else if (id == R.id.wode) {
//            mViewPage.setCurrentItem(2);
//            //底部ImageView点击之后变色
//            rb_mine.setChecked(true);
//            setRadioGroupStatus();
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
            UIUtils.gotoActivity(this, RegistActivity.class);
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
            UserService.logout(this);
        }

        // 抽屉关闭
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
