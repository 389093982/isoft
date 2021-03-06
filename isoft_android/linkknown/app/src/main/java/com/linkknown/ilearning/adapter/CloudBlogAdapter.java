package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.AvailableCouponForPayActivity;
import com.linkknown.ilearning.activity.CloudBlogDetailActivity;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.activity.LoginActivity;
import com.linkknown.ilearning.activity.PersonalCenterActivity;
import com.linkknown.ilearning.model.BlogListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.lxj.xpopup.XPopup;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class CloudBlogAdapter extends BaseQuickAdapter<BlogListResponse.BlogArticle, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    public OnClickAttention onClickAttention;
    public OnClickDeleteBlog onClickDeleteBlog;
    public Fragment fragment;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CloudBlogAdapter(Context mContext, List<BlogListResponse.BlogArticle> blogs, Fragment fragment) {
        super(R.layout.layout_cloud_blog_item, blogs);
        this.mContext = mContext;
        this.fragment = fragment;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @NotNull BlogListResponse.BlogArticle blog) {
        //用户头像
        UIUtils.setImage(mContext,  viewHolder.findView(R.id.headerIcon), blog.getUser().getSmall_icon());
        viewHolder.findView(R.id.headerIcon).setOnClickListener(v -> UIUtils.gotoActivity(mContext, PersonalCenterActivity.class,intent -> {
            intent.putExtra(Constants.USER_NAME,blog.getAuthor());
            return intent;
        }));

        //博客标题
        viewHolder.setText(R.id.blog_title,blog.getBlog_title());
        viewHolder.findView(R.id.blog_title).setOnClickListener(v -> GoBlogDetail(blog));

        //用户名
        viewHolder.setText(R.id.userNameText,blog.getUser().getNick_name());
        viewHolder.findView(R.id.userNameText).setOnClickListener(v -> UIUtils.gotoActivity(mContext,PersonalCenterActivity.class,intent -> {
            intent.putExtra(Constants.USER_NAME,blog.getAuthor());
            return intent;
        }));
        ((TextView)viewHolder.findView(R.id.userNameText)).getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

        //博客类型
        viewHolder.setText(R.id.catalog_name,blog.getCatalog_name());

        //创建时间
        viewHolder.setText(R.id.createdTime, "发布于:"+DateUtil.formatPublishTime(blog.getCreated_time()));
        //更新时间
        viewHolder.setText(R.id.lastUpdatedTime, "更新于:"+DateUtil.formatPublishTime(blog.getLast_updated_time()));
        if (blog.getCreated_time().equals(blog.getLast_updated_time())){
            viewHolder.findView(R.id.lastUpdatedTime).setVisibility(View.GONE);
        }else{
            viewHolder.findView(R.id.lastUpdatedTime).setVisibility(View.VISIBLE);
        }

        //first_img图片
        if (StringUtils.isNotEmpty(blog.getFirst_img())){
            viewHolder.setVisible(R.id.first_img,true);
            UIUtils.setImage(mContext,  viewHolder.findView(R.id.first_img), blog.getFirst_img());
            viewHolder.findView(R.id.first_img).setOnClickListener(v -> GoBlogDetail(blog));
        }else{
            viewHolder.setGone(R.id.first_img,true);
        }

        //阅读量
        viewHolder.setText(R.id.views,blog.getViews()+"");
        viewHolder.findView(R.id.views).setOnClickListener(v -> GoBlogDetail(blog));
        viewHolder.findView(R.id.views_icon).setOnClickListener(v -> GoBlogDetail(blog));
        //评论数
        viewHolder.setText(R.id.comments,blog.getComments()+"");
        viewHolder.findView(R.id.comments).setOnClickListener(v -> GoBlogDetail(blog));
        viewHolder.findView(R.id.comments_icon).setOnClickListener(v -> GoBlogDetail(blog));
        //删除博客
        if (LoginUtil.isLoginUserName(mContext,blog.getAuthor())){
            viewHolder.setVisible(R.id.delete_blog_icon,true);
            viewHolder.findView(R.id.delete_blog_icon).setOnClickListener(v -> onClickDeleteBlog.deleteBlog(blog.getId()+""));
        }else{
            viewHolder.setGone(R.id.delete_blog_icon,true);
        }

        if (blog.isAttention()){
            viewHolder.setGone(R.id.attention_off,true);
        }else{
            if (LoginUtil.isLoginUserName(mContext,blog.getAuthor())){
                viewHolder.setGone(R.id.attention_off,true);
            }else{
                viewHolder.setVisible(R.id.attention_off,true);
                //关注
                viewHolder.findView(R.id.attention_off).setOnClickListener(v -> onClickAttention.doAttention(blog.getAuthor(), viewHolder.getAdapterPosition()));
            }
        }

    }


    public interface OnClickAttention{
        public void doAttention(String userName,int position);
    }

    public void setOnClickAttention(OnClickAttention onClickAttention) {
        this.onClickAttention = onClickAttention;
    }

    public interface OnClickDeleteBlog{
        public void deleteBlog(String id);
    }

    public void setOnClickDeleteBlog(OnClickDeleteBlog onClickDeleteBlog){
        this.onClickDeleteBlog = onClickDeleteBlog;
    };


    //去博客详情界面
    public void GoBlogDetail(BlogListResponse.BlogArticle blog){
        Intent intent = new Intent(mContext, CloudBlogDetailActivity.class);
        intent.putExtra("id",blog.getId()+"");
        intent.putExtra("userName",blog.getAuthor());
        intent.putExtra("headerIcon",blog.getUser().getSmall_icon());
        intent.putExtra("userNameText",blog.getUser().getNick_name());
        fragment.startActivityForResult(intent,189);
    };


}