package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
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
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseActivity {

    private Context mContext;
    private String userName;

    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    @BindView(R.id.nickNameText)
    public TextView nickNameText;
    @BindView(R.id.vipLevel)
    public ImageView vipLevel;
    @BindView(R.id.userSignature)
    public TextView userSignature;
    @BindView(R.id.genderView)
    public ImageView genderView;
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

        // 有用户名则是查看第三方用户
        userName = getIntent().getStringExtra(Constants.USER_NAME);
        if (StringUtils.isEmpty(userName) && !LoginUtil.checkHasLogin(mContext)) {
            // 全局对话框登录拦截
            LoginUtil.showLoginOrAutoLoginDialog(mContext, new LoginUtil.ConfirmDialogCallback() {
                @Override
                public void onPositive() {
                    UIUtils.gotoActivity(mContext, LoginActivity.class);
                    finish();
                }

                @Override
                public void onNegative() {
                    finish();
                }
            });
        } else {
            userName = LoginUtil.getLoginUserName(mContext);
            initView();
        }
    }

    private void initView () {
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
