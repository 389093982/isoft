package com.linkknown.ilearning.viewpage;

// 首页 ViewPager

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

// ViewPager(视图滑动切换工具)
// 而ViewPager则有一个特定的 Adapter —— PagerAdapter
// 建议我们使用Fragment来填充ViewPager的，这样 可以更加方便的生成每个Page，以及管理每个Page的生命周期
public class MainActivityViewPager extends ViewPager {
    public MainActivityViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
