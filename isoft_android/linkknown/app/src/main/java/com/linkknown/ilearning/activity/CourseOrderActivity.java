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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseOrderActivity extends BaseActivity {

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
        setContentView(R.layout.activity_course_order);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }


    private void initView(){
        initToolBar(toolbar, true, "我的订单");
        mFragments.add(new PayOrderFragment());
        mFragments.add(new PayOrderFragment());
        titles.add("待付款");
        titles.add("历史订单");
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
