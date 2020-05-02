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

public class CVedioAdapter extends BaseAdapter {

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
        // 使用 ViewHolder 来解决每次 onMeasure 导致的多次 getView 调用,发生重用
        ViewHolder viewHolder;
        if (convertView == null) {
            // inflate 加载布局的方法
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cvideo,parent,false);
            // 重新 bind
            ButterKnife.bind(this,convertView);

            viewHolder = new ViewHolder();
            viewHolder.cVideoName = cVideoName;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取第 position 个模型对象
        CourseDetailResponse.CVideos cVideo = this.cVideos.get(position);
        viewHolder.cVideoName.setText(cVideo.getVideo_name());
        return convertView;
    }

    public CVedioAdapter(List<CourseDetailResponse.CVideos> cVideos, Context mContext) {
        this.cVideos = cVideos;
        this.mContext = mContext;
    }

    private class ViewHolder {
        public TextView cVideoName;
    }
}
