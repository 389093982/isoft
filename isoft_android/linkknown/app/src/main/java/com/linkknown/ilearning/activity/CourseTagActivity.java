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
import com.linkknown.ilearning.fragment.CourseTagFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

// 课程分类页面
public class CourseTagActivity extends AppCompatActivity {
    // 顶部工具栏
    @BindView(R.id.headerToolBarLayout)
    public LinearLayout headerToolBarLayout;
    // tab 标签页
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    // tab 标签页切换的页面
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tag);

        ButterKnife.bind(this);

        // 注册activity在post事件 之后会导致无法接收
        initView();

        initData();
    }

    private void initData () {

    }

    private void initView () {
        initFragment();
    }

    private void initFragment () {
        // 加载课程一级分类
        CourseFirstClassify firstClassify = new CourseFirstClassify();
        firstClassify.classifyNames = Arrays.asList("前端", "后台", "基础", "实践", "名师", "讲坛");

        List<Fragment> mFragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        for (String classifyName : firstClassify.classifyNames){
            // 创建 fragment
            CourseTagFragment fragment = new CourseTagFragment();
            mFragments.add(fragment);
            // title 限制 2 个字，好看点
            titles.add(classifyName);
        }
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

        // tabLayout 设置 viewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    // 课程一级分类
    @Data
    public static class CourseFirstClassify {
        public List<String> classifyNames;
    }
}
