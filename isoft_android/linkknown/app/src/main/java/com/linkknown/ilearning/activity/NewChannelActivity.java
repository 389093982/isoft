package com.linkknown.ilearning.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.linkknown.ilearning.Constants;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.section.ChannelMineSection;
import com.linkknown.ilearning.section.ChannelOtherSection;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NewChannelActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        mContext = this;
        ButterKnife.bind(this);

        init();
    }

    private void init () {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        initToolBar(toolbar, true, "拖拽排序");

        final List<NewsChannelBean> enableItems = Arrays.asList(
                new NewsChannelBean("测试","测试",1,1),
                new NewsChannelBean("测试","测试",1,1),
                new NewsChannelBean("测试","测试",1,1)
        );
        final List<NewsChannelBean> disableItems = Arrays.asList(
                new NewsChannelBean("测试","测试",1,1),
                new NewsChannelBean("测试","测试",1,1),
                new NewsChannelBean("测试","测试",1,1)
        );

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        SectionedRecyclerViewAdapter adapter = new SectionedRecyclerViewAdapter();
        adapter.addSection(new ChannelMineSection(this));
        adapter.addSection(new ChannelOtherSection(this));
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER ? 4 : 1;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsChannelBean {
        private String channelId;
        private String channelName;
        private int isEnable;
        private int position;
    }
}
