package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AboutUsActivity;
import com.linkknown.ilearning.activity.AdviseActivity;
import com.linkknown.ilearning.activity.CouponCenterActivity;
import com.linkknown.ilearning.activity.CourseOrderActivity;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.activity.MessageInfoActivity;
import com.linkknown.ilearning.activity.SettingActivity;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.iv_coupon)
    public ImageView iv_coupon;
    @BindView(R.id.iv_order)
    public ImageView iv_order;
    @BindView(R.id.iv_message)
    public ImageView iv_message;
    @BindView(R.id.iv_huodong)
    public ImageView iv_huodong;

    // 登录用户头像和用户名
    @BindView(R.id.small_headerIcon)
    public ImageView small_headerIcon;
    @BindView(R.id.big_headerIcon)
    public ImageView big_headerIcon;

    //购物车
    @BindView(R.id.menuShoppingCart)
    public LinearLayout menuShoppingCart;
    @BindView(R.id.menuShoppingCartTextView)
    public TextView menuShoppingCartTextView;

    //我的订单
    @BindView(R.id.menuOrderLayout)
    public LinearLayout menuOrderLayout;
    @BindView(R.id.menuOrderTextView)
    public TextView menuOrderTextView;

    //我要吐槽(提出意见)
    @BindView(R.id.menuAdviseLayout)
    public LinearLayout menuAdviseLayout;
    @BindView(R.id.menuAdviseTextView)
    public TextView menuAdviseTextView;

    //个人中心
    @BindView(R.id.menuPersonalCenter)
    public LinearLayout menuPersonalCenter;
    @BindView(R.id.menuPersonalCenterTextView)
    public TextView menuPersonalCenterTextView;

    //关于
    @BindView(R.id.menuAboutLayout)
    public LinearLayout menuAboutLayout;
    @BindView(R.id.menuAboutLayoutTextView)
    public TextView menuAboutLayoutTextView;

    //检查更新
    @BindView(R.id.menuCheckUpdate)
    public LinearLayout menuCheckUpdate;
    @BindView(R.id.menuCheckUpdateTextView)
    public TextView menuCheckUpdateTextView;

    //设置
    @BindView(R.id.menuSettingLayout)
    public LinearLayout menuSettingLayout;
    @BindView(R.id.menuSettingLayoutTextView)
    public TextView menuSettingLayoutTextView;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        //        if (DEV_MODE) {
//            // 严格模式的开启可以放在Application或者Activity以及其他组件的onCreate方法
//            // 可以用来帮助开发者发现代码中的一些不规范的问题，以达到提升应用响应能力的目的
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            builder.detectFileUriExposure();
//        }
        bindMenuDrawnStart();

        initAppBar();

        // 初始化登录相关信息
        initLoginInfo();

        // 中间图标入口
        initMenuImageView();

        //设置点击事件
        menuAboutLayout.setOnClickListener(this);
        menuOrderLayout.setOnClickListener(this);
        menuPersonalCenter.setOnClickListener(this);
        menuShoppingCart.setOnClickListener(this);
        menuAdviseLayout.setOnClickListener(this);
        menuSettingLayout.setOnClickListener(this);
    }

    private void initMenuImageView () {
        // 点击调往对应页面
        iv_coupon.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CouponCenterActivity.class));
        iv_order.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseOrderActivity.class));
        iv_message.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MessageInfoActivity.class));
        iv_huodong.setOnClickListener(v -> UIUtils.gotoActivity(mContext, HuodongActivity.class));
    }

    private void initLoginInfo () {
        LiveEventBus.get("loginUserResponse", LoginUserResponse.class).observe(this, loginUserResponse -> {
            if (StringUtils.isEmpty(loginUserResponse.getErrorMsg()) && StringUtils.isNotEmpty(loginUserResponse.getUserName())) {
                small_headerIcon.setVisibility(View.VISIBLE);
                // 登录成功
                Glide.with(getContext())
                        .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                        .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                        .into(small_headerIcon);
                Glide.with(getContext())
                        .load(UIUtils.replaceMediaUrl(loginUserResponse.getHeaderIcon()))
                        .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                        .into(big_headerIcon);
            } else {
                // 登录失败
                small_headerIcon.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 跳转到新的界面
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuAboutLayout:
                UIUtils.gotoActivity(mContext, AboutUsActivity.class);
                break;
            case R.id.menuSettingLayout:
                UIUtils.gotoActivity(mContext, SettingActivity.class);
                break;
            case R.id.menuOrderLayout:
                UIUtils.gotoActivity(mContext, CourseOrderActivity.class);
                break;
            case R.id.menuPersonalCenter:
                UIUtils.gotoActivity(mContext, UserDetailActivity.class);
                break;
            case R.id.menuShoppingCart:
                UIUtils.gotoActivity(mContext, ShoppingCartActivity.class);
                break;
            case R.id.menuAdviseLayout:
                UIUtils.gotoActivity(mContext, AdviseActivity.class);
                break;
            default:
                break;
        }
    }

    @BindView(R.id.appbar)
    public AppBarLayout appbar;
    @BindView(R.id.collapsing_toolbar)
    public CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.minePageToolBar)
    public Toolbar minePageToolBar;

    private CollapsingToolbarLayoutState state;
    // 先写一个枚举定义出CollapsingToolbarLayout展开、折叠、中间，这三种状态。
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private void initAppBar () {
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                    collapsing_toolbar.setTitle("EXPANDED");//设置title为EXPANDED
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                    collapsing_toolbar.setTitle("");//设置title不显示
                    minePageToolBar.setVisibility(View.VISIBLE);//隐藏 toolbar
                    state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                }
            } else {
                if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if(state == CollapsingToolbarLayoutState.COLLAPSED){
                        minePageToolBar.setVisibility(View.GONE);//由折叠变为中间状态时隐藏 toolbar
                    }
                    collapsing_toolbar.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                    state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                }
            }
        });
    }

    /**
     * 菜单前面添加图标
     */
    public void bindMenuDrawnStart(){
        UIUtils.setTextViewDrawbleImg(mContext, menuShoppingCartTextView, R.drawable.ic_shopping_cart, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuOrderTextView, R.drawable.ic_order2, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuAdviseTextView, R.drawable.ic_advise, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuPersonalCenterTextView, R.drawable.ic_personal_center, 0, 2, 40, 42);

        UIUtils.setTextViewDrawbleImg(mContext, menuAboutLayoutTextView, R.drawable.ic_about, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuCheckUpdateTextView, R.drawable.ic_check_update, 0, 2, 40, 42);

        UIUtils.setTextViewDrawbleImg(mContext, menuSettingLayoutTextView, R.drawable.ic_setup, 0, 2, 40, 42);
    }


}
