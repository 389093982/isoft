package com.linkknown.ilearning.activity;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.SpaceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    // 存储首页对应的三个片段
    private List<Fragment> mFragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        init();
    }

    private void init () {
        initFragment();
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
}
