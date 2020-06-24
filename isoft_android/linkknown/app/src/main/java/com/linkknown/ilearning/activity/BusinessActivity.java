package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.linkknown.ilearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessActivity extends BaseActivity {

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private Context mContext;

    // 顶部 appBar
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    // appbar 包裹的 toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // toolbar 上面的标题
    @BindView(R.id.toolbarText)
    TextView toolbarText;
    @BindView(R.id.linkknownInfoLayout)
    LinearLayout linkknownInfoLayout;
    @BindView(R.id.linkknownInfoFrame)
    FrameLayout linkknownInfoFrame; // Title的FrameLayout

    @BindView(R.id.logoImageView)
    ImageView logoImageView; // 大图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolBar();

        // appbar 上下滚动监听
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int maxScroll = appBarLayout.getTotalScrollRange();
            float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
            handleAlphaOnTitle(percentage);
            handleToolbarTitleVisibility(percentage);
        });

        initParallaxValues(); // 自动滑动效果
    }

    // 设置自动滑动的动画效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) logoImageView.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) linkknownInfoFrame.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        logoImageView.setLayoutParams(petDetailsLp);
        linkknownInfoFrame.setLayoutParams(petBackgroundLp);
    }


    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linkknownInfoLayout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//                startAlphaAnimation(civ, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//                startAlphaAnimation(lvMenu, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linkknownInfoLayout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//                startAlphaAnimation(civ, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//                startAlphaAnimation(lvMenu, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 处理 ToolBar 的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbarText, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarText, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
