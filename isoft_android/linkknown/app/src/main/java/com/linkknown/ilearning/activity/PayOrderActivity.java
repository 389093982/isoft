package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.PayOrderFragment;
import com.linkknown.ilearning.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayOrderActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    // 存储 fragment 的标题
    private List<String> titles = new ArrayList<>();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView(){
        initToolBar(toolbar, true, "我的订单");

        titles.add("全部");
        Fragment payrderFragment = new PayOrderFragment();
        payrderFragment.setArguments(CommonUtil.createBundle("scope","ALL"));
        mFragments.add(payrderFragment);

        titles.add("待付款");
        payrderFragment = new PayOrderFragment();
        payrderFragment.setArguments(CommonUtil.createBundle("scope","WAIT_FOR_PAY"));
        mFragments.add(payrderFragment);

        titles.add("已付款");
        payrderFragment = new PayOrderFragment();
        payrderFragment.setArguments(CommonUtil.createBundle("scope","PAID"));
        mFragments.add(payrderFragment);

        titles.add("已取消");
        payrderFragment = new PayOrderFragment();
        payrderFragment.setArguments(CommonUtil.createBundle("scope","CANCELLED"));
        mFragments.add(payrderFragment);

        titles.add("失败");
        payrderFragment = new PayOrderFragment();
        payrderFragment.setArguments(CommonUtil.createBundle("scope","FAIL"));
        mFragments.add(payrderFragment);

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
