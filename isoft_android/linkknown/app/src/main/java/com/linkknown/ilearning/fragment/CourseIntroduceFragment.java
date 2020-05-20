package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.common.LinkKnownOnNextObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.model.IsFavoriteResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.model.ui.CourseOperate;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.CourseDetailCVideoListSection;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseIntroduceFragment extends BaseLazyLoadFragment {

    private Context mContext;
    private CourseDetailResponse courseDetailResponse;

    public CourseIntroduceFragment(CourseDetailResponse courseDetailResponse) {
        this.courseDetailResponse = courseDetailResponse;
    }

    @BindView(R.id.courseNameText)
    public TextView courseNameText;
    @BindView(R.id.playNumberText)
    public TextView playNumberText;
    @BindView(R.id.courseNumberText)
    public TextView courseNumberText;
    @BindView(R.id.courseShortDescText)
    public TextView courseShortDescText;
    @BindView(R.id.courseOperateRecyclerView)
    public RecyclerView courseOperateRecyclerView;
    @BindView(R.id.cVideoRecyclerView)
    public RecyclerView cVideoRecyclerView;
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    @BindView(R.id.userNameText)
    public TextView userNameText;

    private List<CourseOperate> courseOperates;

    @Override
    protected void initView(View mRootView) {
        mContext = getActivity();
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void initData() {
        init();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.fragment_course_introduce;
    }

    private void init () {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        List<CourseDetailResponse.CVideo> cVideos = courseDetailResponse.getCVideos();
        String course_label = courseDetailResponse.getCourse().getCourse_label();

        // 操作区域刷新
        refreshOperateArea(course);

        // 设置课程名称
        courseNameText.setText(course.getCourse_name());
        // 设置播放次数
        playNumberText.setText(course.getWatch_number() + "");
        // 课程集数
        courseNumberText.setText(course.getCourse_number() + "");
        // 课程描述
        courseShortDescText.setText(course.getCourse_short_desc());

        userNameText.setText(courseDetailResponse.getUser().getNick_name());
        UIUtils.setImage(mContext, headerIcon, courseDetailResponse.getUser().getSmall_icon());

        // 设置视频列表 section 部分
        SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        CommonTagSection commonTagSection = new CommonTagSection(mContext, CommonUtil.splitCommonTag(course_label));
        CourseDetailCVideoListSection courseDetailCVideoListSection = new CourseDetailCVideoListSection(mContext, cVideos);
        sectionedRecyclerViewAdapter.addSection(commonTagSection);
        sectionedRecyclerViewAdapter.addSection(courseDetailCVideoListSection);
        cVideoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        cVideoRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    // 操作区域刷新
    private void refreshOperateArea(CourseDetailResponse.Course course) {
        // 获取初始操作信息
        courseOperates = CourseOperate.getInitialCourseOperates();
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(CourseOperate.class, new MultiItemView<CourseOperate>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_course_operate;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CourseOperate operate, int i) {
                // 设置操作名称
                viewHolder.setText(R.id.operateNameText, operate.getOperateName());

                // 主要是 tint 属性
                VectorDrawableCompat vectorDrawableCompat =
                        VectorDrawableCompat.create(getResources(), operate.getOperateIcon(), mContext.getTheme());
                vectorDrawableCompat.setTint(operate.getOperateStatus() == 1 ?
                                ContextCompat.getColor(mContext, R.color.colorPrimary) : ContextCompat.getColor(mContext, R.color.gray));
                // 设置图标和图标颜色
                viewHolder.setImageDrawable(R.id.operateIcon, vectorDrawableCompat);
            }
        });
        multiTypeAdapter.setItems(courseOperates);
        courseOperateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        courseOperateRecyclerView.setAdapter(multiTypeAdapter);

        LinkKnownApiFactory.getLinkKnownApi().isFavorite(LoginUtil.getLoginUserName(mContext), course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownOnNextObserver<IsFavoriteResponse>() {
                    @Override
                    public void onNext(IsFavoriteResponse isFavoriteResponse) {
                        if (isFavoriteResponse.isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_SHOU_CANG);
                            operate.setOperateStatus(isFavoriteResponse.isFavorite ? 1 : 0);
                            operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_HUANCUN);
                            operate.setOperateStatus(isFavoriteResponse.isFavorite ? 1 : 0);
                        }
                        multiTypeAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick(R.id.userInfoLayout)
    public void showUserDetail () {
        UIUtils.gotoActivity(mContext, UserDetailActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME, courseDetailResponse.getCourse().getCourse_author());
            return intent;
        });
    }
}
