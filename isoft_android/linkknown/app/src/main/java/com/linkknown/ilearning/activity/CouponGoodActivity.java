package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CouponGoodAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.helper.SwipeRefreshLayoutHelper;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.CouponCourseResponse;
import com.linkknown.ilearning.model.CouponListResponse;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.model.PayShoppinpCartResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CouponGoodActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private Context mContext;
    private BaseQuickAdapter baseQuickAdapter;
    private Handler handler = new Handler();

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayoutHelper swipeRefreshLayoutHelper = new SwipeRefreshLayoutHelper();

    //点击券过来的参数
    private CouponListResponse.Coupon coupon;

    //商品集合 - 课程集合
    private List<CouponCourseResponse.Course> courseList = new ArrayList<CouponCourseResponse.Course>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_good);
        ButterKnife.bind(this);
        mContext = this;
        swipeRefreshLayoutHelper.bind(mContext, refreshLayout);
        swipeRefreshLayoutHelper.initStyle();
        swipeRefreshLayoutHelper.registerListener(() -> initData());

        initView();
        initData();
    }


    //初始化视图
    private void initView(){
        initToolBar(toolbar, true, "可以购买的课程");
        //获取传过来的参数
        Bundle bundle = getIntent().getBundleExtra("bundle");
        coupon = (CouponListResponse.Coupon)bundle.getSerializable("coupon");
    }


    private void initData() {
        loadPageData(1, Constants.DEFAULT_PAGE_SIZE2);
    }

    private void loadPageData(int current_page, int pageSize) {
        // 第一页不延时执行
        if (current_page == 1) {
            executeLoadPageData(current_page, pageSize);
        } else {
            // 后续页面，延迟执行，让加载效果更好
            handler.postDelayed(() -> executeLoadPageData(current_page, pageSize), 1000);
        }
    }


    public void executeLoadPageData(int current_page,int pageSize){
        if ("designated".equals(coupon.getCoupon_type())){
            if ("course".equals(coupon.getTarget_type())){
                Integer courseId = Integer.valueOf(coupon.getTarget_id());
                LinkKnownApiFactory.getLinkKnownApi().ShowCourseDetail(courseId,LoginUtil.getLoginUserName(mContext))
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<CourseDetailResponse>() {
                            @Override
                            public void onNext(CourseDetailResponse o) {
                                if (o.isSuccess()){
                                    courseList.clear();
                                    CourseDetailResponse.Course course_detail = o.getCourse();
                                    CouponCourseResponse.Course course = new CouponCourseResponse.Course();
                                    //将 CourseDetailResponse.Course 5个展示字段 赋值进入 CouponCourseResponse.Course
                                    course.setSmall_image(course_detail.getSmall_image());
                                    course.setCourse_name(course_detail.getCourse_name());
                                    course.setCourse_short_desc(course_detail.getCourse_short_desc());
                                    course.setCourse_number(course_detail.getCourse_number());
                                    course.setWatch_number(course_detail.getWatch_number());
                                    course.setPrice(course_detail.getPrice());
                                    course.setId(course_detail.getId());
                                    courseList.add(course);

                                    //创建 CouponGoodAdapter
                                    baseQuickAdapter = new CouponGoodAdapter(mContext,courseList);
                                    recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
                                    recyclerView.setAdapter(baseQuickAdapter);

                                }else{
                                    ToastUtil.showText(mContext,o.getErrorMsg());
                                }
                            };
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ShowCourseDetail error", e.getMessage());
                                ToastUtil.showText(mContext,"查询失败！");
                            }
                        });
            }

        }else if ("general".equals(coupon.getCoupon_type())){
            String youhui_type = coupon.getYouhui_type();
            String goods_min_amount = coupon.getGoods_min_amount();
            LinkKnownApiFactory.getLinkKnownApi().QueryGeneralCouponTargets(youhui_type,goods_min_amount,current_page,pageSize)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<CouponCourseResponse>() {
                        @Override
                        public void onNext(CouponCourseResponse o) {
                            if (o.isSuccess()){
                                courseList = o.getCourseList();

                                //创建 CouponGoodAdapter
                                baseQuickAdapter = new CouponGoodAdapter(mContext,courseList);
                                recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
                                recyclerView.setAdapter(baseQuickAdapter);

                            }else{
                                ToastUtil.showText(mContext,o.getErrorMsg());
                            }
                        };
                        @Override
                        public void onError(Throwable e) {
                            Log.e("ShowCourseDetail error", e.getMessage());
                            ToastUtil.showText(mContext,"查询失败！");
                        }
                    });
        }

    };

}
