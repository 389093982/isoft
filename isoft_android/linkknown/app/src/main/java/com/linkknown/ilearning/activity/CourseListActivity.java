package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.service.CourseService;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseListActivity extends AppCompatActivity {

    // butterknife 使用注册取代 findViewById
    @BindView(R.id.courseListView)
    public ListView courseListView;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        // 初始化
        ButterKnife.bind(this);

        mContext = this;
        // 发送异步请求获取数据
        initData();

        LiveEventBus.get(Constants.COURSE_HOT_RECOMMEND_PREFIX, CourseMetaResponse.class).observeSticky(this, new Observer<CourseMetaResponse>() {
            @Override
            public void onChanged(CourseMetaResponse courseMetaResponse) {
                MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
                multiTypeAdapter.register(CourseMetaResponse.CourseMeta.class, new MultiItemView<CourseMetaResponse.CourseMeta>() {
                    @NonNull
                    @Override
                    public int getLayoutId() {
                        return R.layout.item_course;
                    }

                    @Override
                    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CourseMetaResponse.CourseMeta courseMeta, int i) {

                        UIUtils.setImage(getApplicationContext(),
                                viewHolder.getConvertView().findViewById(R.id.courseImageView), courseMeta.getSmall_image());

                        viewHolder.setOnClickListener(R.id.courseImageView, v -> UIUtils.gotoActivity(mContext, CourseDetailActivity.class, intent -> {
                            intent.putExtra("course_id", courseMeta.getId());
                            return intent;
                        }));

                        viewHolder.setText(R.id.courseNameView, courseMeta.getCourse_name());
                        viewHolder.setText(R.id.courseShortDescView, courseMeta.getCourse_short_desc());
                        viewHolder.setText(R.id.courseTypeView, courseMeta.getCourse_type() + "/" + courseMeta.getCourse_sub_type());
                        viewHolder.setText(R.id.courseLabelView, courseMeta.getCourse_label());

                        String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
                        viewHolder.setText(R.id.courseNumberView, String.format(courseNumberTextDemo, courseMeta.getCourse_number()));

                        String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
                        viewHolder.setText(R.id.watchNumberView, String.format(watchNumberTextDemo, courseMeta.getWatch_number()));
                    }
                });
                multiTypeAdapter.setItems(courseMetaResponse.getCourses());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(multiTypeAdapter);
            }
        });

    }

    private void initData() {
        CourseService.getHotCourseRecommend();
    }
}
