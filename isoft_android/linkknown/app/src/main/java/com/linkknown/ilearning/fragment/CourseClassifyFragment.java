package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseSearchActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.popup.HotSearchPopView;
import com.linkknown.ilearning.popup.SearchHistoryPopView;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    //搜索历史
    @BindView(R.id.searchHistory)
    public TextView searchHistory;

    private Disposable searchTextViewDisposable;
    private int showHintIndex = 0;

    @BindView(R.id.leftClassifyRecyclerView)
    public RecyclerView leftClassifyRecyclerView;

    @BindView(R.id.rightClassifyRecyclerView)
    public RecyclerView rightClassifyRecyclerView;


    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> leftClassifyElements = new ArrayList<>();
    private BaseQuickAdapter leftClassifyAdapter;

    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> rightClassifyElements = new ArrayList<>();
    private BaseQuickAdapter rightClassifyAdapter;

    private ElementResponse element_response;
    private Handler handler = new Handler();

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        //初始化查询视图
        initSearchView();
        //初始化热门搜索视图
        initHotSearchView();
        //初始化历史视图
        initHistorySearchView();
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
            new XPopup.Builder(getContext())
                    .hasShadowBg(true)
                    .asCustom(new HotSearchPopView(mContext, text -> {
                        // TODO 确认只查免费？
                        UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                            intent.putExtra("search", text);
                            intent.putExtra("isCharge", "free");
                            return intent;
                        });
                    })).show();
        });
    };


    /**
     * 初始化搜索历史视图
     */
    private void initHistorySearchView () {
        searchHistory.setOnClickListener(v -> new XPopup.Builder(getContext())
                .hasShadowBg(true)
                .asCustom(new SearchHistoryPopView(mContext, text -> {
                    // TODO 确认只查免费？
                    UIUtils.gotoActivity(mContext, CourseSearchActivity.class, intent -> {
                    intent.putExtra("search",text);
                    intent.putExtra("isCharge", "free");
                    return intent;
                });
            })).show());
    }

    private int selectLeftClassifyIndex = 0;    // 左侧分类默认选中项

    /**
     * 初始化左侧分类
     */
    private void initLeftClassfiyView() {
        // 一级分类
        leftClassifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.item_course_type_left_classify, leftClassifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
                baseViewHolder.findView(R.id.classifyName).setSelected(selectLeftClassifyIndex == baseViewHolder.getAdapterPosition());
            }
        };
        leftClassifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        leftClassifyRecyclerView.setAdapter(leftClassifyAdapter);
        leftClassifyAdapter.addChildClickViewIds(R.id.classifyName);

        leftClassifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.classifyName) {
                selectLeftClassifyIndex = position;
                leftClassifyAdapter.notifyDataSetChanged();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i =0; i< leftClassifyElements.size(); i++) {
//                            View cView = leftClassifyAdapter.getViewByPosition(i, R.id.classifyName);
//                            if (cView != null) {
//                                if (i == position) {
//                                    selectLeftClassifyIndex =
//                                    cView.setSelected(true);
//                                } else {
//                                    cView.setSelected(false);
//                                }
//                            }
//                        }
//                    }
//                }, 100);
                List<ElementResponse.Element> levelTwoClassifyElements = ElementResponse.getLevelTwoClassifyElements(element_response.getElements(), leftClassifyElements.get(position).getId());
                initRightClassfiyView(levelTwoClassifyElements);
            }
        });
    }

    private void initRightClassfiyView(List<ElementResponse.Element> levelTwoClassifyElements) {
        rightClassifyElements.clear();
        rightClassifyElements.addAll(levelTwoClassifyElements);
        // 二级分类
        rightClassifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.item_course_type_right_classify, rightClassifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                UIUtils.setImage(mContext, baseViewHolder.findView(R.id.classifyImage), element.getImg_path());
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
            }
        };
        rightClassifyRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        rightClassifyRecyclerView.setAdapter(rightClassifyAdapter);
        rightClassifyAdapter.addChildClickViewIds(R.id.classifyName);

        rightClassifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.classifyName) {
                ToastUtil.showText(mContext, "点击了");
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        UIUtils.disposeSecurity(searchTextViewDisposable);
    }
}
