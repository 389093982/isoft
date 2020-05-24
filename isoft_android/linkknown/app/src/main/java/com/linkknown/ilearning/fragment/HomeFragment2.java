package com.linkknown.ilearning.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.NewChannelActivity;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.superluo.textbannerlibrary.TextBannerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment2 extends BaseLazyLoadFragment {
    // 存储所有 fragment
    private List<Fragment> mFragments = new ArrayList<>();
    // 存储 fragment 的标题
    private List<String> titles = new ArrayList<>();
    // tabLayout 和 viewPager 用于切换 fragment
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 添加更多频道的图标
    @BindView(R.id.add_channel)
    ImageView addChannel;

    // 名人名言
    @BindView(R.id.mingyanTextbanner)
    TextBannerView mingyanTextbanner;

    // 工具栏中的用户布局
    @BindView(R.id.toolBarLoginLayout)
    public LinearLayout toolBarLoginLayout;
    @BindView(R.id.toolBarUnLoginLayout)
    public LinearLayout toolBarUnLoginLayout;
    // 用户布局中的用户名和头像
    @BindView(R.id.person_head)
    public ImageView person_head;
    @BindView(R.id.userNameText)
    public TextView userNameText;

    @Override
    protected void initView(View mRootView) {
        ButterKnife.bind(this, mRootView);
        // 初始化 fragment
        initFragment();
        // 初始化名人名言
        initMingYan();
        // 订阅登录信息,然后显示用户名和头像
        observeLogin();
    }

    private void initMingYan() {
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

    private void initFragment () {
        addChannel.setOnClickListener(v -> UIUtils.gotoActivity(getContext(), NewChannelActivity.class));

        // 创建 fragment
        MoreFragment moreFragment1 = new MoreFragment();
        CourseFilterFragment courseFilterFragment1 = new CourseFilterFragment();
        CourseFilterFragment courseFilterFragment2 = new CourseFilterFragment();
        CourseFilterFragment courseFilterFragment3 = new CourseFilterFragment();
        CourseFilterFragment courseFilterFragment4 = new CourseFilterFragment();
        // activity 向 fragment 传参
        courseFilterFragment1.setArguments(CommonUtil.createBundle2("search","","isCharge", "free"));
        courseFilterFragment2.setArguments(CommonUtil.createBundle2("search","","isCharge", "charge"));
        courseFilterFragment3.setArguments(CommonUtil.createBundle2("search","","isCharge", ""));
        courseFilterFragment4.setArguments(CommonUtil.createBundle2("search","github","isCharge", ""));

//        MoreFragment moreFragment5 = new MoreFragment();
        MoreFragment moreFragment6 = new MoreFragment();
        FindFragment findFragment = new FindFragment();
        MoreFragment moreFragment8 = new MoreFragment();

        mFragments.add(moreFragment1);
        mFragments.add(courseFilterFragment1);
        mFragments.add(courseFilterFragment2);
        mFragments.add(courseFilterFragment3);
        mFragments.add(courseFilterFragment4);
//        mFragments.add(moreFragment5);
        mFragments.add(moreFragment6);
        mFragments.add(findFragment);
        mFragments.add(moreFragment8);

        // title 限制 2 个字
        titles.add("首页");
        titles.add("免费");
        titles.add("付费");
        titles.add("全部");
        titles.add("github专栏");
//        titles.add("关注");
        titles.add("推荐");
        titles.add("发现");
        titles.add("更多");

        viewPager.setAdapter(new FragmentStatePagerAdapter(this.getChildFragmentManager()) {
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

    @Override
    protected void initData() {

    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_home2;
    }

    @Override
    public void onResume() {
        super.onResume();
        // onResume()和onStop()方法分别调用startViewAnimator()和stopViewAnimator()，防止返回页面出现文字重影问题
        mingyanTextbanner.startViewAnimator();
    }

    @Override
    public void onStop() {
        super.onStop();
        mingyanTextbanner.stopViewAnimator();
    }

    private void observeLogin () {
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observeSticky(this, loginUserResponse -> {
            if (loginUserResponse != null){
                if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                    // 2、--------- 顶部 toolbar 显示登录信息
                    toolBarLoginLayout.setVisibility(View.VISIBLE);
                    toolBarUnLoginLayout.setVisibility(View.GONE);

                    // 设置昵称或者用户名
                    userNameText.setText(StringUtilEx.getFirstNotEmptyStr(loginUserResponse.getNickName(), loginUserResponse.getUserName()));
                    Glide.with(this)
                            .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                            .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                            .into(person_head);
                } else {
                    // 顶部 toolbar 显示登录信息
                    toolBarLoginLayout.setVisibility(View.VISIBLE);
                    toolBarUnLoginLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}
