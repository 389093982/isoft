package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.AppBarStateChangeEvent;
import com.linkknown.ilearning.common.CommonFragmentStatePagerAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.CourseCommentFragment;
import com.linkknown.ilearning.fragment.CourseIntroduceFragment;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.PayOrderResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.DisplayUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class CourseDetailActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mSlidingTabLayout;

    // 顶部导航栏布局
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 播放提示文本
    @BindView(R.id.playTipText)
    TextView playTipText;
    // 课程名称文本
    @BindView(R.id.courseNameText)
    TextView courseNameText;
    // 课程图片
    @BindView(R.id.video_preview)
    ImageView courseImage;

    // 悬浮按钮
    @BindView(R.id.fab)
    FloatingActionButton mFAB;

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    private int course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        mContext = this;
        // 绑定
        ButterKnife.bind(this);

        course_id = getIntent().getIntExtra("course_id", -1);

        // 初始化组件
        initView();

        // 加载数据
        initData();
    }

    private void renderUI (CourseDetailResponse courseDetailResponse) {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        // 设置课程名称
        courseNameText.setText(course.getCourse_name());
        // 设置课程图片
        UIUtils.setImage(getApplicationContext(), courseImage, course.getSmall_image());
        // 此处绑定 viewPager 是因为评论 tab 页标题中的评论数需要在请求后修改
        initViewPager(courseDetailResponse, course.getComments());
        // 点击播放图标进行播放
        mFAB.setOnClickListener(v -> {
            if (CollectionUtils.isNotEmpty(courseDetailResponse.getCVideos())) {
//                CourseDetailResponse.CVideo cVideo = courseDetailResponse.getCVideos().get(0);
                UIUtils.gotoActivity(mContext, VideoPlayActivity.class, intent -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseDetailResponse", courseDetailResponse);
                    intent.putExtras(bundle);
                    return intent;
                });
            } else {
                ToastUtil.showText(mContext, Constants.COURSE_PLAY_NO_COURSE_NUM_TIP);
            }
        });
    }

    /**
     * 获取支付订单信息- 判断某课程是否已付款
     * @return
     */
    private Observable<PayOrderResponse> getPayOrderResponseObservable (int course_id) {
        Observable<PayOrderResponse> payOrderResponseObservable;
        if (LoginUtil.checkHasLogin(mContext)) {
            payOrderResponseObservable =
                    LinkKnownApiFactory.getLinkKnownApi().queryPayOrderList(1, 10, "course_theme_type", String.valueOf(course_id), LoginUtil.getLoginUserName(mContext),"SUCCESS");
        } else {
            // 没登录则返回空订单
            payOrderResponseObservable = Observable.just(new PayOrderResponse());
        }
        return payOrderResponseObservable;
    }

    private void initData () {
        // 1、调用 showCourseDetailForApp 接口返回课程信息
        // 2、通过 flatMap 方法将 CourseDetailResponse 转换成 CourseDetailResponse
        // 转换的逻辑是通过 Observable.zip 方法合并三个网络请求
        Observable<CourseDetailResponse> courseDetailResponseObservable = LinkKnownApiFactory.getLinkKnownApi().showCourseDetailForApp(course_id)
                .flatMap((Function<CourseDetailResponse, ObservableSource<CourseDetailResponse>>) (CourseDetailResponse courseDetailResponse) -> {
                    // 返回合并的三个网络请求: 课程信息，课程作者信息，当前登录用户购买订单信息
                    return Observable.zip(Observable.just(courseDetailResponse),
                            LinkKnownApiFactory.getLinkKnownApi().getUserDetail(courseDetailResponse.getCourse().getCourse_author()),
                            getPayOrderResponseObservable(courseDetailResponse.getCourse().getId()),
                            new Function3<CourseDetailResponse, UserDetailResponse, PayOrderResponse, CourseDetailResponse>() {
                                // 合并逻辑实现
                                @Override
                                public CourseDetailResponse apply(CourseDetailResponse courseDetailResponse, UserDetailResponse userDetailResponse, PayOrderResponse payOrderResponse) throws Exception {
                                    // 给课程添加作者信息
                                    courseDetailResponse.setUser(userDetailResponse.getUser());
                                    // 给课程添加当前登录用户购买的订单信息
                                    courseDetailResponse.setPayOrder(CollectionUtils.isEmpty(payOrderResponse.getOrders()) ? null : payOrderResponse.getOrders().get(0));
                                    return courseDetailResponse;
                                }
                            });
                });
        comsumerData(courseDetailResponseObservable);
    }

    private void comsumerData(Observable<CourseDetailResponse> courseDetailResponseObservable) {
        courseDetailResponseObservable.subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<CourseDetailResponse>() {

                    @Override
                    public void onNext(CourseDetailResponse courseDetailResponse) {
                        if (courseDetailResponse.isSuccess()) {
                            // 加载成功后渲染 UI
                            renderUI(courseDetailResponse);
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                            ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                        ToastUtil.showText(mContext, "数据加载失败！");
                    }

                });
    }

    private void initToolBar() {
        toolbar.setTitle("");
        // 支持 ActionBar,方便在上面设置 menu
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            // 给左上角图标的左边加上一个返回的图标
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 图片也占用了状态栏,故需要使用沉浸式状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    // 希望点击图标左侧箭头返回上一页，需要加载选项菜单后，对于菜单项的点击事件调用如下方法
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_test) {
            ToastUtil.showText(getApplicationContext(), "点击了测试");
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        initToolBar();
        initListener();
    }

    private void initListener () {
        // 在 textView 左侧添加播放图标
        UIUtils.setTextViewDrawbleImg(getApplicationContext(), playTipText, R.drawable.ic_fab_play, 0,0,60,64);

        // addOnOffsetChangedListener: 当 AppBarLayout 垂直方向上的偏移量发生改变时，为触发一个回调方法定义的接口
        //   // 导航栏布局垂直移动时悬浮按钮跟着变化
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> setFloatingActionButtonStatus(verticalOffset));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeEvent() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                if (state == State.EXPANDED) {
                    //展开状态
                    playTipText.setVisibility(View.GONE);
                    courseNameText.setVisibility(View.VISIBLE);

                    /**
                     * contentInsetLeft、contentInsetRight、contentInsetStart、contentInsetEnd：Toolbar的左右两侧都是默认有16dp的padding的，
                     *      如果你需要让Toolbar上的内容与左右两侧的距离有变化，便可以通过以上四个属性来进行相应的设置。
                     * 比如要让内容紧贴左侧或起始侧便可以将contentInsetLeft或contentInsetStart设为0。
                     * 对应方法：
                     * setContentInsetsRelative(int,int)——对应start和end
                     * setContentInsetsAbsolute(int,int)——对应left和right
                     */
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 15), 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    playTipText.setVisibility(View.VISIBLE);

                    courseNameText.setVisibility(View.GONE);
                    // 或者使用  playTipText.setTranslationX(100) 实现
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 150), 0);
                } else {
                    playTipText.setVisibility(View.GONE);
                    courseNameText.setVisibility(View.VISIBLE);
                    toolbar.setContentInsetsRelative(DisplayUtil.dp2px(getApplicationContext(), 15), 0);
                }
            }
        });
    }

    // 导航栏布局垂直移动时悬浮按钮跟着变化
    private void setFloatingActionButtonStatus(int verticalOffset) {
        // 设置垂直位移和导航栏一样，Y 轴对齐
        mFAB.setTranslationY(verticalOffset);
        if (verticalOffset == 0) {          // 标识向下滚动到最底部
            showFloatingActionButton();
        } else if (verticalOffset < 0) {    // 标识向下滚动
            hideFloatingActionButton();
        }
    }

    // 悬浮按钮缩放显示
    private void showFloatingActionButton() {
        mFAB.animate().scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();
        mFAB.setClickable(true);
    }

    // 悬浮按钮缩放隐藏
    private void hideFloatingActionButton() {
        mFAB.animate().scaleX(0f).scaleY(0f).setInterpolator(new AccelerateInterpolator()).start();
        mFAB.setClickable(false);
    }

    private void initViewPager(CourseDetailResponse courseDetailResponse, int comments) {
        // 多次订阅数据会导致重复,需要先进行清理
        fragments.clear();
        titles.clear();

        // 课程简介片段
        CourseIntroduceFragment courseIntroduceFragment = new CourseIntroduceFragment(courseDetailResponse);
        // 课程评论片段
        CourseCommentFragment courseCommentFragment = new CourseCommentFragment();
        // activity 向 fragment 传参
        Bundle bundle = new Bundle();
        bundle.putInt("course_id", courseDetailResponse.getCourse().getId());
        bundle.putString("course_author", courseDetailResponse.getCourse().getCourse_author());
        courseCommentFragment.setArguments(bundle);

        fragments.add(courseIntroduceFragment);
        fragments.add(courseCommentFragment);
        titles.add("简介");
        titles.add("评论(" + comments + ")");
        CommonFragmentStatePagerAdapter mAdapter = new CommonFragmentStatePagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mAdapter);
        // 设置预加载页面数量的方法，那就是setOffscreenPageLimit()
        mViewPager.setOffscreenPageLimit(2);
        mSlidingTabLayout.setViewPager(mViewPager);
        measureTabLayoutTextWidth(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                measureTabLayoutTextWidth(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void measureTabLayoutTextWidth(int position) {
        String title = titles.get(position);
        TextView titleView = mSlidingTabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        mSlidingTabLayout.setIndicatorWidth(textWidth / 3);
    }

    /**
     * onCreateOptionsMenu() 创建选项菜单 Menu
     * 1.选项菜单（optinosMenu）
     * 2.上下文菜单（ContextMenu）
     * 3.子菜单(subMenu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 如果当前Activity是没有主题的，即用的主题是：Theme.AppCompat.Light.NoActionBar
        //则需要自己先添加一个toolbar并通过setSupportActionBar方法将其设置成ActionBar
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }
}
