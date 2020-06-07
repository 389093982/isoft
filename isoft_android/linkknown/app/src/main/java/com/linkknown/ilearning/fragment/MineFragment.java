package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
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

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AboutUsActivity;
import com.linkknown.ilearning.activity.AdviseActivity;
import com.linkknown.ilearning.activity.CouponCenterActivity;
import com.linkknown.ilearning.activity.CourseOrderActivity;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.activity.MessageInfoActivity;
import com.linkknown.ilearning.activity.SettingActivity;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.banner)
    public Banner banner;

    @BindView(R.id.iv_coupon)
    public ImageView iv_coupon;
    @BindView(R.id.iv_order)
    public ImageView iv_order;
    @BindView(R.id.iv_message)
    public ImageView iv_message;
    @BindView(R.id.iv_huodong)
    public ImageView iv_huodong;

    @BindView(R.id.userInfoLayout)
    public RelativeLayout userInfoLayout;
    // 登录用户头像和用户名
    @BindView(R.id.headerIcon)
    public ImageView headerIcon;
    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.userPoint)
    public TextView userPoint;
    @BindView(R.id.userSignature)
    public TextView userSignature;

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

        initBanner();

        bindMenuDrawnStart();

        // 初始化登录相关信息
        initLoginView();

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

    // 初始化顶部轮播图组件
    private void initBanner() {
        banner.clearAnimation();
        //设置 banner 样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        // 存放轮播图所有图片
        ArrayList<Integer> bannerImageList = new ArrayList<>();
        ArrayList<String> bannerTitleList = new ArrayList<>();
        //清空旧数据
        bannerImageList.clear();
        bannerTitleList.clear();
        bannerImageList.add(R.drawable.banner_biancheng);
        bannerImageList.add(R.drawable.banner_shaoer);
        bannerTitleList.add("编程思维");
        bannerTitleList.add("少儿编程");
        banner.setImages(bannerImageList);
        banner.setBannerTitles(bannerTitleList);
        banner.start();
    }

    private void initMenuImageView () {
        // 点击调往对应页面
        iv_coupon.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CouponCenterActivity.class));
        iv_order.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CourseOrderActivity.class));
        iv_message.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MessageInfoActivity.class));
        iv_huodong.setOnClickListener(v -> UIUtils.gotoActivity(mContext, HuodongActivity.class));
    }

    private void initLoginView () {
        if (LoginUtil.checkHasLogin(mContext)) {
            userInfoLayout.setVisibility(View.VISIBLE);
            UserDetailResponse.User user = LoginUtil.getLoginUserInfo(mContext).getUserDetailResponse().getUser();
            // 展示用户头像、用户名、积分和个性签名
            UIUtils.setImage(mContext, headerIcon, LoginUtil.getHeaderIcon(mContext));
            userName.setText("学友" + LoginUtil.getLoginNickName(mContext));
            userPoint.setText(String.format(Locale.getDefault(), "积分 %d", user.getUser_points()));
            userSignature.setText(StringUtils.isNotEmpty(user.getUser_signature()) ? user.getUser_signature() : "这家伙很懒，什么个性签名都没有留下");
        } else {
            userInfoLayout.setVisibility(View.GONE);
        }
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

    /**
     * 菜单前面添加图标
     */
    public void bindMenuDrawnStart() {
        UIUtils.setTextViewDrawbleImg(mContext, menuShoppingCartTextView, R.drawable.ic_shopping_cart, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuOrderTextView, R.drawable.ic_order2, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuAdviseTextView, R.drawable.ic_advise, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuPersonalCenterTextView, R.drawable.ic_personal_center, 0, 2, 40, 42);

        UIUtils.setTextViewDrawbleImg(mContext, menuAboutLayoutTextView, R.drawable.ic_about, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuCheckUpdateTextView, R.drawable.ic_check_update, 0, 2, 40, 42);

        UIUtils.setTextViewDrawbleImg(mContext, menuSettingLayoutTextView, R.drawable.ic_setup, 0, 2, 40, 42);
    }
}
