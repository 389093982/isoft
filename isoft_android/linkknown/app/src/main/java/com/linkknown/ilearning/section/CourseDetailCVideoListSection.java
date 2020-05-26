package com.linkknown.ilearning.section;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.activity.VideoPlayActivity;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class CourseDetailCVideoListSection extends Section {

    private Context mContext;
    private List<CourseDetailResponse.CVideo> cVideos;
    private CourseDetailResponse.Course course;

    public CourseDetailCVideoListSection(Context mContext, CourseDetailResponse.Course course, List<CourseDetailResponse.CVideo> cVideos) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_cvideo)
                .build());
        this.mContext = mContext;
        this.cVideos = cVideos;
        this.course = course;
    }

    @Override
    public int getContentItemsTotal() {
        return cVideos.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseDetailResponse.CVideo cVideo = cVideos.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        // 视频索引
        String cVideoIndex = mContext.getResources().getString(R.string.cVideoIndex);
        itemViewHolder.cVideoIndex.setText(String.format(cVideoIndex, position + 1));
        // 视频名称,去除后缀名后
        itemViewHolder.cVideoName.setText(StringUtils.substringBefore(cVideo.getVideo_name(), "."));
        // 视频时长
        if (cVideo.getDuration() > 0) {
            itemViewHolder.cVideoDuration.setText(String.format("%d s", cVideo.getDuration()));
        } else {
            itemViewHolder.cVideoDuration.setVisibility(View.INVISIBLE);
        }
        itemViewHolder.cVideoName.setOnClickListener(v -> {
            UIUtils.gotoActivity(mContext, VideoPlayActivity.class, intent -> {
                intent.putExtra("course_name", course.getCourse_name());
                intent.putExtra("video_name", cVideo.getVideo_name());
                intent.putExtra("course_id", cVideo.getCourse_id());
                intent.putExtra("video_id", cVideo.getId());
                intent.putExtra("first_play", cVideo.getFirst_play());
                return intent;
            });
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView cVideoIndex;
        private TextView cVideoName;
        private TextView cVideoDuration;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cVideoIndex = itemView.findViewById(R.id.cVideoIndex);
            cVideoName = itemView.findViewById(R.id.cVideoName);
            cVideoDuration = itemView.findViewById(R.id.cVideoDuration);
        }
    }
}
