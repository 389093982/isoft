package com.linkknown.ilearning.activity;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.event.AppBarStateChangeEvent;
import com.linkknown.ilearning.fragment.SpaceFragment;
import com.linkknown.ilearning.util.DisplayUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mSlidingTabLayout;

    // 顶部导航栏布局
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_player)
    TextView mTvPlayer;
    @BindView(R.id.tv_av)
    TextView mAvText;

    // 悬浮按钮
    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // 初始化
        ButterKnife.bind(this);

        initViewPager();

        init();
    }

    private void initToolBar() {
        toolbar.setTitle("");
        // 支持 ActionBar,方便在上面设置 menu
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            // 给左上角图标的左边加上一个返回的图标
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // 希望点击图标左侧箭头返回上一页，需要加载选项菜单后，对于菜单项的点击事件调用如下方法
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_test) {
            ToastUtil.showText(getApplicationContext(), "点击了测试");
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        initToolBar();

        mTvPlayer.setText("mTvPlayer");
        mAvText.setText("mAvText");

        // 在 textView 左侧添加播放图标
        UIUtils.setTextViewDrawbleImg(getApplicationContext(), mTvPlayer, R.drawable.ic_fab_play, 0,0,60,64);

        // addOnOffsetChangedListener: 当 AppBarLayout 垂直方向上的偏移量发生改变时，为触发一个回调方法定义的接口
        //   // 导航栏布局垂直移动时悬浮按钮跟着变化
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> setFloatingActionButtonStatus(verticalOffset));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeEvent() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);

                    /**
                     * contentInsetLeft、contentInsetRight、contentInsetStart、contentInsetEnd：Toolbar的左右两侧都是默认有16dp的padding的，
                     *      如果你需要让Toolbar上的内容与左右两侧的距离有变化，便可以通过以上四个属性来进行相应的设置。
                     * 比如要让内容紧贴左侧或起始侧便可以将contentInsetLeft或contentInsetStart设为0。
                     * 对应方法：
                     * setContentInsetsRelative(int,int)——对应start和end
                     * setContentInsetsAbsolute(int,int)——对应left和right
                     */
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 15), 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mTvPlayer.setVisibility(View.VISIBLE);

                    mAvText.setVisibility(View.GONE);
                    // 或者使用  mTvPlayer.setTranslationX(100) 实现
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 150), 0);
                } else {
                    mTvPlayer.setVisibility(View.GONE);
                    mAvText.setVisibility(View.VISIBLE);
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 15), 0);
                }
            }
        });
    }

    // 导航栏布局垂直移动时悬浮按钮跟着变化
    private void setFloatingActionButtonStatus(int verticalOffset) {
        // 设置垂直位移和导航栏一样，Y 轴对齐
        mFAB.setTranslationY(verticalOffset);
        if (verticalOffset == 0) {          // 标识向下滚动到最底部
            showFloatingActionButton();
        } else if (verticalOffset < 0) {    // 标识向下滚动
            hideFloatingActionButton();
        }
    }

    // 缩放显示
    private void showFloatingActionButton() {
        mFAB.animate().scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();
        mFAB.setClickable(true);
    }

    // 缩放隐藏
    private void hideFloatingActionButton() {
        mFAB.animate().scaleX(0f).scaleY(0f).setInterpolator(new AccelerateInterpolator()).start();
        mFAB.setClickable(false);
    }

    private void initViewPager() {
        SpaceFragment spaceFragment1 = new SpaceFragment();
        SpaceFragment spaceFragment2 = new SpaceFragment();
        fragments.add(spaceFragment1);
        fragments.add(spaceFragment2);
        titles.add("简介");
        titles.add("评论" + "(8888888888)");
        VideoDetailsPagerAdapter mAdapter = new VideoDetailsPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mSlidingTabLayout.setViewPager(mViewPager);
        measureTabLayoutTextWidth(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                measureTabLayoutTextWidth(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void measureTabLayoutTextWidth(int position) {
        String title = titles.get(position);
        TextView titleView = mSlidingTabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        mSlidingTabLayout.setIndicatorWidth(textWidth / 3);
    }

    /**
     * onCreateOptionsMenu() 创建选项菜单 Menu
     * 1.选项菜单（optinosMenu）
     * 2.上下文菜单（ContextMenu）
     * 3.子菜单(subMenu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 如果当前Activity是没有主题的，即用的主题是：Theme.AppCompat.Light.NoActionBar
        //则需要自己先添加一个toolbar并通过setSupportActionBar方法将其设置成ActionBar
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    public static class VideoDetailsPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;

        VideoDetailsPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
