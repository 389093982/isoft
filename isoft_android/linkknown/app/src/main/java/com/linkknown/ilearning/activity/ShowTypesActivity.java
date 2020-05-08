package com.linkknown.ilearning.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.CourseClassifyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTypesActivity extends AppCompatActivity {

    @BindView(R.id.headerToolBarLayout)
    public LinearLayout headerToolBarLayout;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private List<Fragment> mFragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_types);

        ButterKnife.bind(this);

        init();
    }

    private void init () {
        initFragment();
    }

    private void initFragment () {
        // 创建 fragment
        CourseClassifyFragment showTypeDetailFragment1 = new CourseClassifyFragment();
        CourseClassifyFragment showTypeDetailFragment2 = new CourseClassifyFragment();
        CourseClassifyFragment showTypeDetailFragment3 = new CourseClassifyFragment();
        CourseClassifyFragment showTypeDetailFragment4 = new CourseClassifyFragment();
        CourseClassifyFragment showTypeDetailFragment5 = new CourseClassifyFragment();

        mFragments.add(showTypeDetailFragment1);
        mFragments.add(showTypeDetailFragment2);
        mFragments.add(showTypeDetailFragment3);
        mFragments.add(showTypeDetailFragment4);
        mFragments.add(showTypeDetailFragment5);

        // title 限制 2 个字
        titles.add("测试1");
        titles.add("测试1");
        titles.add("测试1");
        titles.add("测试1");
        titles.add("测试1");

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
