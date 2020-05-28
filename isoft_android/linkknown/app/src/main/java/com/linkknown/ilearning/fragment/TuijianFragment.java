package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.adapter.HomePageGridViewAdapter;
import com.linkknown.ilearning.adapter.HomePageViewPagerAdapter;
import com.linkknown.ilearning.model.Catalog;
import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TuijianFragment extends BaseLazyLoadFragment implements OnBannerListener {

    private Context mContext;

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    // 轮播图部分
    @BindView(R.id.banner)
    public Banner banner;
    // 存放轮播图所有图片
    private ArrayList<Integer> bannerImageList;

    // GridView 作为一个 View 对象添加到 ViewPager 集合中
    private List<View> viewPagerList;

    // 下拉刷新的layout
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    @Override
    protected void initView(View mRootView) {
        mContext = getContext();
        ButterKnife.bind(this, mRootView);

        //加载轮播图
        initBannerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initData();


        refreshLayout.setOnRefreshListener(() -> ToastUtil.showText(mContext, "onRefresh..."));
    }

    @Override
    public void initData () {
        //判断网络状态，并初始化数据
        if (!NetUtil.isNetworkAvailable(this.getContext())) {
            ToastUtil.showText(mContext, "亲，网络异常，无法获取数据请检查网络！");
            return;
        }
        // 初始化热门课程分类
        initHotCategorys();

//            //获取热评商品
//            initCommodityList(Constants.HOME_PAGE_LIMIT, 0, true);
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
        bannerImageList.add(R.drawable.banner_coupon);
        bannerImageList.add(R.drawable.banner_coupon);
        bannerImageList.add(R.drawable.banner_coupon);

        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果
        banner.setBannerAnimation(Transformer.Accordion);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);

        banner.setImages(bannerImageList)
                //轮播图的监听
                .setOnBannerListener(this).start();
    }

    private void initHotCategorys () {
        List<Catalog> listData = new ArrayList<>();
        for (int i=0; i<30; i++) {
            Catalog catalog = new Catalog();
            catalog.setCatalogName("默认分类1");
            catalog.setCatalogIcon("https://img-blog.csdn.net/20180420104431654");
            listData.add(catalog);
        }
        initHotCategorysViewPage(listData);
    }

    private void initHotCategorysViewPage (List<Catalog> listData) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        int totalPage = (int) Math.ceil(listData.size() * 1.0 / Constants.DEFAULT_PAGE_SIZE);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, viewPager, false);
            gridView.setAdapter(new HomePageGridViewAdapter(this.getContext(), listData, i, Constants.DEFAULT_PAGE_SIZE));
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        viewPager.setAdapter(new HomePageViewPagerAdapter(viewPagerList));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void OnBannerClick(int position) {
        ToastUtil.showText(mContext, "点击了图片");
    }
}
