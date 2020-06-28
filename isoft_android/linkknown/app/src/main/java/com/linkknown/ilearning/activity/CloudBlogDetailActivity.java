package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.common.LinkKnownObserver;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.BaseResponse;
import com.linkknown.ilearning.model.BlogDetailResponse;
import com.linkknown.ilearning.model.QueryIsAttentionResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

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

    //博客内容
    @BindView(R.id.content)
    public TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_blog_detail);
        this.mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, true, "博客详情");

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
                            BlogDetailResponse.Blog blog = o.getBlog();
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
                            views.setText(blog.getViews()+"次阅读");
                            comments.setText(blog.getComments()+"条评论");
                            content.setText(blog.getContent());

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



}
