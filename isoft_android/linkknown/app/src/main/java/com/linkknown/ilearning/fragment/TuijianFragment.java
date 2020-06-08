package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CouponReceiveCenterActivity;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TuijianFragment extends BaseLazyLoadFragment {

    private Context mContext;

    // 轮播图部分
    @BindView(R.id.banner)
    public Banner banner;
    // 存放轮播图所有图片
    private ArrayList<Integer> bannerImageList;

    @Override
    protected void initView(View mRootView) {
        mContext = getContext();
        ButterKnife.bind(this, mRootView);

        //加载轮播图
        initBannerView();

        initCustomTagCourseView();
    }

    @Override
    public void initData () {
        //判断网络状态，并初始化数据
        if (!NetUtil.isNetworkAvailable(this.getContext())) {
            ToastUtil.showText(mContext, "亲，网络异常，无法获取数据请检查网络！");
            return;
        }
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_tuijian;
    }

    /**
     * 初始化广告页
     */
    private void initBannerView() {
        bannerImageList = new ArrayList<>();
        bannerImageList.add(R.drawable.banner_coupon);
        bannerImageList.add(R.drawable.banner_huodong);

        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //轮播图的监听
        banner.setOnBannerListener(position -> {
            switch (position) {
                case 0:
                    UIUtils.gotoActivity(mContext, CouponReceiveCenterActivity.class);
                    break;
                case 1:
                    UIUtils.gotoActivity(mContext, HuodongActivity.class);
                    break;
                default:
                    break;
            }
        });
        banner.setImages(bannerImageList).start();
    }

    private void initCustomTagCourseView() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        CourseCustomTagFragment customTagFragment = new CourseCustomTagFragment();
        customTagFragment.setArguments(CommonUtil.createBundle("custom_tag", "hot"));
        transaction.replace(R.id.courseCustomTagHotLayout, customTagFragment);

        CourseCustomTagFragment customTagFragment2 = new CourseCustomTagFragment();
        customTagFragment2.setArguments(CommonUtil.createBundle("custom_tag", "recommend"));
        transaction.replace(R.id.courseCustomTagHotLayout2, customTagFragment2);

        CourseCustomTagFragment customTagFragment3 = new CourseCustomTagFragment();
        customTagFragment3.setArguments(CommonUtil.createBundle("custom_tag", "views"));
        transaction.replace(R.id.courseCustomTagHotLayout3, customTagFragment3);

        transaction.commit();
    }
}
