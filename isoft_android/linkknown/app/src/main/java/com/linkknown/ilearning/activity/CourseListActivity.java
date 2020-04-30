package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseAdapter;
import com.linkknown.ilearning.model.CourseMeta;

import java.util.LinkedList;

public class CourseListActivity extends AppCompatActivity {

    private LinkedList<CourseMeta> mData;
    private ListView courseListView;
    private Context mContext;
    private CourseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        mContext = this;

        getData();

        courseListView = findViewById(R.id.courseList);
        mAdapter = new CourseAdapter(mData, mContext);
        courseListView.setAdapter(mAdapter);
    }

    private void getData(){
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
    }
}
