package com.linkknown.ilearning.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseDetailResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseIntroduceFragment extends Fragment {

    private Context mContext;
    private CourseDetailResponse courseDetailResponse;

    public CourseIntroduceFragment(CourseDetailResponse courseDetailResponse) {
        this.courseDetailResponse = courseDetailResponse;
    }

    @BindView(R.id.courseNameText)
    public TextView courseNameText;
    @BindView(R.id.playNumberText)
    public TextView playNumberText;
    @BindView(R.id.courseNumberText)
    public TextView courseNumberText;
    @BindView(R.id.courseShortDescText)
    public TextView courseShortDescText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_introduce, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void init () {
        CourseDetailResponse.Course course = courseDetailResponse.getCourse();
        // 设置课程名称
        courseNameText.setText(course.getCourse_name());
        // 设置播放次数
        playNumberText.setText(course.getWatch_number() + "");
        // 课程集数
        courseNumberText.setText(course.getCourse_number() + "");
        // 课程描述
        courseShortDescText.setText(course.getCourse_short_desc());
    }
}
