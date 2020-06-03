package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.CouponCenterFragment;
import com.linkknown.ilearning.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponCenterActivity extends BaseActivity {

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
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tab_fragment_toolbar);
        ButterKnife.bind(this);
        mContext = this;

        initView();
    }

    private void initView () {
        initToolBar(toolbar, true, "优惠券");
        // 创建 fragment
        CouponCenterFragment couponCenterFragment1 = new CouponCenterFragment();
        CouponCenterFragment couponCenterFragment2 = new CouponCenterFragment();
        CouponCenterFragment couponCenterFragment3 = new CouponCenterFragment();

        // activity 向 fragment 传参
        // 已领取
        couponCenterFragment1.setArguments(CommonUtil.createBundle2("isExpired", "false", "isUsed", "false"));
        // 已使用
        couponCenterFragment2.setArguments(CommonUtil.createBundle2("isExpired", "", "isUsed", "true"));
        // 已过期
        couponCenterFragment3.setArguments(CommonUtil.createBundle2("isExpired", "true", "isUsed", "false"));

        mFragments.add(couponCenterFragment1);
        mFragments.add(couponCenterFragment2);
        mFragments.add(couponCenterFragment3);

        titles.add("已领取");
        titles.add("已使用");
        titles.add("已过期");

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
}
