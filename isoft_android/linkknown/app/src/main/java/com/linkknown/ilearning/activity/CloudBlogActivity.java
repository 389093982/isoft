package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.GlideImageLoader;
import com.linkknown.ilearning.common.CommonFragmentStatePagerAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.CloudBlogFragment;
import com.linkknown.ilearning.fragment.UserCourseFragment;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.model.UserDetailResponse;
import com.linkknown.ilearning.util.CommonUtil;
import com.linkknown.ilearning.util.KeyBoardUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudBlogActivity extends BaseActivity {

    private Context mContext;
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

    //添加博客
    @BindView(R.id.add_blog)
    public ImageView add_blog;

    //搜索博客
    @BindView(R.id.searchBlogTextView)
    public EditText searchBlogTextView;
    @BindView(R.id.searchBlogEditTextClear)
    public ImageView searchBlogEditTextClear;
    @BindView(R.id.searchBtn)
    public ImageView searchBtn;

    //两个fragment
    public CloudBlogFragment fragment_cloud_blog;
    public CloudBlogFragment fragment_my_blog;

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
        setContentView(R.layout.activity_cloud_blog);
        mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "云博客");

        //顶部设置为透明
        UIUtils.setTopTransparent(this);

        //设置banner
        initBannerView();

        initView();

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
        //查看用户信息，设置昵称
        getUserDetail();

        //添加博客
        add_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,EditCloudBlogActivity.class);
                //这里 intent 无需传参数
                startActivityForResult(intent,199);
            }
        });


        // 有内容显示 clear 图标,没有则隐藏
        RxTextView.textChanges(searchBlogTextView).map(CharSequence::toString).subscribe(s -> {
            if (!TextUtils.isEmpty(s)) {
                searchBlogEditTextClear.setVisibility(View.VISIBLE);
            } else {
                searchBlogEditTextClear.setVisibility(View.GONE);
                //为了用户体验
                if (fragment_cloud_blog!=null){
                    fragment_cloud_blog.doSearch(searchBlogTextView.getText().toString().trim());
                }
                if (fragment_my_blog!=null){
                    fragment_my_blog.doSearch(searchBlogTextView.getText().toString().trim());
                }
                //关闭软键盘
                KeyBoardUtil.closeKeybord(searchBlogTextView, mContext);
            }
        });

        // 清空输入框
        RxView.clicks(searchBlogEditTextClear).subscribe(aVoid -> {
            searchBlogTextView.setText("");
            fragment_cloud_blog.doSearch(searchBlogTextView.getText().toString().trim());
            fragment_my_blog.doSearch(searchBlogTextView.getText().toString().trim());
            //关闭软键盘
            KeyBoardUtil.closeKeybord(searchBlogTextView, mContext);
        });

        // 软键盘搜索
        searchBlogTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (StringUtilEx.isAllNotEmpty(searchBlogTextView.getText().toString())){
                        //查询
                        fragment_cloud_blog.doSearch(searchBlogTextView.getText().toString().trim());
                        fragment_my_blog.doSearch(searchBlogTextView.getText().toString().trim());
                        //关闭软键盘
                        KeyBoardUtil.closeKeybord(searchBlogTextView, mContext);
                    }else{
                        ToastUtil.showText(mContext,"请输入查询内容");
                    }
                    return true;
                }
                return false;
            }
        });

        //查询博客
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtilEx.isAllNotEmpty(searchBlogTextView.getText().toString())){
                    fragment_cloud_blog.doSearch(searchBlogTextView.getText().toString().trim());
                    fragment_my_blog.doSearch(searchBlogTextView.getText().toString().trim());
                    //关闭软键盘
                    KeyBoardUtil.closeKeybord(searchBlogTextView, mContext);
                }else{
                    ToastUtil.showText(mContext,"请输入查询内容");
                }
            }
        });
    }


    private void initFragments(){
        // 多次订阅数据会导致重复,需要先进行清理
        fragments.clear();
        titles.clear();

        titles.add("云博客");
        fragment_cloud_blog = new CloudBlogFragment(CloudBlogFragment.SCOP_ALL);
        fragments.add(fragment_cloud_blog);

        titles.add("我的博客");
        fragment_my_blog = new CloudBlogFragment(CloudBlogFragment.SCOP_MYSELF);
        fragments.add(fragment_my_blog);

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
        mSlidingTabLayout.setIndicatorWidth(textWidth / 2);
    }


    //查看用户信息
    public void getUserDetail(){
        //查看基本信息
        LinkKnownApiFactory.getLinkKnownApi().getUserDetail(LoginUtil.getLoginUserName(mContext))
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
                                //初始化两个fragment
                                initFragments();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onNext =>", "系统异常,请联系管理员~");
                        ToastUtil.showText(getApplication(), "系统异常,请联系管理员~");
                    }
                });
    };




    // 为了从 “发表博客” 回来 “本页面” 做个刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199 && resultCode == 200) {
            Bundle bundle = data.getBundleExtra("bundle");
            String status = (String) bundle.getSerializable("status");
            if ("SUCCESS".equals(status)){
                //就为了做个刷新
                fragment_cloud_blog.doSearch("");
                fragment_my_blog.doSearch("");
            }
        }else if(resultCode == 201){
            //就为了做个刷新
            fragment_cloud_blog.doSearch("");
            fragment_my_blog.doSearch("");
        }
    }

}
