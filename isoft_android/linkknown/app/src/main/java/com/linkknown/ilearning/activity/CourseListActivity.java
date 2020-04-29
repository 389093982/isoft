package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.linkknown.ilearning.R;
import com.linkknown.ilearning.adapter.CourseAdapter;
import com.linkknown.ilearning.model.CourseMeta;

import java.util.LinkedList;
import java.util.List;

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

        mData = new LinkedList<CourseMeta>();
        mData.add(new CourseMeta("测试1"));
        mData.add(new CourseMeta("测试2"));
        mData.add(new CourseMeta("测试3"));
        mData.add(new CourseMeta("测试4"));
        courseListView = findViewById(R.id.courseList);
        mAdapter = new CourseAdapter(mData, mContext);
        courseListView.setAdapter(mAdapter);
    }
}
