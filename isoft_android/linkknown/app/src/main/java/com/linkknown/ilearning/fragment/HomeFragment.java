package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseListActivity;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.adapter.HomePageGridViewAdapter;
import com.linkknown.ilearning.adapter.HomePageViewPagerAdapter;
import com.linkknown.ilearning.model.Catalog;
import com.linkknown.ilearning.util.NetUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    // 轮播图部分
    @BindView(R.id.banner)
    public Banner banner;
    // 存放轮播图所有图片
    private ArrayList<String> bannerImageList = new ArrayList<>();

    // GridView 作为一个 View 对象添加到 ViewPager 集合中
    private List<View> viewPagerList;

    // GridView小圆点指示器
    @BindView(R.id.points)
    public ViewGroup points;
    // 小圆点指示器图片集合
    private ImageView[] ivPoints;


    private View rootView;

    // 下拉刷新的layout
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initData();


        refreshLayout.setOnRefreshListener(() -> ToastUtil.showText(mContext, "onRefresh..."));
    }

    private void initData () {
        //判断网络状态，并初始化数据
        if (!NetUtil.isNetworkAvailable(this.getContext())) {
            ToastUtil.showText(mContext, "亲，网络异常，无法获取数据请检查网络！");
            return;
        }
        // 初始化热门课程分类
        initHotCategorys();

        //加载轮播图
        initBanner();

//            //获取热评商品
//            initCommodityList(Constants.HOME_PAGE_LIMIT, 0, true);
    }

    /**
     * 初始化广告页
     */
    private void initBanner() {
        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        //清空旧数据
        bannerImageList.clear();
        bannerImageList.add("https://img-blog.csdn.net/20180420104431654");
        bannerImageList.add("https://img-blog.csdn.net/20180420104431654");
        bannerImageList.add("https://img-blog.csdn.net/20180420104431654");
        bannerImageList.add("https://img-blog.csdn.net/20180420104431654");
        banner.setImages(bannerImageList);
        banner.start();
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
                //改变小圆圈指示器的切换效果
                setImageBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 小圆点指示器
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < ivPoints.length; i++) {
            ImageView imageView = new ImageView(getContext());
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page__selected_indicator);
            } else {
                imageView.setBackgroundResource(R.drawable.page__normal_indicator);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;//设置点点点view的左边距
            layoutParams.rightMargin = 10;//设置点点点view的右边距
            points.addView(imageView, layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /**
     * 设置GridView图片背景
     *
     * @param selectItems 选中项index
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.drawable.page__selected_indicator);
            } else {
                ivPoints[i].setBackgroundResource(R.drawable.page__normal_indicator);
            }
        }
    }
}
