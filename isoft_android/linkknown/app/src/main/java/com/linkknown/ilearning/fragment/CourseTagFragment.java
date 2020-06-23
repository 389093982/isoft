package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.activity.ReceiveCouponCenterActivity;
import com.linkknown.ilearning.adapter.CourseCardAdapter;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CourseTagFragment extends Fragment {

    // 下拉刷新布局
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.banner)
    Banner banner;
    // 存放轮播图所有图片
    private ArrayList<Integer> bannerImageList;

    private List<CourseMetaResponse.CourseMeta> hotCourseMetas = new ArrayList<>();

    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_tag, container, false);

        mContext = getActivity();

        ButterKnife.bind(this, rootView);

        swipeRefreshLayoutHelper.bind(getActivity(), refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        // 初始化组件
        initView();

        // 加载数据
        initData();

        return rootView;
    }

    private CourseCardAdapter courseCardAdapter;

    private void initView () {
        initBannerView();

        courseCardAdapter = new CourseCardAdapter(mContext, hotCourseMetas);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(courseCardAdapter);
    }

    private void initBannerView() {
        bannerImageList = new ArrayList<>();
        bannerImageList.add(R.drawable.banner_coupon);
        bannerImageList.add(R.drawable.banner_huodong);
        bannerImageList.add(R.drawable.banner_shaoer);
        bannerImageList.add(R.drawable.banner_biancheng);

        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //轮播图的监听
        banner.setOnBannerListener(position -> {
            switch (position) {
                case 0:
                    UIUtils.gotoActivity(mContext, ReceiveCouponCenterActivity.class);
                    break;
                case 1:
                    UIUtils.gotoActivity(mContext, HuodongActivity.class);
                    break;
                default:
                    break;
            }
        });
        banner.setImages(bannerImageList).start();
    }


    private void initData() {

        LinkKnownApiFactory.getLinkKnownApi().queryCustomTagCourse("hot",1, 10)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseMetaResponse>() {
                    @Override
                    public void onNext(CourseMetaResponse o) {
                        if (o.isSuccess()){
                            if (CollectionUtils.isNotEmpty(o.getCourses())) {
                                hotCourseMetas.clear();
                                hotCourseMetas.addAll(o.getCourses());
                                // 加载成功后才设置头部
                                if (!courseCardAdapter.hasHeaderLayout()) {
                                    View headerView = getLayoutInflater().inflate(R.layout.layout_region_recommend_hot_head, recyclerView, false);
                                    // 指定添加位置
                                    courseCardAdapter.addHeaderView(headerView, 1);
                                }
                                courseCardAdapter.setList(hotCourseMetas);
                            }
                        }
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayoutHelper.finishRefreshing();
                    }
                });
    }

}
