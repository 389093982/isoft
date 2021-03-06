package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AboutUsActivity;
import com.linkknown.ilearning.activity.AdviseActivity;
import com.linkknown.ilearning.activity.CloudBlogActivity;
import com.linkknown.ilearning.activity.BoughtCourseActivity;
import com.linkknown.ilearning.activity.LinkknownWithMeActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.MyCouponActivity;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.activity.KaoShiShijuanListActivity;
import com.linkknown.ilearning.activity.MessageInfoActivity;
import com.linkknown.ilearning.activity.PayOrderActivity;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.activity.SettingActivity;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.UserAttentionListActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.AnimationUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MineFragment extends Fragment implements View.OnClickListener {

    //设置
    @BindView(R.id.setup)
    ImageView setup;
    // 铃铛(消息)
    @BindView(R.id.lingdang)
    ImageView lingdang;

    //头像
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    //昵称
    @BindView(R.id.nickName)
    public TextView nickName;
    //积分
    @BindView(R.id.userPoint)
    public TextView userPoint;
    //个性签名
    @BindView(R.id.userSignature)
    public TextView userSignature;
    //关注
    @BindView(R.id.attention_counts)
    public TextView attention_counts;
    //粉丝
    @BindView(R.id.fensi_counts)
    public TextView fensi_counts;


    //优惠券
    @BindView(R.id.iv_coupon)
    public ImageView iv_coupon;
    //订单
    @BindView(R.id.iv_order)
    public ImageView iv_order;
    //购物车
    @BindView(R.id.iv_shoppingCart)
    public ImageView iv_shoppingCart;
    //活动中心
    @BindView(R.id.iv_huodong)
    public ImageView iv_huodong;
    //考试
    @BindView(R.id.iv_kaoshi)
    public ImageView iv_kaoshi;


    //已登录用户布局
    @BindView(R.id.userInfoLayout)
    public RelativeLayout userInfoLayout;

    //未登录用户布局
    @BindView(R.id.unLoginLayout)
    public RelativeLayout unLoginLayout;
    //前去登录按钮
    @BindView(R.id.toLoginView)
    public TextView toLoginView;

    //云博客
    @BindView(R.id.menuCloudBlogLayout)
    public LinearLayout menuCloudBlogLayout;
    @BindView(R.id.menuCloudBlog)
    public TextView menuCloudBlog;
    //个人中心
    @BindView(R.id.menuPersonalCenterLayout)
    public LinearLayout menuPersonalCenterLayout;
    @BindView(R.id.menuPersonalCenter)
    public TextView menuPersonalCenter;
    //已购课程
    @BindView(R.id.menuBoughtCourseLayout)
    public LinearLayout menuBoughtCourseLayout;
    @BindView(R.id.menuBoughtCourse)
    public TextView menuBoughtCourse;
    //我要吐槽(提出意见)
    @BindView(R.id.menuAdviseLayout)
    public LinearLayout menuAdviseLayout;
    @BindView(R.id.menuAdvise)
    public TextView menuAdvise;
    //关于
    @BindView(R.id.menuAboutLayout)
    public LinearLayout menuAboutLayout;
    @BindView(R.id.menuAbout)
    public TextView menuAbout;
    //我与链知
    @BindView(R.id.menuLinkKnownWithMeLayout)
    public LinearLayout menuLinkKnownWithMeLayout;
    @BindView(R.id.menuLinkKnownWithMe)
    public TextView menuLinkKnownWithMe;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        initView();
        initLingDang();

        return rootView;
    }

    private void initView() {
        // 查询用户信息
        initLoginView();

        //菜单前面添加图标
        bindMenuDrawnStart();

        // 中间图标入口
        initMenuImageView();

        //设置点击事件

        menuCloudBlogLayout.setOnClickListener(this);
        menuPersonalCenterLayout.setOnClickListener(this);
        menuBoughtCourseLayout.setOnClickListener(this);
        menuAdviseLayout.setOnClickListener(this);
        menuAboutLayout.setOnClickListener(this);
        setup.setOnClickListener(this);
        menuLinkKnownWithMeLayout.setOnClickListener(this);
    }


    //铃铛
    private void initLingDang () {
        lingdang.setAnimation(AnimationUtil.getShakeAnimation(3));
        lingdang.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MessageInfoActivity.class));
    }


    private void initMenuImageView () {
        // 点击调往对应页面
        iv_coupon.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MyCouponActivity.class));
        iv_order.setOnClickListener(v -> UIUtils.gotoActivity(mContext, PayOrderActivity.class));
        iv_shoppingCart.setOnClickListener(v -> UIUtils.gotoActivity(mContext, ShoppingCartActivity.class));
        iv_huodong.setOnClickListener(v -> UIUtils.gotoActivity(mContext, HuodongActivity.class));
        iv_kaoshi.setOnClickListener(v -> UIUtils.gotoActivity(mContext, KaoShiShijuanListActivity.class));
    }

    //查询用户信息
    private void initLoginView () {
        initLoginData();

        // 从自动登录响应结果中订阅登录信息
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observeSticky(this, loginUserResponse -> {
            if (loginUserResponse != null){
                if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                    // 登录操作
                    initLoginData();
                } else {
                    // 登出操作
                    userInfoLayout.setVisibility(View.GONE);
                    unLoginLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // 点击登录/注册按钮调往登录页面
        toLoginView.setOnClickListener(v -> UIUtils.gotoActivity(mContext, LoginActivity.class));
    }

    private void initLoginData() {
        if (LoginUtil.checkHasLogin(mContext)) {
            userInfoLayout.setVisibility(View.VISIBLE);
            unLoginLayout.setVisibility(View.GONE);

            LinkKnownApiFactory.getLinkKnownApi().getUserDetail(LoginUtil.getLoginUserName(mContext))
                    .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                    .subscribe(new LinkKnownObserver<UserDetailResponse>() {
                        @Override
                        public void onNext(UserDetailResponse userDetailResponse) {
                            if (userDetailResponse.isSuccess()){
                                UserDetailResponse.User user = userDetailResponse.getUser();
                                // 展示用户头像、用户名、积分和个性签名
                                UIUtils.setImage(mContext, headerIcon, LoginUtil.getHeaderIcon(mContext));
                                nickName.setText(LoginUtil.getLoginNickName(mContext));
                                userPoint.setText(String.format(Locale.getDefault(), "积分 %d", user.getUser_points()));
                                userSignature.setText(StringUtilEx.getFirstNotEmptyStr(user.getUser_signature(), Constants.DEFAULT_USER_SIGNATURE));
                                attention_counts.setText(user.getAttention_counts()==0?"关注:0":"关注:"+user.getAttention_counts());
                                fensi_counts.setText(user.getFensi_counts()==0?"粉丝:0":"粉丝:"+user.getFensi_counts());

                                attention_counts.setOnClickListener(v -> UIUtils.gotoActivity(mContext, UserAttentionListActivity.class, intent -> {
                                    intent.putExtra(UserAttentionListActivity.ATTENTION_TYPE, UserAttentionListActivity.ATTENTION);
                                    return intent;
                                }));
                                fensi_counts.setOnClickListener(v -> UIUtils.gotoActivity(mContext, UserAttentionListActivity.class, intent -> {
                                    intent.putExtra(UserAttentionListActivity.ATTENTION_TYPE, UserAttentionListActivity.FEN_SI);
                                    return intent;
                                }));
                            }
                        };

                        @Override
                        public void onError(Throwable e) {
                            Log.e("getUserDetail error", e.getMessage());
                            ToastUtil.showText(mContext,"查询用户信息失败！");

                            // 接口查询失败也展示未登录
                            userInfoLayout.setVisibility(View.GONE);
                            unLoginLayout.setVisibility(View.VISIBLE);
                        }
                    });

        } else {
            userInfoLayout.setVisibility(View.GONE);
            unLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 跳转到新的界面
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setup:
                UIUtils.gotoActivity(mContext, SettingActivity.class);
                break;
            // 菜单项点击事件
            case R.id.menuCloudBlogLayout:
                UIUtils.gotoActivity(mContext, CloudBlogActivity.class);
                break;
            case R.id.menuPersonalCenterLayout:
                UIUtils.gotoActivity(mContext, PersonalCenterActivity.class);
                break;
            case R.id.menuBoughtCourseLayout:
                UIUtils.gotoActivity(mContext, BoughtCourseActivity.class);
                break;
            case R.id.menuAdviseLayout:
                UIUtils.gotoActivity(mContext, AdviseActivity.class);
                break;
            case R.id.menuAboutLayout:
                UIUtils.gotoActivity(mContext, AboutUsActivity.class);
                break;
            case R.id.menuLinkKnownWithMeLayout:
                UIUtils.gotoActivity(mContext, LinkknownWithMeActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 菜单前面添加图标
     */
    public void bindMenuDrawnStart() {
        UIUtils.setTextViewDrawbleImg(mContext, menuCloudBlog, R.drawable.ic_blog_park, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuPersonalCenter, R.drawable.ic_personal_center, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuBoughtCourse, R.drawable.ic_course, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuAdvise, R.drawable.ic_advise, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuAbout, R.drawable.ic_about, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuLinkKnownWithMe, R.drawable.ic_link, 0, 2, 40, 42);
    }
}
