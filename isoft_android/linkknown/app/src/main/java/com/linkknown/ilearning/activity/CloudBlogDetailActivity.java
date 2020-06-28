package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.CommonFragmentStatePagerAdapter;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.fragment.FirstLevelCommentFragment;
import com.linkknown.ilearning.fragment.UserCourseFragment;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.BlogDetailResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudBlogDetailActivity extends BaseActivity {

    private Context mContext;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private String id_param;
    private String userName_param;
    private String headerIcon_param;
    private String userNameText_param;

    //查询出来的博客
    public BlogDetailResponse.Blog blog;

    //头像
    @BindView(R.id.headerIcon)
    public CircleImageView headerIcon;

    //博客标题
    @BindView(R.id.blog_title)
    public TextView blog_title;

    //用户名
    @BindView(R.id.userNameText)
    public TextView userNameText;

    //分类名称
    @BindView(R.id.catalog_name)
    public TextView catalog_name;

    //关注按钮
    @BindView(R.id.attention_off)
    public TextView attention_off;
    @BindView(R.id.attention_on)
    public TextView attention_on;

    //博客第一张图片
    @BindView(R.id.first_img)
    public ImageView first_img;

    //发布时间
    @BindView(R.id.createdTime)
    public TextView createdTime;

    //最后更新时间
    @BindView(R.id.lastUpdatedTime)
    public TextView lastUpdatedTime;

    //浏览次数
    @BindView(R.id.views)
    public TextView views;

    //评论数
    @BindView(R.id.comments)
    public TextView comments;

    @BindView(R.id.delete_blog_icon)
    public ImageView delete_blog_icon;

    //博客内容
    @BindView(R.id.content)
    public TextView content;

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.tab_layout)
    SlidingTabLayout tab_layout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_blog_detail);
        this.mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "博客详情");

        //设置顶部透明
        UIUtils.setTopTransparent(this);

        initView();
        initData();
    }


    private void initView() {
        id_param = getIntent().getStringExtra("id");
        userName_param = getIntent().getStringExtra("userName");
        headerIcon_param = getIntent().getStringExtra("headerIcon");
        userNameText_param = getIntent().getStringExtra("userNameText");
    }


    private void initData() {

        //查看是否已关注
        LinkKnownApiFactory.getLinkKnownApi().QueryIsAttention("user",userName_param)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<QueryIsAttentionResponse>() {
                    @Override
                    public void onNext(QueryIsAttentionResponse o) {
                        if ("SUCCESS".equals(o.getStatus())) {
                            if (o.getAttention_records()>0){
                                //已关注
                                attention_on.setVisibility(View.VISIBLE);
                                attention_off.setVisibility(View.GONE);
                            }else{
                                //未关注
                                attention_on.setVisibility(View.GONE);
                                if (LoginUtil.isLoginUserName(mContext,userName_param)){
                                    attention_off.setVisibility(View.GONE);
                                }else{
                                    attention_off.setVisibility(View.VISIBLE);
                                }

                            }
                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("QueryIsAttention error", e.getMessage());
                        ToastUtil.showText(mContext,"查询是否关注失败！");
                    }
                });


        //查询博客详情
        LinkKnownApiFactory.getLinkKnownApi().ShowBlogArticleDetail(id_param)
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new LinkKnownObserver<BlogDetailResponse>() {
                    @Override
                    public void onNext(BlogDetailResponse o) {
                        if ("SUCCESS".equals(o.getStatus())) {
                            blog = o.getBlog();
                            //头像
                            UIUtils.setImage(mContext,  headerIcon, headerIcon_param);
                            headerIcon.setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class,intent -> {
                                intent.putExtra(Constants.USER_NAME,blog.getAuthor());
                                return intent;
                            }));
                            //博客名称
                            blog_title.setText(blog.getBlog_title());
                            //昵称
                            userNameText.setText(userNameText_param);
                            userNameText.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
                            userNameText.setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class,intent -> {
                                intent.putExtra(Constants.USER_NAME,blog.getAuthor());
                                return intent;
                            }));
                            //分类名称
                            catalog_name.setText(blog.getCatalog_name());
                            //第一张图片
                            if (StringUtils.isNotEmpty(blog.getFirst_img())){
                                first_img.setVisibility(View.VISIBLE);
                                UIUtils.setImage(mContext,  first_img, blog.getFirst_img());
                            }else{
                                first_img.setVisibility(View.GONE);
                            }
                            //创建时间
                            createdTime.setText("发布于:"+DateUtil.formatPublishTime(blog.getCreated_time()));
                            //更新时间
                            lastUpdatedTime.setText("更新于:"+DateUtil.formatPublishTime(blog.getLast_updated_time()));
                            if (blog.getCreated_time().equals(blog.getLast_updated_time())){
                                lastUpdatedTime.setVisibility(View.GONE);
                            }else{
                                lastUpdatedTime.setVisibility(View.VISIBLE);
                            }
                            //浏览量、评论数、内容
                            views.setText(blog.getViews()+"");
                            comments.setText(blog.getComments()+"");
                            content.setText(blog.getContent());

                            //删除博客
                            if (LoginUtil.isLoginUserName(mContext,blog.getAuthor())){
                                delete_blog_icon.setVisibility(View.VISIBLE);
                                delete_blog_icon.setOnClickListener(v -> DeleteBlog(blog.getId()+""));
                            }else{
                                delete_blog_icon.setVisibility(View.GONE);
                            }

                            //这里初始化评论的fragment
                            initFragments();
                        }else{
                            ToastUtil.showText(mContext,o.getErrorMsg());
                        }
                    };

                    @Override
                    public void onError(Throwable e) {
                        Log.e("查询博客详情 error", e.getMessage());
                        ToastUtil.showText(mContext,"ShowBlogArticleDetail 失败！");
                    }
                });


        // +关注
        attention_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkKnownApiFactory.getLinkKnownApi().DoAttention("user",userName_param,"on")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if ("SUCCESS".equals(o.getStatus())) {
                                    ToastUtil.showText(mContext,"关注成功！");
                                    attention_off.setVisibility(View.GONE);
                                    attention_on.setVisibility(View.VISIBLE);
                                }else{
                                    ToastUtil.showText(mContext,o.getErrorMsg());
                                }
                            };

                            @Override
                            public void onError(Throwable e) {
                                Log.e("doAttention error", e.getMessage());
                                ToastUtil.showText(mContext,"关注失败！");
                            }
                        });
            }
        });

        // 取消关注
        attention_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkKnownApiFactory.getLinkKnownApi().DoAttention("user",userName_param,"off")
                        .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                        .subscribe(new LinkKnownObserver<BaseResponse>() {
                            @Override
                            public void onNext(BaseResponse o) {
                                if ("SUCCESS".equals(o.getStatus())) {
                                    ToastUtil.showText(mContext,"取消关注成功！");
                                    attention_off.setVisibility(View.VISIBLE);
                                    attention_on.setVisibility(View.GONE);
                                }else{
                                    ToastUtil.showText(mContext,o.getErrorMsg());
                                }
                            };

                            @Override
                            public void onError(Throwable e) {
                                Log.e("doAttention error", e.getMessage());
                                ToastUtil.showText(mContext,"取消关注失败！");
                            }
                        });
            }
        });



    }


    //删除博客
    public void DeleteBlog(String id){
        // 没登录,提示登录
        new XPopup.Builder(mContext)
                .hasShadowBg(true)
                .asConfirm("温馨提示", "您确认删除这篇博客？", () -> {
                    LinkKnownApiFactory.getLinkKnownApi().ArticleDelete(id)
                            .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                            .subscribe(new LinkKnownObserver<BaseResponse>() {
                                @Override
                                public void onNext(BaseResponse o) {
                                    if ("SUCCESS".equals(o.getStatus())){
                                        ToastUtil.showText(mContext,"删除成功");
                                        //返回上一页
                                        setResult(201,new Intent());
                                        finish();
                                    }else{
                                        ToastUtil.showText(mContext,o.getErrorMsg());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtil.showText(mContext,"系统异常");
                                }
                            });

                }).show();
    };


    public void initFragments(){
        // 多次订阅数据会导致重复,需要先进行清理
        fragments.clear();
        titles.clear();

        titles.add("评论");
        int theme_pk = blog.getId();
        String theme_type = "blog_theme_type";
        String comment_type = "comment";
        String refer_user_name = this.userName_param;
        String comments = blog.getComments()+"";
        FirstLevelCommentFragment firstLevelCommentFragment = new FirstLevelCommentFragment(theme_pk,theme_type,comment_type,refer_user_name,comments);
        fragments.add(firstLevelCommentFragment);

        CommonFragmentStatePagerAdapter mAdapter = new CommonFragmentStatePagerAdapter(getSupportFragmentManager(), fragments, titles);
        view_pager.setAdapter(mAdapter);
        // 设置预加载页面数量的方法，那就是setOffscreenPageLimit()
        view_pager.setOffscreenPageLimit(1);
        tab_layout.setViewPager(view_pager);
        measureTabLayoutTextWidth(0);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        TextView titleView = tab_layout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float textWidth = paint.measureText(title);
        tab_layout.setIndicatorWidth(textWidth / 3);
    }


}
