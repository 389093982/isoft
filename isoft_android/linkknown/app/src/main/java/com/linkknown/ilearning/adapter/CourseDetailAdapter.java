package com.linkknown.ilearning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailAdapter extends BaseAdapter {

    private List<CourseDetailResponse.CVideos> cVideos;
    private Context mContext;

    @BindView(R.id.cVideoName)
    public TextView cVideoName;

    @Override
    public int getCount() {
        return cVideos.size();
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
        // 获取第 position 个模型对象
        CourseDetailResponse.CVideos cVideo = this.cVideos.get(position);
        // inflate 加载布局的方法
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cvideo,parent,false);
        // 重新 bind
        ButterKnife.bind(this,convertView);

        cVideoName.setText(cVideo.getVideo_name());
        return convertView;
    }

    public CourseDetailAdapter(List<CourseDetailResponse.CVideos> cVideos, Context mContext) {
        this.cVideos = cVideos;
        this.mContext = mContext;
    }
}
