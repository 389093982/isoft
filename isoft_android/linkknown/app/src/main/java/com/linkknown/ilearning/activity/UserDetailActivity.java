package com.linkknown.ilearning.activity;

import android.annotation.SuppressLint;
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
import com.linkknown.ilearning.common.CommonFragmentStatePagerAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.UserCourseFragment;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.service.CourseService;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseActivity {

    private Context mContext;
    private String userName;

    //用户头像
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;

    //昵称
    @BindView(R.id.nickNameText)
    public TextView nickNameText;

    //vip等级
    @BindView(R.id.vipLevel)
    public ImageView vipLevel;

    //个性签名
    @BindView(R.id.userSignature)
    public TextView userSignature;

    //性别
    @BindView(R.id.genderView)
    public ImageView genderView;

    @BindView(R.id.attention_off)
    public TextView attention_off;
    @BindView(R.id.attention_on)
    public TextView attention_on;

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
        setContentView(R.layout.activity_user_detail);
        mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "");
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
                            if (o.isSuccess() && o.getAttention_records() > 0){
                                //大于0 则表示已关注
                                attention_off.setVisibility(View.GONE);
                                attention_on.setVisibility(View.VISIBLE);
                            }else{
                                attention_off.setVisibility(View.VISIBLE);
                                attention_on.setVisibility(View.GONE);
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
                                vipLevel.setImageResource(UIUtils.getVipLevelImageResource(user.getVip_level()));
                                userSignature.setText(StringUtils.isNotEmpty(user.getUser_signature()) ? user.getUser_signature() : "这家伙很懒，什么个性签名都没有留下");
                                genderView.setImageResource(UIUtils.getGenderImageResource(user.getGender()));
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
                LinkKnownApiFactory.getLinkKnownApi().DoAttention("user",userName,"on")
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
                LinkKnownApiFactory.getLinkKnownApi().DoAttention("user",userName,"off")
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

        if (LoginUtil.isLoginUserName(getApplicationContext(), userName)) {
            UserCourseFragment fragment2 = new UserCourseFragment(UserCourseFragment.DISPLAY_TYPE_FAVORITE);
            fragments.add(fragment2);
            fragment2.setArguments(bundle);
            titles.add("收藏的课程");

            UserCourseFragment fragment3 = new UserCourseFragment(UserCourseFragment.DISPLAY_TYPE_VIEWED);
            fragments.add(fragment3);
            fragment3.setArguments(bundle);
            titles.add("观看的课程");
        }

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
