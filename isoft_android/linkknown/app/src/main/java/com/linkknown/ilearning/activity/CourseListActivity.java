package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseAdapter;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.service.LinkKnownApiFactory;
import com.linkknown.ilearning.util.HandlerUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    private List<CourseMetaResponse.CourseMeta> mData = new LinkedList<>();
    private Context mContext;
    private CourseAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        // 初始化
        ButterKnife.bind(this);

        mContext = this;
        // 发送异步请求获取数据
        loadData();

        mAdapter = new CourseAdapter(mData, mContext);
        courseListView.setAdapter(mAdapter);
    }

    private void loadData() {
        LinkKnownApiFactory.getLinkKnownService().getHotCourseRecommend()
                .subscribeOn(Schedulers.io())                   // 请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())      // 切换到主线程运行
                .subscribe(new Observer<CourseMetaResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CourseMetaResponse courseMetaResponse) {
                        if (courseMetaResponse.isSuccess() && courseMetaResponse.getCourseMetas() != null) {
                            mData.addAll(courseMetaResponse.getCourseMetas());
                        } else {
                            ToastUtil.showText(mContext, "系统异常,请联系管理员~");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mAdapter.notifyDataSetChanged();
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
