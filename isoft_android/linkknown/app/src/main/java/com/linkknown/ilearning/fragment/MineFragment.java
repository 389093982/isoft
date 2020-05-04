package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.linkknown.ilearning.manager.UserServiceManager;
import com.linkknown.ilearning.model.LoginUserResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment implements View.OnClickListener {

    // 登录用户头像和用户名
    @BindView(R.id.small_headerIcon)
    public ImageView small_headerIcon;
    @BindView(R.id.big_headerIcon)
    public ImageView big_headerIcon;
//    @BindView(R.id.cat_title)
//    public TextView cat_title;

    @BindView(R.id.menuAboutLayout)
    public LinearLayout menuAboutLayout;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

//        if (DEV_MODE) {
//            // 严格模式的开启可以放在Application或者Activity以及其他组件的onCreate方法
//            // 可以用来帮助开发者发现代码中的一些不规范的问题，以达到提升应用响应能力的目的
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            builder.detectFileUriExposure();
//        }

        initAppBar();

        // 初始化登录相关信息
        initLoginInfo();

        menuAboutLayout.setOnClickListener(this);

        return rootView;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuAboutLayout:
                UIUtils.gotoActivity(mContext, AboutUsActivity.class);
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
}
