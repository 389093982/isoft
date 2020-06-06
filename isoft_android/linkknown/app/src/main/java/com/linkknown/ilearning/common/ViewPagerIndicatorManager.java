package com.linkknown.ilearning.common;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.ui.UIUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//  ViewPager 指示器管理类
public class ViewPagerIndicatorManager {

	private Context context;
	private MagicIndicator magicIndicator;
	private List<String> titles;
	private ViewPager viewPager;

	/**
	 * 构造器接收所需要的所有参数
	 * @param context
	 * @param magicIndicator
	 * @param titles
	 * @param viewPager
	 */
	public ViewPagerIndicatorManager (Context context, MagicIndicator magicIndicator, final List<String> titles, final ViewPager viewPager) {
		this.context = context;
		this.magicIndicator = magicIndicator;
		this.titles = titles;
		this.viewPager = viewPager;
	}

	@NotNull
	private CommonNavigatorAdapter initAdapter(List<String> titles, ViewPager viewPager) {
		return new CommonNavigatorAdapter() {
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
				ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context) {
					// 复写指示器标题四种状态
					@Override
					public void onSelected(int index, int totalCount) {
						super.onSelected(index, totalCount);
					}

					@Override
					public void onDeselected(int index, int totalCount) {
						super.onDeselected(index, totalCount);
					}

					@Override
					public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
						super.onEnter(index, totalCount, enterPercent, leftToRight);
					}

					@Override
					public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
						super.onLeave(index, totalCount, leavePercent, leftToRight);
					}
				};

				// 设置颜色和选中时的颜色
				colorTransitionPagerTitleView.setNormalColor(UIUtils.getResourceColor(context, R.color.gray1));
				colorTransitionPagerTitleView.setSelectedColor(Color.RED);
				colorTransitionPagerTitleView.setTextSize(14);
				colorTransitionPagerTitleView.setLayoutParams(params);
				colorTransitionPagerTitleView.setText(titles.get(index));
				colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
				return colorTransitionPagerTitleView;
			}

			/**
			 * 指示器样式，返回 null 则不带下划线
			 * @param context
			 * @return
			 */
			@Override
			public IPagerIndicator getIndicator(Context context) {
//				LinePagerIndicator indicator = new LinePagerIndicator(context);
//				indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//				indicator.setColors(Color.RED);
//				return indicator;
				return null;
			}

			@Override
			public float getTitleWeight(Context context, int index) {
				return 1.0f;
			}
		};
	}

	public void registerListener() {
		// 给 viewPager 添加事假监听
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}

			@Override
			public void onPageSelected(int position) {
				magicIndicator.onPageSelected(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				magicIndicator.onPageScrollStateChanged(state);
			}
		});
	}

	/**
	 * 进行绑定的统一入口
	 */
	public void bind() {
		CommonNavigator commonNavigator = new CommonNavigator(context);
		commonNavigator.setAdapter(initAdapter(titles, viewPager));
		magicIndicator.setNavigator(commonNavigator);

		ViewPagerHelper.bind(magicIndicator, viewPager);

		registerListener();
	}
}
