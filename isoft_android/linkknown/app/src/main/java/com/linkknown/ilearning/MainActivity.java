package com.linkknown.ilearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.linkknown.ilearning.activity.IFavoritesActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.NewChannelActivity;
import com.linkknown.ilearning.activity.RegistActivity;
import com.linkknown.ilearning.fragment.FindFragment;
import com.linkknown.ilearning.fragment.MoreFragment;
import com.linkknown.ilearning.interceptor.HeaderInterceptor;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.PermissionUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.superluo.textbannerlibrary.TextBannerView;

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
    @BindView(R.id.headerToolBarLayout)
    public LinearLayout headerToolBarLayout;

    @BindView(R.id.person_head)
    public ImageView person_head;
    @BindView(R.id.userNameText)
    public TextView userNameText;
    @BindView(R.id.toolBarLoginLayout)
    public LinearLayout toolBarLoginLayout;
    @BindView(R.id.toolBarUnLoginLayout)
    public LinearLayout toolBarUnLoginLayout;

    private List<Fragment> mFragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.add_channel)
    ImageView addChannel;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    // 用户登录之后显示用户头像及昵称
    private LinearLayout navigationUserHeaderInfoLayout;

    // 登陆后在头像下方显示的用户名
    private TextView navigationUserNameText;

    // 未登录时显示提示信息的布局
    private RelativeLayout navigationUnLoginLayout;

    ImageView navigationHeaderIconView;
    TextBannerView mingyanTextbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        PermissionUtil.requestOverlayPermission(this);

        // 绑定控件
        init();

        addChannel.setOnClickListener(this);
        headerToolBarLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerToolBarLayout:
                toggleDrawer();
                break;
            case R.id.add_channel:
                UIUtils.gotoActivity(this, NewChannelActivity.class);
                break;
            default:
                break;
        }
    }

    private void init () {
        initFragment();
        initNavigation();

        // 订阅登录信息
        observeLogin();

        // 自动登录
        autoLogin();
    }

    private void autoLogin () {
        // 按返回并没有真正退出应用
        // 没有 tokenString 代表没有登录过
        if (StringUtils.isEmpty(LoginUtil.getTokenString(this))) {
            // 获取存储的用户名和密码
            SharedPreferences preferences = LoginUtil.getUserInfoSharedPreferences(this);
            String username = preferences.getString(Constants.USER_SHARED_PREFERENCES_USER_NAME, "");
            String passwd = preferences.getString(Constants.USER_SHARED_PREFERENCES_PASSWD, "");
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(passwd)) {
                // 自动登录
                UserService.login(this, username, passwd);
            }
        }
    }

    private void observeLogin () {
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observe(this, loginUserResponse -> {
            if (loginUserResponse != null){
                if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {

                    // 1、--------- 左侧 navigationView 显示登录信息
                    // 登录成功，显示登录布局
                    navigationUserHeaderInfoLayout.setVisibility(View.VISIBLE);
                    navigationUnLoginLayout.setVisibility(View.GONE);

                    // 设置登录信息
                    // 异步加载图片,使用 Glide 第三方库
                    Glide.with(this)
                            .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                            .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                            .into(navigationHeaderIconView);
                    // 设置昵称或者用户名
                    navigationUserNameText.setText(StringUtilEx.getFirstNotEmptyStr(loginUserResponse.getNickName(), loginUserResponse.getUserName()));

                    // 2、--------- 顶部 toolbar 显示登录信息
                    toolBarLoginLayout.setVisibility(View.VISIBLE);
                    toolBarUnLoginLayout.setVisibility(View.GONE);

                    // 设置昵称或者用户名
                    userNameText.setText(StringUtilEx.getFirstNotEmptyStr(loginUserResponse.getNickName(), loginUserResponse.getUserName()));
                    Glide.with(this)
                            .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                            .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                            .into(person_head);
                } else {
                    // 1、--------- 左侧 navigationView 显示登录信息
                    // 登录失败
                    navigationUnLoginLayout.setVisibility(View.VISIBLE);
                    navigationUserHeaderInfoLayout.setVisibility(View.GONE);

                    // 2、--------- 顶部 toolbar 显示登录信息
                    toolBarLoginLayout.setVisibility(View.VISIBLE);
                    toolBarUnLoginLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initFragment () {
        // 创建 fragment
        MoreFragment moreFragment1 = new MoreFragment();
        MoreFragment moreFragment2 = new MoreFragment();
        MoreFragment moreFragment3 = new MoreFragment();
//        MoreFragment moreFragment4 = new MoreFragment();
//        MoreFragment moreFragment5 = new MoreFragment();
        MoreFragment moreFragment6 = new MoreFragment();
        FindFragment findFragment = new FindFragment();
        MoreFragment moreFragment8 = new MoreFragment();

        mFragments.add(moreFragment1);
        mFragments.add(moreFragment2);
        mFragments.add(moreFragment3);
//        mFragments.add(moreFragment4);
//        mFragments.add(moreFragment5);
        mFragments.add(moreFragment6);
        mFragments.add(findFragment);
        mFragments.add(moreFragment8);

        // title 限制 2 个字
        titles.add("首页");
        titles.add("免费");
        titles.add("全部");
//        titles.add("分类");
//        titles.add("关注");
        titles.add("推荐");
        titles.add("发现");
        titles.add("更多");

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
        // navigation 未登录布局
        navigationUnLoginLayout = headerView.findViewById(R.id.navigationUnLoginLayout);
        // navigation 已登录布局
        navigationUserHeaderInfoLayout = headerView.findViewById(R.id.navigationUserHeaderInfoLayout);
        // 用户名和头像
        navigationUserNameText = headerView.findViewById(R.id.navigationUserNameText);
        navigationHeaderIconView = headerView.findViewById(R.id.navigationHeaderIconView);
        mingyanTextbanner = headerView.findViewById(R.id.mingyanTextbanner);




        //设置数据
        List<String> list = new ArrayList<>();

        list.add("发现程序之美，遇见最好的自己");
        list.add("今天你学习了吗？");
        list.add("你是最棒的，奔跑吧孩子！");
        list.add("路漫漫其修远兮，吾将上下而求索");

        //调用setDatas(List<String>)方法后,TextBannerView自动开始轮播
        //注意：此方法目前只接受List<String>类型
        mingyanTextbanner.setDatas(list);
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
        } else if (id == R.id.shoucang) {
            UIUtils.gotoActivity(this, IFavoritesActivity.class);
        }

        // 抽屉关闭
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * DrawerLayout侧滑菜单开关
     */
    public void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按返回键清空 tokenString
            LoginUtil.logout(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume()和onStop()方法分别调用startViewAnimator()和stopViewAnimator()，防止返回页面出现文字重影问题
        mingyanTextbanner.startViewAnimator();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mingyanTextbanner.stopViewAnimator();
    }
}
