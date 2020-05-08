package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.service.CourseService;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseListActivity extends AppCompatActivity {

    // butterknife 使用注册取代 findViewById
    @BindView(R.id.courseListView)
    public ListView courseListView;

    private Context mContext;
    private CommonAdapter<CourseMetaResponse.CourseMeta> courseCommonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        // 初始化
        ButterKnife.bind(this);

        mContext = this;
        // 发送异步请求获取数据
        initData();

        LiveEventBus.get("courseMetaResponse", CourseMetaResponse.class).observeSticky(this, new Observer<CourseMetaResponse>() {
            @Override
            public void onChanged(CourseMetaResponse courseMetaResponse) {
                courseCommonAdapter = new CommonAdapter<CourseMetaResponse.CourseMeta>((ArrayList<CourseMetaResponse.CourseMeta>) courseMetaResponse.getCourses(), R.layout.item_course) {

                    @Override
                    public void bindView(ViewHolder holder, CourseMetaResponse.CourseMeta courseMeta) {

                        holder.setImage(R.id.courseImageView, UIUtils.replaceMediaUrl(courseMeta.getSmall_image()));

                        holder.setOnClickListener(R.id.courseImageView, v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                            intent.putExtra("course_id", courseMeta.getId());
                            return intent;
                        }));

                        holder.setText(R.id.courseNameView, courseMeta.getCourse_name());
                        holder.setText(R.id.courseShortDescView, courseMeta.getCourse_short_desc());
                        holder.setText(R.id.courseTypeView, courseMeta.getCourse_type() + "/" + courseMeta.getCourse_sub_type());
                        holder.setText(R.id.courseLabelView, courseMeta.getCourse_label());

                        String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
                        holder.setText(R.id.courseNumberView, String.format(courseNumberTextDemo, courseMeta.getCourse_number()));

                        String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
                        holder.setText(R.id.watchNumberView, String.format(watchNumberTextDemo, courseMeta.getWatch_number()));
                    }
                };
                courseListView.setAdapter(courseCommonAdapter);
            }
        });

    }

    private void initData() {
        CourseService.getHotCourseRecommend();
    }
}
