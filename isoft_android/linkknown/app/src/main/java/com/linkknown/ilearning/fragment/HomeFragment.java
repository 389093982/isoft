package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.linkknown.ilearning.activity.MessageInfoActivity;
import com.linkknown.ilearning.activity.NewChannelActivity;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.AnimationUtil;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.superluo.textbannerlibrary.TextBannerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseLazyLoadFragment {

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

    // 添加更多频道的图标
    @BindView(R.id.add_channel)
    ImageView addChannel;

    // 名人名言
    @BindView(R.id.mingyanTextbanner)
    TextBannerView mingyanTextbanner;

    // 铃铛(消息)
    @BindView(R.id.lingdang)
    ImageView lingdang;

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
        mContext = getContext();
        // 初始化 fragment
        initFragment();
        // 初始化名人名言
        initMingYan();
        // 初始化铃铛(消息)
        initLingDang();
        // 订阅登录信息,然后显示用户名和头像
        observeLogin();
    }

    private void initLingDang () {
        lingdang.setAnimation(AnimationUtil.getShakeAnimation(3));
        lingdang.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MessageInfoActivity.class));
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

        titles.add("全部");
        CourseFilterFragment courseFilterFragment3 = new CourseFilterFragment();
        courseFilterFragment3.setArguments(CommonUtil.createBundle2("search","","isCharge", ""));
        mFragments.add(courseFilterFragment3);

        titles.add("免费");
        CourseFilterFragment courseFilterFragment1 = new CourseFilterFragment();
        courseFilterFragment1.setArguments(CommonUtil.createBundle2("search","","isCharge", "free"));
        mFragments.add(courseFilterFragment1);

        titles.add("付费");
        CourseFilterFragment courseFilterFragment2 = new CourseFilterFragment();
        courseFilterFragment2.setArguments(CommonUtil.createBundle2("search","","isCharge", "charge"));
        mFragments.add(courseFilterFragment2);

        titles.add("github专栏");
        CourseFilterFragment courseFilterFragment4 = new CourseFilterFragment();
        courseFilterFragment4.setArguments(CommonUtil.createBundle2("search","github","isCharge", ""));
        mFragments.add(courseFilterFragment4);

//        titles.add("关注");
//        MoreFragment moreFragment5 = new MoreFragment();
//        mFragments.add(moreFragment5);

        titles.add("推荐");
        MoreFragment moreFragment6 = new MoreFragment();
        mFragments.add(moreFragment6);

        titles.add("发现");
        FindFragment findFragment = new FindFragment();
        mFragments.add(findFragment);

        titles.add("更多");
        MoreFragment moreFragment8 = new MoreFragment();
        mFragments.add(moreFragment8);


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
        return R.layout.fragment_home;
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
        if (LoginUtil.checkHasLogin(mContext)) {
            initLoginView(LoginUtil.getHeaderIcon(mContext), LoginUtil.getLoginNickName(mContext));
        } else {

        }
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observeSticky(this, loginUserResponse -> {
            if (loginUserResponse != null){
                if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                    initLoginView(loginUserResponse.getHeaderIcon(), StringUtilEx.getFirstNotEmptyStr(loginUserResponse.getNickName(), loginUserResponse.getUserName()));
                } else {
                    // 顶部 toolbar 显示登录信息
                    toolBarLoginLayout.setVisibility(View.VISIBLE);
                    toolBarUnLoginLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initLoginView(String headerIcon, String userName) {
        // 2、--------- 顶部 toolbar 显示登录信息
        toolBarLoginLayout.setVisibility(View.VISIBLE);
        toolBarUnLoginLayout.setVisibility(View.GONE);

        // 设置昵称或者用户名
        Glide.with(this)
                .load(UIUtils.replaceMediaUrl(headerIcon))
                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                .into(person_head);
        userNameText.setText(userName);

    }
}
