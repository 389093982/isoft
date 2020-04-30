package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseAdapter;
import com.linkknown.ilearning.model.CourseMeta;
import com.linkknown.ilearning.util.HandlerUtil;
import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.LinkedList;

// 消息传递机制 Handler
public class CourseListActivity extends AppCompatActivity implements HandlerUtil.OnReceiveMessageListener {

    private LinkedList<CourseMeta> mData;
    private ListView courseListView;
    private Context mContext;
    private CourseAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        mContext = this;
        // 发送异步请求获取数据
        loadData();

        courseListView = findViewById(R.id.courseList);
        mAdapter = new CourseAdapter(mData, mContext);
        courseListView.setAdapter(mAdapter);
    }

    private void loadData(){
        this.mData = new LinkedList<>();
        CourseMeta meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);

        meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);


        meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);


        meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);

        meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);

        meta = new CourseMeta();
        meta.setCourseName("测试123");
        meta.setCourseAuthor("你好哇");
        meta.setCourseLabel("helloworld");
        meta.setCourseNumber(10);
        meta.setCourseShortDesc("呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看呃呃呃，世界那么大，我想去看看");
        meta.setCourseSubType("不想编程了");
        meta.setCourseType("真是够无聊");
        meta.setWatchNumber(9999);
        meta.setSmallImage("https://w.wallhaven.cc/full/6k/wallhaven-6k3oox.jpg");
        this.mData.add(meta);

        // 调用 obtain 方法从 Message 池中获取一个 Message 对象
        Message message = Message.obtain();
        message.obj = this.mData;

        HandlerUtil.HandlerHolder handlerHolder = new HandlerUtil.HandlerHolder(this);
        handlerHolder.sendMessage(message);
    }

    @Override
    public void handlerMessage(Message msg) {
        Log.i("CourseListActivity_msg", msg.toString());
        ToastUtil.showText(mContext, msg.toString());
    }
}
