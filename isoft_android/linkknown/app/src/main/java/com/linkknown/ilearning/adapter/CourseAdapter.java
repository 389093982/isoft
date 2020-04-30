package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMeta;

import java.util.LinkedList;

public class CourseAdapter extends BaseAdapter {

    private LinkedList<CourseMeta> mData;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseMeta courseMeta = mData.get(position);

        // LayoutInflater 布局服务
        // LayoutInflater.from 获取布局服务实例
        // inflate 加载布局的方法
        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_course_item,parent,false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.courseImageView);
//        TextView txt_aName = (TextView) convertView.findViewById(R.id.txt_aName);
//        TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.txt_aSpeak);

        Glide.with(mContext)
                .load(courseMeta.getSmall_image())
                // placeholder 图片加载出来前,显示的图片
                // error 图片加载失败后,显示的图片
                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                .into(imageView);

//        txt_aName.setText(mData.get(position).getaName());
//        txt_aSpeak.setText(mData.get(position).getaSpeak());
        return convertView;
    }

    public CourseAdapter() {}

    public CourseAdapter(LinkedList<CourseMeta> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }
}
