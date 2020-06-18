package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KaoShiActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 轮播图
    @BindView(R.id.banner)
    Banner banner;
    // 去考试按钮
    @BindView(R.id.toKaoshiView)
    TextView toKaoshiView;
    // 查看考试结果按钮
    @BindView(R.id.kaoshiResultView)
    TextView kaoshiResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_shi);

        mContext = this;
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initToolBar(toolbar, true, "测试一下你的学习成果吧！");
        // 初始化轮播图
        initBannerView();
        // 初始化菜单按钮
        initMenuButton();
    }

    private void initMenuButton() {
        toKaoshiView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, KaoShiTimuClassifyListActivity.class));

        kaoshiResultView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, KaoShiResultListActivity.class));
    }

    // 初始化顶部轮播图组件
    private void initBannerView() {
        banner.clearAnimation();
        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(3000);

        // 存放轮播图所有图片
        ArrayList<Integer> bannerImageList = new ArrayList<>();
        ArrayList<String> bannerTitleList = new ArrayList<>();
        //清空旧数据
        bannerImageList.clear();
        bannerTitleList.clear();
        bannerImageList.add(R.drawable.banner_biancheng);
        bannerImageList.add(R.drawable.banner_shaoer);
        bannerTitleList.add("编程思维");
        bannerTitleList.add("少儿编程");
        banner.setImages(bannerImageList);
        banner.setBannerTitles(bannerTitleList);
        banner.start();
    }
}
