package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.FavoriteCountResponse;
import com.linkknown.ilearning.model.IsFavoriteResponse;
import com.linkknown.ilearning.model.ui.CourseOperate;
import com.linkknown.ilearning.section.CommonTagSection;
import com.linkknown.ilearning.section.CourseDetailCVideoListSection;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.DrawableUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;
import java.util.function.Function;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
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

    // 课程操作菜单和适配器
    private List<CourseOperate> courseOperates;
    private MultiTypeAdapter courseOperateAdapter;

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

        // 操作区域初始化并刷新刷新
        initCourseOperateArea();
        refreshCourseOperateArea(course);

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

    private void handleCourseOperateClick (String operateName) {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        switch (operateName) {
            case CourseOperate.OPERATE_SHOU_CANG:
                LinkKnownApiFactory.getLinkKnownApi().toggleFavorite(course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT)
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {

                            @Override
                            public void onNext(BaseResponse response) {
                                if (response.isSuccess()) {
                                    ToastUtil.showText(mContext, "课程收藏成功！");
                                    // 收藏成功后刷新收藏区域
                                    refreshCourseOperateArea(course);
                                } else {
                                    ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showText(mContext, Constants.TIP_SYSTEM_ERROR);
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void refreshCourseOperateArea(CourseDetailResponse.Course course) {
        Observable.zip(
                LinkKnownApiFactory.getLinkKnownApi().queryFavoriteCount(course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT),
                // 是否收藏应该从请求头中获取用户名，不强制登录，没登录不发送请求
                LinkKnownApiFactory.getLinkKnownApi().isFavorite(LoginUtil.getLoginUserName(mContext), course.getId(), Constants.FAVORITE_TYPE_COURSE_COLLECT),
                new BiFunction<FavoriteCountResponse, IsFavoriteResponse, CourseOperate.CourseOperateResponseWrapper>() {
                    @Override
                    public CourseOperate.CourseOperateResponseWrapper apply(FavoriteCountResponse favoriteCountResponse, IsFavoriteResponse isFavoriteResponse) throws Exception {
                        CourseOperate.CourseOperateResponseWrapper wrapper = new CourseOperate.CourseOperateResponseWrapper();
                        wrapper.setFavoriteCountResponse(favoriteCountResponse);
                        wrapper.setIsFavoriteResponse(isFavoriteResponse);
                        return wrapper;
                    }
                }
        ).subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownOnNextObserver<CourseOperate.CourseOperateResponseWrapper>() {
                    @Override
                    public void onNext(CourseOperate.CourseOperateResponseWrapper wrapper) {
                        if (wrapper.getIsFavoriteResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_SHOU_CANG);
                            operate.setOperateStatus(wrapper.getIsFavoriteResponse().isFavorite ? 1 : 0);
                        }
                        if (wrapper.getFavoriteCountResponse().isSuccess()) {
                            CourseOperate operate = CourseOperate.getCourseOperateByName(courseOperates, CourseOperate.OPERATE_SHOU_CANG);
                            operate.setOperateNum(wrapper.getFavoriteCountResponse().getCounts());
                        }
                        courseOperateAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initCourseOperateArea() {
        // 获取初始操作信息
        courseOperates = CourseOperate.getInitialCourseOperates();
        courseOperateAdapter = new MultiTypeAdapter();

        courseOperateAdapter.register(CourseOperate.class, new MultiItemView<CourseOperate>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_course_operate;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CourseOperate operate, int position) {
                // 设置操作名称
                viewHolder.setText(R.id.operateNameText, operate.getOperateName());
                // 主要是 tint 属性
                VectorDrawableCompat vectorDrawableCompat =
                        VectorDrawableCompat.create(getResources(), operate.getOperateIcon(), mContext.getTheme());
                // 颜色
                int color = operate.getOperateStatus() == 1 ?
                        ContextCompat.getColor(mContext, R.color.colorPrimary) : ContextCompat.getColor(mContext, R.color.gray);
                // 设置图标和图标颜色
                // 使用工具类解决着色共享状态的 bug
                viewHolder.setImageDrawable(R.id.operateIcon, DrawableUtil.tintDrawable(vectorDrawableCompat, color));
                viewHolder.setText(R.id.operateNum, operate.getOperateNum() + "");
                viewHolder.setOnClickListener(R.id.operateIcon, v -> handleCourseOperateClick(operate.getOperateName()));
            }
        });
        courseOperateAdapter.setItems(courseOperates);
        courseOperateRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        courseOperateRecyclerView.setAdapter(courseOperateAdapter);
    }

    @OnClick(R.id.userInfoLayout)
    public void showUserDetail () {
        UIUtils.gotoActivity(mContext, UserDetailActivity.class, intent -> {
            intent.putExtra(Constants.USER_NAME, courseDetailResponse.getCourse().getCourse_author());
            return intent;
        });
    }

}

