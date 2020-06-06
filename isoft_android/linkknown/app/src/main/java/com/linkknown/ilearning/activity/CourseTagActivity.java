package com.linkknown.ilearning.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.fragment.CourseTagFragment;
import com.linkknown.ilearning.service.CourseClassifyService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        // 注册activity在post事件 之后  会导致无法接收
        init();

        initData();
    }

    private void initData () {
        // 加载课程一级分类
        CourseClassifyService.loadCourseFirstClassify();
    }

    private void init () {
        initFragment();
    }

    private void initFragment () {
        // 根据一分分类创建 fragment
        LiveEventBus.get("courseFirstClassify", CourseClassifyService.CourseFirstClassify.class)
                // 支持在注册订阅者的时候设置Sticky模式,这样订阅者可以接收到订阅之前发送的消息
                .observeSticky(this, new Observer<CourseClassifyService.CourseFirstClassify>() {
            @Override
            public void onChanged(CourseClassifyService.CourseFirstClassify courseFirstClassify) {
                List<Fragment> mFragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();

                for (String classifyName : courseFirstClassify.classifyNames){
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
        });
    }
}
