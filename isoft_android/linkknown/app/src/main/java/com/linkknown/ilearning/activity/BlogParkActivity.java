package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class BlogParkActivity extends BaseActivity {

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

    //关注按钮
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_park);
        mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "博客园");

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
                                // 设置用户头像、用户昵称
                                UIUtils.setImage(getApplication(), headerIcon, user.getSmall_icon());
                                nickNameText.setText(user.getNick_name());
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



}
