package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.diff.CommonDiffCallback;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CommentResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserCourseFragment extends BaseLazyLoadFragment {

    private Context mContext;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    private List<CourseMetaResponse.CourseMeta> courseMetaList = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    private String userName;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);

        userName = this.getArguments().getString(Constants.USER_NAME);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CourseHotRecommendSection courseHotRecommendSection = new CourseHotRecommendSection(mContext, courseMetaList);
        sectionedRecyclerViewAdapter.addSection(courseHotRecommendSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    @Override
    protected void initData() {
        LinkKnownApiFactory.getLinkKnownApi().getCourseListByUserName(userName, 1, 10)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess()) {
                            courseMetaList.addAll(courseMetaResponse.getCourses());
                            sectionedRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError=>", Constants.TIP_SYSTEM_ERROR);
                        ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                    }
                });
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_user_course;
    }
}
