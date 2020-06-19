package com.linkknown.ilearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.PayOrderActivity;
import com.linkknown.ilearning.activity.RegistActivity;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.fragment.ClassifyFragment;
import com.linkknown.ilearning.fragment.FindFragment;
import com.linkknown.ilearning.fragment.HomeFragment;
import com.linkknown.ilearning.fragment.MineFragment;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.popup.BottomQuickWindow;
import com.linkknown.ilearning.service.UserService;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.superluo.textbannerlibrary.TextBannerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    // 抽屉布局
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    // 抽屉中的导航
    @BindView(R.id.navigationView)
    public NavigationView navigationView;

    // 用户登录之后显示用户头像及昵称
    private LinearLayout navigationLoginLayout;
    
    // 未登录时显示提示信息的布局
    private RelativeLayout navigationUnLoginLayout;
    
    // 登陆后在头像下方显示的用户名
    private TextView navigationUserNameText;

    //存放4个fragment
    List<Fragment> fragmentList = new ArrayList<>();

    //fragment适配器
    public FragmentPagerAdapter mPagerAdapter;
    
    // 底部导航栏
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    //左侧导航
    @BindView(R.id.bottom_navigation)
    public BottomNavigationView bottomNavigationView;

    // 绑定小熊猫
    @BindView(R.id.xiaoxiongmao)
    public ImageView xiaoxiongmao;

    // 用户头像
    ImageView navigationHeaderIconView;
    
    // 名人名言
    TextBannerView mingyanTextbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplication();
        ButterKnife.bind(this);

        // 绑定控件
        initView();
    }

    private void initView() {
        // 初始化左侧弹框导航栏
        initNavigation();
        // 底部4个fragment相关
        initFragment();
        // 初始化底部导航栏
        initBottomNavigation();
        // 订阅登录信息
        observeLogin();
        // 自动登录
        autoLogin();
//        小熊猫
        initXiaoxiongmao();
    }


    /**
     * 初始化导航栏
     */
    private void initNavigation () {
        navigationView.setNavigationItemSelectedListener(item -> {
            // navigation view 点击事件
            int id = item.getItemId();
            if (id == R.id.course) {
                UIUtils.gotoActivity(mContext, UserDetailActivity.class);
            }else if (id == R.id.orders) {
                UIUtils.gotoActivity(mContext, PayOrderActivity.class);
            }else if (id == R.id.shoppingCart) {
                UIUtils.gotoActivity(mContext, ShoppingCartActivity.class);
            }
            else if (id == R.id.login) {
//            //intent跳转
//            if (LoginCheckUtil.isLogin(this)) {
//                Toast.makeText(this, "您已登录过了，请先注销", Toast.LENGTH_SHORT).show();
//            } else {
//                UIUtils.gotoActivity(this, LoginActivity.class);
//            }
                UIUtils.gotoActivity(mContext, LoginActivity.class);
            }else if (id == R.id.regist) {
                //intent跳转
//            if (LoginCheckUtil.isLogin(this)) {
//                Toast.makeText(this, "您已登录过了，请先注销", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
//                startActivity(intent);
//            }
                UIUtils.gotoActivity(mContext, RegistActivity.class);
            }else if (id == R.id.logout) {
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
                UserService.logout(mContext);
            }else if (id == R.id.share) {
                CommonUtil.send(mContext, Constants.SHARE_APK_INFO);
            }

            // 抽屉关闭
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        View headerView = navigationView.getHeaderView(0);
        // navigation 未登录布局
        navigationUnLoginLayout = headerView.findViewById(R.id.navigationUnLoginLayout);
        // navigation 已登录布局
        navigationLoginLayout = headerView.findViewById(R.id.navigationLoginLayout);
        // 用户名和头像
        navigationUserNameText = headerView.findViewById(R.id.navigationUserNameText);
        navigationHeaderIconView = headerView.findViewById(R.id.navigationHeaderIconView);
        // 初始化名人名言
        initMingYan(headerView);
    }
    

    /**
     * 初始化4个主菜单
     */
    private void initFragment() {
        //底部导航栏有几项就有几个Fragment
        fragmentList = new ArrayList<>(4);
        // 创建 3 个片段
        fragmentList.add(new HomeFragment(() -> {
            drawer.openDrawer(navigationView);
        }));
        fragmentList.add(new ClassifyFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new MineFragment());
    }


    /**
     * 初始化底部导航栏
     */
    private void initBottomNavigation() {
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT 参考文章 https://www.jianshu.com/p/94515681e335
        //设置适配器用于装载Fragment,ViewPager的好朋友
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);  //得到Fragment
            }

            @Override
            public int getCount() {
                return fragmentList.size();  //得到数量
            }
        };
        //设置导航切换监听
        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
        // 默认场景下超过三个图标只显示选中图标的文字，此处是所有文字都显示
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        /**
         * ViewPager的监听
         */
        setViewPagerListener();

        viewPager.setAdapter(mPagerAdapter);   //设置适配器
        viewPager.setOffscreenPageLimit(4); //预加载所有页
    }


    /**
     * 初始化登录信息
     */
    private void observeLogin () {
        if (LoginUtil.checkHasLogin(this)) {
            initLoginView(LoginUtil.getHeaderIcon(this), LoginUtil.getLoginNickName(this));
        } else {
            LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observeSticky(this, loginUserResponse -> {
                if (loginUserResponse != null){
                    if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                        // 1、--------- 左侧 navigationView 显示登录信息
                        initLoginView(loginUserResponse.getHeaderIcon(),
                                StringUtilEx.getFirstNotEmptyStr(loginUserResponse.getNickName(), loginUserResponse.getUserName()));
                    } else {
                        // 1、--------- 左侧 navigationView 显示登录信息
                        // 登录失败
                        navigationUnLoginLayout.setVisibility(View.VISIBLE);
                        navigationLoginLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }


    /**
     * 自动登录
     */
    private void autoLogin () {
        // 没有 tokenString 代表没有登录过,登录过期也需要重新登录
        if (StringUtils.isEmpty(LoginUtil.getTokenString(this)) || LoginUtil.checkHasExpired(this)) {
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

    /**
     * 初始化小熊猫
     */
    private void initXiaoxiongmao() {
        BottomQuickWindow bottomQuickWindow = new BottomQuickWindow(MainActivity.this);
        xiaoxiongmao.setOnClickListener(v -> {
            // 显示 popupwindow
            bottomQuickWindow.show();
        });
    }

    //4个fragment 的对应跳转
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_home: {
                    viewPager.setCurrentItem(0);
                    return true;
                }
                case R.id.action_classfiy: {
                    viewPager.setCurrentItem(1);
                    return true;
                }
                case R.id.action_find: {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                case R.id.action_mine: {
                    viewPager.setCurrentItem(3);
                    return true;
                }
            }
            return false;
        }
    };


    /**
     * 初始化名人名言
     * @param headerView
     */
    private void initMingYan(View headerView) {
        // 名人名言
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
    

    //这里有3中滑动过程,我们用点击后就可以
    private void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //滑动页面后做的事，这里与BottomNavigationView结合，使其与正确page对应
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 初始化登录视图
     * @param headerIcon
     * @param userName
     */
    private void initLoginView(String headerIcon, String userName) {
        // 登录成功，显示登录布局
        navigationLoginLayout.setVisibility(View.VISIBLE);
        navigationUnLoginLayout.setVisibility(View.GONE);
        // 设置登录信息
        // 异步加载图片,使用 Glide 第三方库
        Glide.with(this)
                .load(UIUtils.replaceMediaUrl(headerIcon))
                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                .into(navigationHeaderIconView);
        // 设置昵称或者用户名
        navigationUserNameText.setText(userName);
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

    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    
}
