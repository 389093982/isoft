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

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AboutUsActivity;
import com.linkknown.ilearning.activity.AdviseActivity;
import com.linkknown.ilearning.activity.CouponCenterActivity;
import com.linkknown.ilearning.activity.HuodongActivity;
import com.linkknown.ilearning.activity.KaoShiShijuanListActivity;
import com.linkknown.ilearning.activity.MessageInfoActivity;
import com.linkknown.ilearning.activity.PayOrderActivity;
import com.linkknown.ilearning.activity.SettingActivity;
import com.linkknown.ilearning.activity.ShoppingCartActivity;
import com.linkknown.ilearning.activity.UserDetailActivity;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.AnimationUtil;
import com.linkknown.ilearning.util.LoginUtil;
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


    //用户信息
    @BindView(R.id.userInfoLayout)
    public RelativeLayout userInfoLayout;
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
        menuAboutLayout.setOnClickListener(this);
        menuPersonalCenter.setOnClickListener(this);
        menuAdviseLayout.setOnClickListener(this);
        setup.setOnClickListener(this);
    }


    //铃铛
    private void initLingDang () {
        lingdang.setAnimation(AnimationUtil.getShakeAnimation(3));
        lingdang.setOnClickListener(v -> UIUtils.gotoActivity(mContext, MessageInfoActivity.class));
    }


    private void initMenuImageView () {
        // 点击调往对应页面
        iv_coupon.setOnClickListener(v -> UIUtils.gotoActivity(mContext, CouponCenterActivity.class));
        iv_order.setOnClickListener(v -> UIUtils.gotoActivity(mContext, PayOrderActivity.class));
        iv_shoppingCart.setOnClickListener(v -> UIUtils.gotoActivity(mContext, ShoppingCartActivity.class));
        iv_huodong.setOnClickListener(v -> UIUtils.gotoActivity(mContext, HuodongActivity.class));
        iv_kaoshi.setOnClickListener(v -> UIUtils.gotoActivity(mContext, KaoShiShijuanListActivity.class));
    }

    //查询用户信息
    private void initLoginView () {
        if (LoginUtil.checkHasLogin(mContext)) {
            userInfoLayout.setVisibility(View.VISIBLE);

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
                                userSignature.setText(StringUtils.isNotEmpty(user.getUser_signature()) ? user.getUser_signature() : "这家伙很懒，什么个性签名都没有留下");
                                attention_counts.setText(user.getAttention_counts()==0?"关注:0":"关注:"+user.getAttention_counts());
                                fensi_counts.setText(user.getFensi_counts()==0?"粉丝:0":"粉丝:"+user.getFensi_counts());
                            }
                        };

                        @Override
                        public void onError(Throwable e) {
                            Log.e("getUserDetail error", e.getMessage());
                            ToastUtil.showText(mContext,"查询用户信息失败！");
                        }
                    });

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

            case R.id.setup:
                UIUtils.gotoActivity(mContext, SettingActivity.class);
                break;
            // 菜单项点击事件
            case R.id.menuAboutLayout:
                UIUtils.gotoActivity(mContext, AboutUsActivity.class);
                break;
            case R.id.menuPersonalCenter:
                UIUtils.gotoActivity(mContext, UserDetailActivity.class);
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
        UIUtils.setTextViewDrawbleImg(mContext, menuAdviseTextView, R.drawable.ic_advise, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuPersonalCenterTextView, R.drawable.ic_personal_center, 0, 2, 40, 42);
        UIUtils.setTextViewDrawbleImg(mContext, menuAboutLayoutTextView, R.drawable.ic_about, 0, 2, 40, 42);
    }
}
