package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.ElementResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ClassifyFragment extends BaseLazyLoadFragment {

    private Context mContext;

    @BindView(R.id.classifyRecyclerView)
    public RecyclerView classifyRecyclerView;
    @BindView(R.id.home_serachview)
    public SearchView searchView;

    // 存储所有的分类占位符数据
    private List<ElementResponse.Element> classifyElements = new ArrayList<>();
    private BaseQuickAdapter classifyAdapter;

    // 过滤课程的 fragment
    private CourseFilterFragment courseFilterFragment;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
        // 左侧分类
        initLeftClassfiyView();
        // 右侧搜索结果
        courseFilterFragment = new CourseFilterFragment();
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
                        if (elementResponse.isSuccess()) {
                            classifyElements.clear();
                            classifyElements.addAll(elementResponse.getElements());
                            classifyAdapter.setList(classifyElements);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", e.getMessage());
                    }
                });
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
        classifyAdapter = new BaseQuickAdapter<ElementResponse.Element, BaseViewHolder>(R.layout.classify_left_item, classifyElements) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ElementResponse.Element element) {
                baseViewHolder.setText(R.id.classifyName, element.getElement_label());
            }
        };
        classifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        classifyRecyclerView.setAdapter(classifyAdapter);
        classifyAdapter.addChildClickViewIds(R.id.classifyName);
        classifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.classifyName) {
                return;
            }
            for (int index = 0; index < classifyElements.size(); index ++) {
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
            courseFilterFragment.refreshFragment(classifyElements.get(position).getElement_label(), "");
        });
    }
}
