package com.linkknown.ilearning.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseDetailResponse;
import com.linkknown.ilearning.service.CourseService;
import com.linkknown.ilearning.util.ui.UIUtils;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseDetailActivity extends AppCompatActivity {

    private Context mContext;
    private Intent intent;
    private List<CourseDetailResponse.CVideo> cVideos = new ArrayList<>();

    @BindView(R.id.detail_goback)
    public ImageView gobackView;

    @BindView(R.id.cVideoRecyclerView)
    public RecyclerView cVideoRecyclerView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // 初始化
        ButterKnife.bind(this);

        mContext = this;

        intent = getIntent();

        // 发送异步请求获取数据
        initData();

        initAdapter();
        // 返回箭头点击事件
        gobackView.setOnClickListener(v -> finish());
    }


    private void initAdapter() {
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(CourseDetailResponse.CVideo.class, new MultiItemView<CourseDetailResponse.CVideo>() {
            @NonNull
            @Override
            public int getLayoutId() {
                return R.layout.item_cvideo;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull CourseDetailResponse.CVideo cVideo, int position) {
                // 视频索引
                String cVideoIndex = mContext.getResources().getString(R.string.cVideoIndex);
                viewHolder.setText(R.id.cVideoIndex, String.format(cVideoIndex, position + 1));
                // 视频名称,去除后缀名后
                viewHolder.setText(R.id.cVideoName, StringUtils.substringBefore(cVideo.getVideo_name(), "."));
                // 视频时长
                if (cVideo.getDuration() > 0) {
                    viewHolder.setText(R.id.cVideoDuration, String.format("%d s", cVideo.getDuration()));
                } else {
                    viewHolder.setVisible(R.id.cVideoDuration, false);
                }

                viewHolder.setOnClickListener(R.id.cVideoName, v -> {
                    UIUtils.gotoActivity(mContext, VideoPlayActivity.class, intent -> {
                        intent.putExtra("course_id", cVideo.getCourse_id());
                        intent.putExtra("video_id", cVideo.getId());
                        intent.putExtra("first_play", cVideo.getFirst_play());
                        return intent;
                    });
                });
            }
        });
        multiTypeAdapter.setItems(cVideos);

        cVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cVideoRecyclerView.setAdapter(multiTypeAdapter);

        LiveEventBus.get("courseDetailResponse_" + intent.getIntExtra("course_id", -1), CourseDetailResponse.class)
                .observeSticky(this, courseDetailResponse -> {
                    if (courseDetailResponse.isSuccess()) {
                        CourseDetailResponse.Course course = courseDetailResponse.getCourse();

                        // 异步加载图片,使用 Glide 第三方库
                        Glide.with(mContext)
                                .load(UIUtils.replaceMediaUrl(course.getSmall_image()))
                                // placeholder 图片加载出来前,显示的图片
                                // error 图片加载失败后,显示的图片
                                .apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.error_image))
                                .into(courseImageView);

                        courseNameView.setText(course.getCourse_name());
                        courseShortDescView.setText(course.getCourse_short_desc());
                        courseTypeView.setText(course.getCourse_type() + "/" + course.getCourse_sub_type());
                        courseLabelView.setText(course.getCourse_label());

                        String courseNumberTextDemo = mContext.getResources().getString(R.string.courseNumberTextDemo);
                        courseNumberView.setText(String.format(courseNumberTextDemo, course.getCourse_number()));

                        String watchNumberTextDemo = mContext.getResources().getString(R.string.watchNumberTextDemo);
                        watchNumberView.setText(String.format(watchNumberTextDemo, course.getWatch_number()));

                        cVideos.addAll(courseDetailResponse.getCVideos());
                    } else {
                        Log.e("onNext =>", "系统异常,请联系管理员~");
                    }
                });
    }

    private void initData () {
        CourseService.showCourseDetailForApp(intent.getIntExtra("course_id", -1));
    }
}
