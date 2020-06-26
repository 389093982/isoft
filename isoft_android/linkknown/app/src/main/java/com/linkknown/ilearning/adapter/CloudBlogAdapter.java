package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.CourseDetailActivity;
import com.linkknown.ilearning.model.BlogListResponse;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.DateUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class CloudBlogAdapter extends BaseQuickAdapter<BlogListResponse.BlogArticle, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CloudBlogAdapter(Context mContext, List<BlogListResponse.BlogArticle> blogs) {
        super(R.layout.layout_cloud_blog_item, blogs);
        this.mContext = mContext;
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @NotNull BlogListResponse.BlogArticle blog) {
        //用户头像
//        UIUtils.setImage(mContext,  viewHolder.findView(R.id.first_img), blogArticle.getFirst_img());
        //用户名
        viewHolder.setText(R.id.userNameText,blog.getAuthor());
        ((TextView)viewHolder.findView(R.id.userNameText)).getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        //博客类型
        viewHolder.setText(R.id.catalog_name,blog.getCatalog_name());
        //博客标题
        viewHolder.setText(R.id.blog_title,blog.getBlog_title());
        //创建时间
        viewHolder.setText(R.id.createdTime, DateUtil.formatDate_StandardForm(blog.getCreated_time()));
        //更新时间
        viewHolder.setText(R.id.lastUpdatedTime, DateUtil.formatDate_StandardForm(blog.getLast_updated_time()));
        //first_img图片
        UIUtils.setImage(mContext,  viewHolder.findView(R.id.first_img), blog.getFirst_img());
        //阅读量
        viewHolder.setText(R.id.views,blog.getViews()+"次阅读");
        //评论数
        viewHolder.setText(R.id.comments,blog.getComments()+"条评论");



    }
}