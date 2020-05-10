package com.linkknown.ilearning.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.model.CourseMetaResponse;
import com.linkknown.ilearning.section.CourseHotRecommendSection;
import com.linkknown.ilearning.service.FavoriteService;
import com.linkknown.ilearning.util.ui.ToastUtil;

import org.apache.commons.collections4.CollectionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class IFavoritesActivity extends AppCompatActivity {

    private Context mContext;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifavorites);
        mContext = this;

        ButterKnife.bind(this);

        // 加载数据
        loadData();
        // 绑定数据
        bindData();
    }

    private void loadData() {
        FavoriteService.getUserFavoriteList("389093982@qq.com", Constants.FAVORITE_TYPE_COURSE_PRAISE);
    }

    private void bindData () {
        LiveEventBus.get(Constants.COURSE_FAVORITE_PREFIX, CourseMetaResponse.class).observeSticky(this, courseMetaResponse -> {
            if (courseMetaResponse != null && CollectionUtils.isNotEmpty(courseMetaResponse.getCourses())) {
                SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
                CourseHotRecommendSection courseHotRecommendSection = new CourseHotRecommendSection(mContext, courseMetaResponse.getCourses());
                sectionedRecyclerViewAdapter.addSection(courseHotRecommendSection);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(sectionedRecyclerViewAdapter);
            }
        });

    }

}
