package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseAdapter extends BaseAdapter {

    private List<CourseMetaResponse.CourseMeta> mData;
    private Context mContext;

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @BindView(R.id.courseImageView)
    public ImageView courseImageView;
    @BindView(R.id.courseNameView)
    public TextView courseNameView;
    @BindView(R.id.courseShortDescView)
    public TextView courseShortDescView;
    @BindView(R.id.courseTypeView)
    public TextView courseTypeView;
    @BindView(R.id.courseLabelView)
    public TextView courseLabelView;
    @BindView(R.id.courseNumberView)
    public TextView courseNumberView;
    @BindView(R.id.watchNumberView)
    public TextView watchNumberView;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取第 position 个模型对象
        CourseMetaResponse.CourseMeta courseMeta = mData.get(position);

        // LayoutInflater 布局服务
        // LayoutInflater.from 获取布局服务实例
        // inflate 加载布局的方法
        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_course_item,parent,false);
        // 重新 bind
        ButterKnife.bind(this,convertView);

        // 异步加载图片,使用 Glide 第三方库
        Glide.with(mContext)
                .load(courseMeta.getSmall_image())
                // placeholder 图片加载出来前,显示的图片
                // error 图片加载失败后,显示的图片
                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                .into(courseImageView);

        courseNameView.setText(courseMeta.getCourse_name());
        courseShortDescView.setText(courseMeta.getCourse_short_desc());
        courseTypeView.setText(courseMeta.getCourse_type() + "/" + courseMeta.getCourse_sub_type());
        courseLabelView.setText(courseMeta.getCourse_label());

        String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
        courseNumberView.setText(String.format(courseNumberTextDemo, courseMeta.getCourse_number()));

        String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
        watchNumberView.setText(String.format(watchNumberTextDemo, courseMeta.getWatch_number()));
        return convertView;
    }

    public CourseAdapter() {}

    public CourseAdapter(List<CourseMetaResponse.CourseMeta> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }
}
