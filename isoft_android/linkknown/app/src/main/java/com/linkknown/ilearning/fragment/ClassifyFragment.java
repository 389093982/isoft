package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.popup.SearchHistoryPopupWindow;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

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

    private String search;
    private Disposable searchTextViewDisposable;
    private int showHintIndex = 0;

    @BindView(R.id.levelOneClassifyRecyclerView)
    public RecyclerView levelOneClassifyRecyclerView;
    @BindView(R.id.levelTwoClassifyRecyclerView)
    public RecyclerView levelTwoClassifyRecyclerView;

    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> levelOneClassifyElements = new ArrayList<>();
    private List<ElementResponse.Element> levelTwoClassifyElements = new ArrayList<>();
    private BaseQuickAdapter levelOneClassifyAdapter;
    private BaseQuickAdapter levelTwoClassifyAdapter;

    // 过滤课程的 fragment
    private CourseFilterFragment courseFilterFragment;
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
        // 右侧搜索结果，一行只显示一个
        courseFilterFragment = new CourseFilterFragment(1);
        getChildFragmentManager().beginTransaction().add(R.id.courseFilterFragment, courseFilterFragment).commit();
    }

    private void initHistoryView () {
        searchHistory.setOnClickListener(v -> {
            if (searchHistoryPopupWindow == null) {
                // 创建 popupwindow 并注册事件监听
                searchHistoryPopupWindow = new SearchHistoryPopupWindow(getActivity());
                searchHistoryPopupWindow.registerListener(text -> {
                    searchTextView.setText(text);
                    search = text;
                    courseFilterFragment.refreshFragment(search, "");
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
                .subscribe(s -> {
                    // 关闭软键盘
                    KeyBoardUtil.closeKeybord(searchTextView, mContext);
//                    showSearchAnim();
//                    clearData();
                    search = s;

                    // 记录搜索历史
                    CommonUtil.recordSearchHistory(mContext, search);

                    courseFilterFragment.refreshFragment(search, "");
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
                            levelOneClassifyElements.clear();
                            levelOneClassifyElements.addAll(getLevelOneClassifyElements(elementResponse.getElements()));
                            levelOneClassifyAdapter.setList(levelOneClassifyElements);

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
        // 延迟 500 ms 执行,因为界面显示是异步的
        handler.postDelayed(() -> {
            // 默认选中第 1 个
            TextView textView = (TextView) levelOneClassifyAdapter.getViewByPosition(0, R.id.classifyName);
            if (textView != null) {
                textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorPrimary));
            }
            // 默认加载第一项数据
            levelTwoClassifyElements.clear();
            levelTwoClassifyElements.addAll(getLvelTwoClassifyElements(element_response.getElements(), levelOneClassifyElements.get(0).getId()));
            levelTwoClassifyAdapter.setList(levelTwoClassifyElements);
        }, 500);
    }

    private List<ElementResponse.Element> getLevelOneClassifyElements (List<ElementResponse.Element> elements) {
        List<ElementResponse.Element> levelOneElements = new ArrayList<>();
        for (ElementResponse.Element element : elements) {
            if (element.getNavigation_level() == 0) {
                levelOneElements.add(element);
            }
        }
        Collections.sort(levelOneElements, (o1, o2) -> getLvelTwoClassifyElements(elements, o2.getId()).size() - getLvelTwoClassifyElements(elements, o1.getId()).size());
        return levelOneElements;
    }

    private List<ElementResponse.Element> getLvelTwoClassifyElements (List<ElementResponse.Element> elements, int parentId) {
        List<ElementResponse.Element> levelTwoElements = new ArrayList<>();
        for (ElementResponse.Element element : elements) {
            if (element.getNavigation_parent_id() == parentId) {
                levelTwoElements.add(element);
            }
        }
        return levelTwoElements;
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_classify;
    }

    private void initLeftClassfiyView() {
        // 一级分类
        levelOneClassifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.classify_left_item, levelOneClassifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
            }
        };
        levelOneClassifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        levelOneClassifyRecyclerView.setAdapter(levelOneClassifyAdapter);
        levelOneClassifyAdapter.addChildClickViewIds(R.id.classifyName);
        levelOneClassifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.classifyName) {
                return;
            }
            for (int index = 0; index < levelOneClassifyElements.size(); index ++) {
                if (index == position) {
                    TextView textView = (TextView)view;
                    textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorPrimary));
                } else {
                    TextView textView = (TextView) adapter.getViewByPosition(index, R.id.classifyName);
                    if (textView != null) {
                        textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorGray));
                    }
                }
            }
            levelTwoClassifyElements.clear();
            levelTwoClassifyElements.addAll(getLvelTwoClassifyElements(element_response.getElements(), levelOneClassifyElements.get(position).getId()));
            levelTwoClassifyAdapter.setList(levelTwoClassifyElements);
        });


        // 二级分类
        levelTwoClassifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.classify_left_item, levelTwoClassifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
            }
        };
        levelTwoClassifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        levelTwoClassifyRecyclerView.setAdapter(levelTwoClassifyAdapter);
        levelTwoClassifyAdapter.addChildClickViewIds(R.id.classifyName);
        levelTwoClassifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.classifyName) {
                return;
            }
            for (int index = 0; index < levelTwoClassifyElements.size(); index ++) {
                if (index == position) {
                    TextView textView = (TextView)view;
                    textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorPrimary));
                } else {
                    TextView textView = (TextView) adapter.getViewByPosition(index, R.id.classifyName);
                    if (textView != null) {
                        textView.setTextColor(UIUtils.getResourceColor(mContext, R.color.colorGray));
                    }
                }
            }
            courseFilterFragment.refreshFragment(levelTwoClassifyElements.get(position).getElement_label(), "");
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (searchTextViewDisposable != null) {
            searchTextViewDisposable.dispose();
        }
    }
}
