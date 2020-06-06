package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseSearchActivity;
import com.linkknown.ilearning.common.CommonNavigatorCreater;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.popup.SearchHistoryPopupWindow;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ClassifyFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.searchTextView)
    public EditText searchTextView;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;
    @BindView(R.id.searchHistory)
    public TextView searchHistory;

    private Disposable searchTextViewDisposable;
    private int showHintIndex = 0;

    @BindView(R.id.leftClassifyRecyclerView)
    public RecyclerView leftClassifyRecyclerView;

    @BindView(R.id.magic_indicator)
    public MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> leftClassifyElements = new ArrayList<>();
    private BaseQuickAdapter leftClassifyAdapter;

    private ElementResponse element_response;
    private Handler handler = new Handler();

    private SearchHistoryPopupWindow searchHistoryPopupWindow;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        initSearchView();
        initHistoryView();
        // 左侧分类
        initLeftClassfiyView();
    }

    private void initHistoryView () {
        searchHistory.setOnClickListener(v -> {
            if (searchHistoryPopupWindow == null) {
                // 创建 popupwindow 并注册事件监听
                searchHistoryPopupWindow = new SearchHistoryPopupWindow(getActivity());
                searchHistoryPopupWindow.registerListener(text -> {
                    searchTextView.setText(text);
                    UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                        intent.putExtra("search",text);
                        intent.putExtra("isCharge", "free");
                        return intent;
                    });
                });
            }
            // 显示 popupwindow
            searchHistoryPopupWindow.show();
        });
    }

    private void initSearchView () {
        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(searchTextView)
                .map(CharSequence::toString)
                .subscribe(s -> {
                    if (!TextUtils.isEmpty(s)) {
                        searchEditTextClear.setVisibility(View.VISIBLE);
                    } else {
                        searchEditTextClear.setVisibility(View.GONE);
                    }
                });
        // 清空输入框
        RxView.clicks(searchEditTextClear)
                .subscribe(aVoid -> searchTextView.setText(""));

        RxView.clicks(searchBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .map(aVoid -> searchTextView.getText().toString().trim())
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribe(searchText -> {
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchTextView, mContext);
//                    showSearchAnim();
//                    clearData();

                    // 记录搜索历史
                    CommonUtil.recordSearchHistory(mContext, searchText);

                    UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                        intent.putExtra("search",searchText);
                        intent.putExtra("isCharge", "free");
                        return intent;
                    });
                });

        // 定时任务定时修改 TextView 中的提示文字
        searchTextViewDisposable = Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())                   // 在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(aLong -> {
                    searchTextView.setHint(Constants.COURSE_SEARCH_HINT_LIST.get(showHintIndex));
                    showHintIndex = showHintIndex == Constants.COURSE_SEARCH_HINT_LIST.size() - 1 ? 0 : ++ showHintIndex;
                });
    }

    @Override
    protected void initData() {
        LinkKnownApiFactory.getLinkKnownApi().filterElementByPlacement(Constants.PLACEMENT_HOT_COURSE_TYPE_CAROUSEL)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<ElementResponse>() {
                    @Override
                    public void onNext(ElementResponse elementResponse) {
                        element_response = elementResponse;
                        if (elementResponse.isSuccess()) {
                            leftClassifyElements.clear();
                            leftClassifyElements.addAll(ElementResponse.getLevelOneClassifyElements(elementResponse.getElements()));
                            leftClassifyAdapter.setList(leftClassifyElements);

                            // 选中第一项
                            initCheckFirst();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", e.getMessage());
                    }
                });
    }

    private void initCheckFirst() {
        // 延迟 100 ms 执行,因为界面显示是异步的
        handler.postDelayed(() -> {
            // 默认选中第 1 个
            TextView textView = (TextView) leftClassifyAdapter.getViewByPosition(0, R.id.classifyName);
            if (textView != null) {
                textView.performClick();
            }
        }, 100);
    }



    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_classify;
    }

    private void initTopClassfiyView (List<String> mTitleDataList) {
        // 创建指示器并绑定到 viewPager
        CommonNavigator commonNavigator = CommonNavigatorCreater.setDefaultNavigator(getContext(), mTitleDataList, viewPager);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        // 动态创建多个 fragment
        List<Fragment> mFragments = new ArrayList<>();
        for (String title : mTitleDataList) {
            CourseFilterFragment courseFilterFragment = new CourseFilterFragment();
            courseFilterFragment.setArguments(CommonUtil.createBundle2("search",title,"isCharge", "free"));
            mFragments.add(courseFilterFragment);
        }

        // 给 viewPager 绑定 fragment
        com.linkknown.ilearning.helper.ViewPagerHelper.bindQuickAdapter(viewPager, mFragments, mTitleDataList, getChildFragmentManager());
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
        // 默认选中第一项
        if (mTitleDataList.size() > 0) {
            viewPager.setCurrentItem(0);
        }
    }

    private void initLeftClassfiyView() {
        // 一级分类
        leftClassifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.classify_left_item, leftClassifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
            }
        };
        leftClassifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        leftClassifyRecyclerView.setAdapter(leftClassifyAdapter);
        leftClassifyAdapter.addChildClickViewIds(R.id.classifyName);

        leftClassifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.classifyName) {
                for (int index = 0; index < leftClassifyElements.size(); index ++) {
                    if (index == position) {
                        TextView textView = (TextView)view;
                        textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.white));
                        textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorPrimary));
                    } else {
                        TextView textView = (TextView) adapter.getViewByPosition(index, R.id.classifyName);
                        if (textView != null) {
                            textView.setBackgroundColor(UIUtils.getResourceColor(mContext, R.color.colorRecyclerBg));
                            textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorGray));
                        }
                    }
                }

                List<String> mTitleDataList = new ArrayList<>();
                List<ElementResponse.Element> levelTwoClassifyElements = ElementResponse.getLevelTwoClassifyElements(element_response.getElements(), leftClassifyElements.get(position).getId());
                for (ElementResponse.Element element: levelTwoClassifyElements) {
                    mTitleDataList.add(element.getElement_label());
                }
                initTopClassfiyView(mTitleDataList);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UIUtils.disposeSecurity(searchTextViewDisposable);
    }
}
