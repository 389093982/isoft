package com.linkknown.ilearning.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CommonAdapter;
import com.linkknown.ilearning.factory.LinkKnownApiFactory;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.util.HandlerUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// 消息传递机制 Handler
public class CourseListActivity extends AppCompatActivity implements HandlerUtil.OnReceiveMessageListener {

    // butterknife 使用注册取代 findViewById
    @BindView(R.id.courseListView)
    public ListView courseListView;
    private ArrayList<CourseMetaResponse.CourseMeta> mData = new ArrayList<>();

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

        courseCommonAdapter = new CommonAdapter<CourseMetaResponse.CourseMeta>(mData, R.layout.item_course) {

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

    private void initData() {
        LinkKnownApiFactory.getLinkKnownApi().getHotCourseRecommend()
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseMetaResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess() && courseMetaResponse.getCourses() != null) {
                            mData.addAll(courseMetaResponse.getCourses());
                        } else {
                            Log.e("onNext =>", "系统异常,请联系管理员~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError =>", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        courseCommonAdapter.notifyDataSetChanged();
                    }
                });
//        this.mData = new LinkedList<>();
//        CourseMeta meta = new CourseMeta();
//        meta.setCourseName("测试123");
//        meta.setCourseAuthor("你好哇");
//        meta.setCourseLabel("helloworld");
//        meta.setCourseNumber(10);
//        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
//        meta.setCourseSubType("不想编程了");
//        meta.setCourseType("真是够无聊");
//        meta.setWatchNumber(9999);
//        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
//        this.mData.add(meta);

        // 调用 obtain 方法从 Message 池中获取一个 Message 对象
//        Message message = Message.obtain();
//        message.obj = this.mData;
//
//        HandlerUtil.HandlerHolder handlerHolder = new HandlerUtil.HandlerHolder(this);
//        handlerHolder.sendMessage(message);
    }

    @Override
    public void handlerMessage(Message msg) {
        Log.i("CourseListActivity_msg", msg.toString());
        ToastUtil.showText(mContext, msg.toString());
    }
}
