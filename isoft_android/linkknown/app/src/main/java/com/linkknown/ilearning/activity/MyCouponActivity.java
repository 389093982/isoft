package com.linkknown.ilearning.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.MyCouponFragment;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCouponActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Context mContext;

    // 存储所有 fragment
    private List<Fragment> mFragments = new ArrayList<>();

    // 存储 fragment 的标题
    private List<String> titles = new ArrayList<>();

    // tabLayout 和 viewPager 用于切换 fragment
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.receiveCouponLayout)
    LinearLayout receiveCouponLayout;
    @BindView(R.id.receiveCouponText)
    TextView receiveCouponText;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_coupon);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "优惠券");

        initFragment();

        initReceiveCouponView();
    }

    private void initReceiveCouponView() {
        new Handler().postDelayed(() -> {
            receiveCouponLayout.setOnClickListener(v -> UIUtils.gotoActivity(mContext, ReceiveCouponCenterActivity.class));
            int width = receiveCouponText.getWidth();
            // 平移 translation
            ObjectAnimator animator = ObjectAnimator.ofFloat(receiveCouponLayout, "translationX", 10, width + 10, 10).setDuration(5000);
            animator.setRepeatCount(-1);
            animator.start();
        }, 2000);
    }

    private void initFragment() {
        titles.add("已领取");
        MyCouponFragment myCouponFragment = new MyCouponFragment();
        myCouponFragment.setArguments(CommonUtil.createBundle2("isExpired", "false", "isUsed", "false"));
        mFragments.add(myCouponFragment);

        titles.add("已使用");
        myCouponFragment = new MyCouponFragment();
        myCouponFragment.setArguments(CommonUtil.createBundle2("isExpired", "", "isUsed", "true"));
        mFragments.add(myCouponFragment);

        titles.add("已过期");
        myCouponFragment = new MyCouponFragment();
        myCouponFragment.setArguments(CommonUtil.createBundle2("isExpired", "true", "isUsed", "false"));
        mFragments.add(myCouponFragment);

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


    //添加右上角 领券中心 图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.coupon_center, menu);
        return true;
    }

    //跳转到领券中心
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.coupon_center) {
            UIUtils.gotoActivity(mContext, ReceiveCouponCenterActivity.class);
        }
        return true;
    }

}
