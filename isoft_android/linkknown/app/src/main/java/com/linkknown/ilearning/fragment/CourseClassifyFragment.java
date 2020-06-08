package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseSearchActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.ViewPagerIndicatorManager;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.popup.BottomPopupWindow;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.ShowAndCloseMoreSection;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseClassifyFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.searchTextView)
    public EditText searchTextView;
    @BindView(R.id.searchEditTextClear)
    public ImageView searchEditTextClear;

    //查询按钮
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;

    //热门搜索
    @BindView(R.id.hotSearch)
    public TextView hotSearch;
    private SectionedRecyclerViewAdapter tagRecyclerViewAdapter;
    List<String> showTagList = new ArrayList<>();
    List<String> hideTagList = new ArrayList<>();

    //搜索历史
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

    private BottomPopupWindow bottomPopupWindow_searchHistory;
    private BottomPopupWindow bottomPopupWindow_hotSearch;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        //初始化查询视图
        initSearchView();
        //初始化热门搜索视图
        initHotSearchView();
        //初始化历史视图
        initHistoryView();
        // 初始化左侧分类
        initLeftClassfiyView();
    }


    /**
     * 初始化查询视图
     */
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


    /**
     * 初始化热门搜索视图
     */
    private void initHotSearchView(){
        hotSearch.setOnClickListener(v -> {
            if (bottomPopupWindow_hotSearch == null) {
                // 创建 popupwindow 并注册事件监听
                bottomPopupWindow_hotSearch = new BottomPopupWindow(getActivity()) {
                    @Override
                    public TextView createView(Integer position, FlexboxLayout layout, String text) {
                        tagRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                        showTagList = new ArrayList<>(Arrays.asList("java1", "java2","java3", "java4","java5", "java6","java7","java8"));
                        CommonTagSection showCommonTagSection = new CommonTagSection(mContext, showTagList);
                        CommonTagSection hideCommonTagSection = new CommonTagSection(mContext, hideTagList);
                        ShowAndCloseMoreSection showAndCloseMoreSection = new ShowAndCloseMoreSection(mContext, new ShowAndCloseMoreSection.ClickListener() {
                            @Override
                            public void showMore() {
                                hideTagList.clear();
                                hideTagList.addAll(new ArrayList<>(Arrays.asList("go1", "go2","go3", "go4","go5", "go6","go7", "go8")));
                                tagRecyclerViewAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void closeMore() {
                                hideTagList.clear();
                                tagRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                        tagRecyclerViewAdapter.addSection(showCommonTagSection);
                        tagRecyclerViewAdapter.addSection(hideCommonTagSection);
                        tagRecyclerViewAdapter.addSection(showAndCloseMoreSection);
                        showTagView.setLayoutManager(new LinearLayoutManager(mContext));
                        showTagView.setAdapter(tagRecyclerViewAdapter);

                        final TextView textView = (TextView) View.inflate(mContext, R.layout.item_common_tag, null);
                        // 点击事件
                        textView.setOnClickListener(v -> {
                            onConfirm(textView.getText().toString());
                            // 点击完成后隐藏 popupwindow
                            onDismiss();
                            dismiss();
                        });
                        return textView;
                    }

                    @Override
                    public void initData() {
                        createView(null, null, null);
                    }

                    @Override
                    public void onConfirm(String text) {
                        searchTextView.setText(text);
                        UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                            intent.putExtra("search",text);
                            intent.putExtra("isCharge", "free");
                            return intent;
                        });
                    };
                };
            };

            //显示弹框
            bottomPopupWindow_hotSearch.setLeftTip("大家都在搜索");
            bottomPopupWindow_hotSearch.showLeftTip(true);
            bottomPopupWindow_hotSearch.showRightTip(false);
            bottomPopupWindow_hotSearch.showTagView(true);
            bottomPopupWindow_hotSearch.show();
        });
    };


    /**
     * 初始化历史视图
     */
    private void initHistoryView () {
        searchHistory.setOnClickListener(v -> {
            if (bottomPopupWindow_searchHistory == null) {
                // 创建 popupwindow 并注册事件监听
                bottomPopupWindow_searchHistory = new BottomPopupWindow(getActivity()) {
                    @Override
                    public TextView createView(Integer position, FlexboxLayout layout, String text) {
                        // 根据布局动 id 态创建 TextView
                        final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, layout, false);
                        textView.setText(text);
                        // 设置随机背景色，文字使用白色
                        textView.setBackgroundColor(UIUtils.getRandomResourceColor(position));
                        textView.setTextColor(Color.WHITE);

                        // 点击事件
                        textView.setOnClickListener(v -> {
                            onConfirm(textView.getText().toString());
                            // 点击完成后隐藏 popupwindow
                            onDismiss();
                            dismiss();
                        });
                        return textView;
                    }

                    @Override
                    public void initData() {
                        // TODO 显示排序有点问题
                        Set<String> searchTexts = CommonUtil.getSearchHistory(mContext);
                        LinkedList<String> searchTextList = new LinkedList<>(searchTexts);
                        showTextView.removeAllViews();
                        for (int i = 0; i < searchTextList.size(); i++) {
                            final TextView textView = createView(i, showTextView, searchTextList.get(i));
                            showTextView.addView(textView);
                        }
                    }

                    @Override
                    public void onConfirm(String text) {
                        searchTextView.setText(text);
                        UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                            intent.putExtra("search",text);
                            intent.putExtra("isCharge", "free");
                            return intent;
                        });
                    };
                };
            };

            //显示弹框
            bottomPopupWindow_searchHistory.setLeftTip("搜索历史");
            bottomPopupWindow_searchHistory.setRightTip("清空");
            bottomPopupWindow_searchHistory.showLeftTip(true);
            bottomPopupWindow_searchHistory.showRightTip(true);
            bottomPopupWindow_searchHistory.showTextView(true);
            bottomPopupWindow_searchHistory.show();
        });
    }


    /**
     * 初始化左侧分类
     */
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
        return R.layout.fragment_course_classify;
    }

    private void initTopClassfiyView (List<String> mTitleDataList) {
        // 动态创建多个 fragment
        List<Fragment> mFragments = new ArrayList<>();
        for (String title : mTitleDataList) {
            CourseFilterFragment courseFilterFragment = new CourseFilterFragment();
            courseFilterFragment.setArguments(CommonUtil.createBundle2("search",title,"isCharge", "free"));
            mFragments.add(courseFilterFragment);
        }

        // 给 viewPager 绑定 fragment
        com.linkknown.ilearning.helper.ViewPagerHelper.bindQuickAdapter(viewPager, mFragments, mTitleDataList, getChildFragmentManager());

        // 创建 viewPager + 指示器管理类
        ViewPagerIndicatorManager viewPagerIndicatorManager =
                new ViewPagerIndicatorManager(getContext(), magicIndicator, mTitleDataList, viewPager);
        // 由管理类统一进行绑定
        viewPagerIndicatorManager.bind();

        // 默认选中第一项
        if (mTitleDataList.size() > 0) {
            viewPager.setCurrentItem(0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        UIUtils.disposeSecurity(searchTextViewDisposable);
    }
}
