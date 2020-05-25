package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ClassifyFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.levelOneClassifyRecyclerView)
    public RecyclerView levelOneClassifyRecyclerView;
    @BindView(R.id.levelTwoClassifyRecyclerView)
    public RecyclerView levelTwoClassifyRecyclerView;
    @BindView(R.id.home_serachview)
    public SearchView searchView;

    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> levelOneClassifyElements = new ArrayList<>();
    private List<ElementResponse.Element> levelTwoClassifyElements = new ArrayList<>();
    private BaseQuickAdapter levelOneClassifyAdapter;
    private BaseQuickAdapter levelTwoClassifyAdapter;

    // 过滤课程的 fragment
    private CourseFilterFragment courseFilterFragment;
    private ElementResponse element_response;
    private Handler handler = new Handler();

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        // 左侧分类
        initLeftClassfiyView();
        // 右侧搜索结果，一行只显示一个
        courseFilterFragment = new CourseFilterFragment(1);
        getChildFragmentManager().beginTransaction().add(R.id.courseFilterFragment, courseFilterFragment).commit();
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
}
