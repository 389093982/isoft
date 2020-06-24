package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.common.CommonFragmentStatePagerAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.UserCourseFragment;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PersonalCenterActivity extends BaseActivity {

    private Context mContext;
    private String userName;
    // 轮播图部分
    @BindView(R.id.banner)
    public Banner banner;
    // 存放轮播图所有图片
    private ArrayList<Integer> bannerImageList;

    //用户头像
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;

    //昵称
    @BindView(R.id.nickNameText)
    public TextView nickNameText;

    //性别
    @BindView(R.id.genderView)
    public ImageView genderView;

    //积分
    @BindView(R.id.userPoint)
    public TextView userPoint;

    //关注&粉丝
    @BindView(R.id.attention_counts)
    public TextView attention_counts;
    @BindView(R.id.fensi_counts)
    public TextView fensi_counts;

    //关注按钮
    @BindView(R.id.attention_off)
    public TextView attention_off;
    @BindView(R.id.attention_on)
    public TextView attention_on;

    //个性签名
    @BindView(R.id.userSignature)
    public TextView userSignature;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "");

        //顶部设置为透明
        UIUtils.setTopTransparent(this);

        //设置banner
        initBannerView();

        //绑定关注事件
        initAttentionBtn();

        // 查看别人 userName会传值， 如果userName为空，那就是查看自己的。 （查看自己不传这个参数）
        userName = getIntent().getStringExtra(Constants.USER_NAME);
        if (StringUtils.isEmpty(userName)) {
            //查看自己
            if (!LoginUtil.checkHasLogin(mContext)){
                // 全局对话框登录拦截
                LoginUtil.showLoginOrAutoLoginDialog(mContext);
            }else{
                userName = LoginUtil.getLoginUserName(mContext);
                //查看自己
                initView();
            }
        }else {
            //查看别人
            initView();
        }
    }

    public void initBannerView(){
        bannerImageList = new ArrayList<>();
        bannerImageList.add(R.drawable.personal_center_img01);
        bannerImageList.add(R.drawable.personal_center_img02);
        bannerImageList.add(R.drawable.personal_center_img03);
        bannerImageList.add(R.drawable.personal_center_img04);

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

        banner.setImages(bannerImageList).start();
    };

    private void initView () {
        //关注按钮的显示与隐藏
        if (LoginUtil.isLoginUserName(getApplicationContext(), userName)){
            attention_off.setVisibility(View.GONE);
            attention_on.setVisibility(View.GONE);
        }else{
            LinkKnownApiFactory.getLinkKnownApi().QueryIsAttention("user",userName)
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<QueryIsAttentionResponse>() {
                        @Override
                        public void onNext(QueryIsAttentionResponse o) {
                            if (o.isSuccess()){
                                if (o.getAttention_records() > 0){
                                    //已关注， 显示已关注按钮
                                    attention_on.setVisibility(View.VISIBLE);
                                    attention_off.setVisibility(View.GONE);
                                }else{
                                    //未关注， 显示 +关注 按钮
                                    attention_off.setVisibility(View.VISIBLE);
                                    attention_on.setVisibility(View.GONE);
                                }
                            }else{
                                ToastUtil.showText(mContext,"查询是否关注失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {}
                    });
        }

        //查看基本信息
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(userName)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {
                        if (userDetailResponse.isSuccess()){
                            UserDetailResponse.User user = userDetailResponse.getUser();
                            if (user!=null){
                                // 设置用户头像、用户昵称、用户会员等级、用户标签语
                                UIUtils.setImage(getApplication(), headerIcon, user.getSmall_icon());
                                nickNameText.setText(user.getNick_name());
                                genderView.setImageResource(UIUtils.getGenderImageResource(user.getGender()));
                                userPoint.setText(user.getUser_points()==0?"积分:0":"积分:"+user.getUser_points());
                                attention_counts.setText(user.getAttention_counts()==0?"关注:0":"关注:"+user.getAttention_counts());
                                fensi_counts.setText(user.getFensi_counts()==0?"粉丝:0":"粉丝:"+user.getFensi_counts());
                                userSignature.setText(StringUtils.isNotEmpty(user.getUser_signature()) ? user.getUser_signature() : "这家伙很懒，什么个性签名都没有留下");
                                initFragments(user.getUser_name());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onNext =>", "系统异常,请联系管理员~");
                        ToastUtil.showText(getApplication(), "系统异常,请联系管理员~");
                    }
                });
    }


    //关注点击事件
    public void initAttentionBtn(){
        attention_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginUtil.checkHasLogin(mContext)){
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                    return;
                }
                LinkKnownApiFactory.getLinkKnownApi().doAttention("user",userName,"on")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if (o.isSuccess()){
                                    ToastUtil.showText(mContext,"关注成功");
                                    attention_off.setVisibility(View.GONE);
                                    attention_on.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showText(mContext,"系统异常");
                            }
                        });
            }
        });
        attention_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginUtil.checkHasLogin(mContext)){
                    LoginUtil.showLoginOrAutoLoginDialog(mContext);
                    return;
                }
                LinkKnownApiFactory.getLinkKnownApi().doAttention("user",userName,"off")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if (o.isSuccess()){
                                    ToastUtil.showText(mContext,"取消成功");
                                    attention_off.setVisibility(View.VISIBLE);
                                    attention_on.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showText(mContext,"系统异常");
                            }
                        });
            }
        });
    };


    private void initFragments(String userName){
        // 多次订阅数据会导致重复,需要先进行清理
        fragments.clear();
        titles.clear();

        // activity 向 fragment 传参
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_NAME, userName);

        UserCourseFragment fragment1 = new UserCourseFragment(UserCourseFragment.DISPLAY_TYPE_NEW);
        fragments.add(fragment1);
        fragment1.setArguments(bundle);
        titles.add("发布的课程");

        UserCourseFragment fragment2 = new UserCourseFragment(UserCourseFragment.DISPLAY_TYPE_FAVORITE);
        fragments.add(fragment2);
        fragment2.setArguments(bundle);
        titles.add("收藏的课程");

        UserCourseFragment fragment3 = new UserCourseFragment(UserCourseFragment.DISPLAY_TYPE_VIEWED);
        fragments.add(fragment3);
        fragment3.setArguments(bundle);
        titles.add("观看的课程");

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

}
