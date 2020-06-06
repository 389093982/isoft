package com.linkknown.ilearning.common;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

//  ViewPager 指示器
public class CommonNavigatorCreater {

	public static CommonNavigator setDefaultNavigator(Context context, final List<String> titles, final ViewPager viewPager) {

		CommonNavigator commonNavigator = new CommonNavigator(context);

		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return titles.size();
			}

			/**
			 * 标题布局
			 * @param context
			 * @param index
			 * @return
			 */
			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 0, 0, 10);
				ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
				colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
				colorTransitionPagerTitleView.setSelectedColor(Color.RED);
				colorTransitionPagerTitleView.setTextSize(14);
				colorTransitionPagerTitleView.setLayoutParams(params);
				colorTransitionPagerTitleView.setText(titles.get(index));
				colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
				return colorTransitionPagerTitleView;
			}

			/**
			 * 指示器样式
			 * @param context
			 * @return
			 */
			@Override
			public IPagerIndicator getIndicator(Context context) {
				LinePagerIndicator indicator = new LinePagerIndicator(context);
				indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
				indicator.setColors(Color.RED);
				return indicator;
			}

			@Override
			public float getTitleWeight(Context context, int index) {
				return 1.0f;
			}
		});
		return commonNavigator;
	}


}
