package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
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

    @BindView(R.id.leftClassifyRecyclerView)
    public RecyclerView leftClassifyRecyclerView;
    @BindView(R.id.right)
    public ListView rightCourseListView;
    @BindView(R.id.progress)
    public RelativeLayout mProgressBar;
    @BindView(R.id.home_serachview)
    public SearchView searchView;

    // 存储所有的分类占位符数据
    List<ElementResponse.Element> classifyElements = new ArrayList<>();
    BaseQuickAdapter classifyAdapter;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        initLeftClassfiyView();
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
        leftClassifyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        leftClassifyRecyclerView.setAdapter(classifyAdapter);
        classifyAdapter.addChildClickViewIds(R.id.classifyName);
        classifyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.classifyName) {
                return;
            }
            for (int index = 0; index < classifyElements.size(); index ++) {
                if (index == position) {
                    ((TextView)view).setText(((TextView)view).getText() + "~");
                } else {
                    TextView textView = (TextView) adapter.getViewByPosition(index, R.id.classifyName);
                    textView.setText((StringUtils.replace(textView.getText().toString(), "~", "")));
                }
            }
            //加载中动效
            showLoading(true);
            // 加载右侧视频数据
            searchCourse(classifyElements.get(position).getElement_label());
        });
    }

    private void searchCourse(String searchText) {
        LinkKnownApiFactory.getLinkKnownApi().searchCourseList(searchText, "", 1, Constants.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            CommonAdapter rightCourseListAdapter = new CommonAdapter<CourseMetaResponse.CourseMeta>((ArrayList<CourseMetaResponse.CourseMeta>) courseMetaResponse.getCourses(), R.layout.classify_right_item) {

                                @Override
                                public void bindView(ViewHolder holder, CourseMetaResponse.CourseMeta courseMeta) {
                                    holder.setText(R.id.courseName, courseMeta.getCourse_name());
                                }
                            };
                            rightCourseListView.setAdapter(rightCourseListAdapter);
                        }
                        // 去掉加载效果
                        showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("searchCourseList error", e.getMessage());
                        // 去掉加载效果
                        showLoading(false);
                    }
                });
    }

    /**
     * 当进行网络请求时，播放进度条动画
     *
     * @param isLoading 是否正在网络请求
     */
    private void showLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rightCourseListView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
